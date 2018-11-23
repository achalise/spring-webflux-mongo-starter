package com.arun.tltest.repositories;

import com.arun.tltest.models.Event;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EventRepository extends ReactiveMongoRepository<Event, String>, CustomRepository<Event> {
}
