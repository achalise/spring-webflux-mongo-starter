package com.arun.tltest.repositories;

import com.arun.tltest.queryFilters.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class CustomRepositoryImpl<T> implements CustomRepository {

    private Logger logger = LoggerFactory.getLogger(CustomRepositoryImpl.class);

    private ReactiveMongoTemplate mongoTemplate;

    public abstract Class<T> getEntityClass();

    public CustomRepositoryImpl(ReactiveMongoTemplate template) {
        this.mongoTemplate = template;
    }

    /**
     * If no filters are provided, it returns an empty search result
     * @param filters
     * @return search result
     */
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
        logger.debug("The criteria for query " + criteria.toString());

        return mongoTemplate.find(query, getEntityClass());
    }
}
