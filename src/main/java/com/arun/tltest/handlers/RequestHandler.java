package com.arun.tltest.handlers;

import com.arun.tltest.models.ErrorResponse;
import com.arun.tltest.queryFilters.Filter;
import com.arun.tltest.queryFilters.QueryFiltersService;
import com.arun.tltest.repositories.CustomRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Generic handler implementing find by ID and search using filters
 * for a given entity class.
 *
 * @param <T> The entityClass processed by this request handler
 */
public abstract class RequestHandler<T> {
    private static final String PARAM_NAME_FILTER = "filter";
    private static final String PATH_VARIABLE_NAME_ID = "id";

    protected abstract ReactiveMongoRepository<T, String> getRepository();
    protected abstract CustomRepository<T> getCustomRepository();
    protected abstract Class<T> getEntityClass();

    private QueryFiltersService queryFilterService;

    protected RequestHandler(QueryFiltersService queryFiltersService) {
        this.queryFilterService = queryFiltersService;
    }

    /**
     *
     * @param request The request to process
     * @return Response containing the entity found or a not found response if the entity does not exist
     */
    public Mono<ServerResponse> byId(ServerRequest request) {
        String id = request.pathVariable(PATH_VARIABLE_NAME_ID);
        Mono<T> result = getRepository().findById(id);
        return result.flatMap(t -> ServerResponse.ok().body(Mono.just(t), getEntityClass()))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .body(Mono.just(ErrorResponse.builder().message("No entity found for id " + id).build()), ErrorResponse.class));
    }

    /**
     *
     * Returns  a Bar Request response if no filters are provided
     *
     * @param request requet to process
     * @return Response containing the entities found for the supplied filter
     */
    public Mono<ServerResponse> search(ServerRequest request) {
        List<String> filterNames = request.queryParams().get(PARAM_NAME_FILTER);
        if (filterNames == null || filterNames.isEmpty()) {
            return ServerResponse.badRequest()
                    .body(Mono.just(ErrorResponse.builder().message("No filters provided for search").build()), ErrorResponse.class);
        }

        List<Filter> filters = filterNames.stream().map(queryFilterService::retrieveFilterByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Mono<ServerResponse> response = ServerResponse.ok().body(getCustomRepository().search(filters.toArray(new Filter[0])), getEntityClass());
        return response;
    }
}
