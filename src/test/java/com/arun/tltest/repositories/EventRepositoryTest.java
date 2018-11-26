package com.arun.tltest.repositories;

import com.arun.tltest.models.Event;
import com.arun.tltest.queryFilters.Filter;
import com.arun.tltest.queryFilters.Range;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testShouldSaveAndFind() {
        Mono<Event> savedEvent = eventRepository.save(Event.builder()._id("eventid")
                .time(123l).type("testtype").user("user1").build());

        Flux<Event> events = savedEvent.thenMany(eventRepository.findAll());
        StepVerifier.create(events)
                .expectNextMatches(event -> event.get_id().equals("eventid"))
                .as("Should save event and retrieve it in subsequent findall query")
                .verifyComplete();
    }

    @Test
    public void testSearchByFilterThatMatches() {
        Mono<Event> savedEvent = eventRepository.save(Event.builder()._id("eventid")
                .time(123l).type("testtype").user("user1").build());

        Flux<Event> events = savedEvent.thenMany(eventRepository.search(Filter.builder().attribute("user").operator("eq").value("user1").build()));
        StepVerifier.create(events)
                .expectNextMatches(event -> event.get_id().equals("eventid"))
                .as("Should save event and retrieve it in subsequent findall query")
                .verifyComplete();
    }

    @Test
    public void testSearchByFilterWhenNoMatch() {
        Mono<Event> savedEvent = eventRepository.save(Event.builder()._id("eventid")
                .time(123l).type("testtype").user("user").build());

        Flux<Event> events = savedEvent.thenMany(eventRepository.search(Filter.builder().attribute("user").operator("eq").value("user3").build()));
        StepVerifier.create(events)
                .expectNextCount(0)
                .as("Should save event and retrieve it in subsequent findall query")
                .verifyComplete();
    }

    @Test
    public void testSearchByRangeFilterWithResults() {
        Flux<Event> returnedEvents = Flux.just(1l, 2l, 3l, 4l, 5l)
                .map(i -> (Event.builder().time(i).build()))
                .flatMap(event -> eventRepository.save(event))
                .thenMany(eventRepository.search(Filter.builder().attribute("time").operator("eq").range(
                        Range.builder().from("1").to("3").build()).build()));

        StepVerifier.create(returnedEvents).expectNextCount(3);
    }

    @Test
    public void testSearchByRangeFilterWithNoResults() {
        Flux<Event> returnedEvents = Flux.just(1l, 2l, 3l, 4l, 5l)
                .map(i -> (Event.builder().time(i).build()))
                .flatMap(event -> eventRepository.save(event))
                .thenMany(eventRepository.search(Filter.builder().attribute("time").operator("eq").range(
                        Range.builder().from("6").to("9").build()).build()));

        StepVerifier.create(returnedEvents).expectNextCount(0);
    }
}