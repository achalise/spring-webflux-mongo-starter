package com.arun.tltest;

import com.arun.tltest.handlers.EventRequestHandler;
import com.arun.tltest.handlers.UserRequestHandler;
import com.arun.tltest.models.Event;
import com.arun.tltest.models.User;
import com.arun.tltest.repositories.EventRepository;
import com.arun.tltest.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class AppConfiguration {
    @Bean
    RouterFunction<ServerResponse> routes(UserRepository userRepository, UserRequestHandler userRequestHandler,
                                          EventRepository eventRepository, EventRequestHandler eventRequestHandler) {
        return RouterFunctions
                .route(GET("/user"), r -> ServerResponse.ok().body(userRepository.findAll(), User.class))
                .andRoute(GET("/user/search"), userRequestHandler::search)
                .andRoute(GET("/user/{id}"), userRequestHandler::byId)
                .andRoute(GET("/event"), r -> ServerResponse.ok().body(eventRepository.findAll(), Event.class))
                .andRoute(GET("/event/search"), eventRequestHandler::search)
                .andRoute(GET("/event/{id}"), eventRequestHandler::byId);
    }
}
