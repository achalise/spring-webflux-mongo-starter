package com.arun.tltest.handlers;

import com.arun.tltest.models.Event;
import com.arun.tltest.queryFilters.QueryFiltersService;
import com.arun.tltest.repositories.CustomRepository;
import com.arun.tltest.repositories.EventRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;

@Service
public class EventRequestHandler extends RequestHandler<Event> {

    private EventRepository eventRepository;

    public EventRequestHandler(EventRepository eventRepository, QueryFiltersService queryFiltersService) {
        super(queryFiltersService);
        this.eventRepository = eventRepository;
    }


    @Override
    protected ReactiveMongoRepository<Event, String> getRepository() {
        return this.eventRepository;
    }

    @Override
    protected CustomRepository<Event> getCustomRepository() {
        return this.eventRepository;
    }

    @Override
    protected Class<Event> getEntityClass() {
        return Event.class;
    }
}
