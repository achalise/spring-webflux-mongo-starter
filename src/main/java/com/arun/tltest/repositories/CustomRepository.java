package com.arun.tltest.repositories;

import com.arun.tltest.queryFilters.Filter;
import reactor.core.publisher.Flux;

public interface CustomRepository<T> {
    Flux<T> search(Filter... filters);
}
