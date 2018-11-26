package com.arun.tltest.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class EventDocumentTest {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Test
    public void testShouldStoreAndRetrive() {
        Mono<Event> savedEvent = mongoTemplate.save(Event.builder().time(123123l).user("userone").type("typeone").build());
        Flux<Event> retrievedEvents = savedEvent.thenMany(mongoTemplate.findAll(Event.class));
        StepVerifier.create(retrievedEvents)
                .as("Subscription to saved event publisher from Mongo")
                .expectNextMatches(event -> event.getUser().equals("userone"))
                .verifyComplete();
    }
}
