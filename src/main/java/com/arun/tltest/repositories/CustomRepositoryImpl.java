package com.arun.tltest.repositories;

import com.arun.tltest.queryFilters.Filter;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class CustomRepositoryImpl<T> implements CustomRepository {

    private ReactiveMongoTemplate mongoTemplate;

    public abstract Class<T> getEntityClass();

    public CustomRepositoryImpl(ReactiveMongoTemplate template) {
        this.mongoTemplate = template;
    }

    @Override
    public Flux<T> search(Filter... filters) {
        if (filters == null || filters.length < 1) {
           return Flux.empty();
        }
        Query query = new Query();
        Criteria criteria = new Criteria();
        List<Criteria> list = Stream.of(filters).map(Filter::toCriteria).collect(Collectors.toList());
        criteria.andOperator(list.toArray(new Criteria[0]));
        query.addCriteria(criteria);

        return mongoTemplate.find(query, getEntityClass());
    }
}
