package com.arun.tltest.repositories;

import com.arun.tltest.models.Event;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

public class EventRepositoryImpl extends CustomRepositoryImpl<Event> {
    public EventRepositoryImpl(ReactiveMongoTemplate template) {
        super(template);
    }

    @Override
    public Class<Event> getEntityClass() {
        return Event.class;
    }
}
