package com.arun.tltest.handlers;

import com.arun.tltest.AppConfiguration;
import com.arun.tltest.models.User;
import com.arun.tltest.queryFilters.Filter;
import com.arun.tltest.queryFilters.QueryFiltersService;
import com.arun.tltest.repositories.EventRepository;
import com.arun.tltest.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Did not have time to write tests for EventRequestHandler as I wanted to submit it quickly.
 * But since both UserRequestHandler and EventRequestHandler extend RequestHandler which does the actual work
 */

@RunWith(SpringRunner.class)
@WebFluxTest
@Import({AppConfiguration.class, UserRequestHandler.class, EventRequestHandler.class})
public class UserRequestHandlerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private QueryFiltersService queryFiltersService;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testGetAllUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(Flux.just(User.builder().user("user1").workstation("123")._id("userid").build()));
        webClient.get()
                .uri("/user")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("@.[0].user").isEqualTo("user1");
    }

    @Test
    public void testGetUserById() {
        Mockito.when(userRepository.findById("testid")).thenReturn(Mono.just(User.builder().user("tuser2").workstation("123")._id("user2").build()));
        webClient.get()
                .uri("/user/testid")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("@.user").isEqualTo("tuser2");
    }

    @Test
    public void testGetUserByIdWhenNotFound() {
        String notfoundid = "nofoundid";
        Mockito.when(userRepository.findById(notfoundid)).thenReturn(Mono.empty());
        webClient.get()
                .uri("/user/nofoundid")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("@.message").isEqualTo("No entity found for id " + notfoundid);
    }

    @Test
    public void testSearchUserByFilterWhenUsersFound() {
        Filter filter1 = Filter.builder().filterName("filter1").build();
        Filter filter2 = Filter.builder().filterName("filter2").build();

        Mockito.when(queryFiltersService.retrieveFilterByName("filter1")).thenReturn(Optional.of(filter1));
        Mockito.when(queryFiltersService.retrieveFilterByName("filter2")).thenReturn(Optional.of(filter2));

        User uerOne = User.builder().user("user1").build();
        User userTwo = User.builder().user("user2").build();

        Mockito.when(userRepository.search(filter1, filter2)).thenReturn(Flux.just(uerOne, userTwo));

        webClient.get()
                .uri("/user/search?filter=filter1&filter=filter2")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("@.[0].user").isEqualTo("user1")
                .jsonPath("@.[1].user").isEqualTo("user2")
                .jsonPath("$.size()").isEqualTo(2);


    }

    @Test
    public void testSearchWithoutFilterReturnsErrorMessage() {
        webClient.get()
                .uri("/user/search")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("@.message").isEqualTo("No filters provided for search");
    }
}

