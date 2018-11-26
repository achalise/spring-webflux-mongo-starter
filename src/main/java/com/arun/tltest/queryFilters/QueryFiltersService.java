package com.arun.tltest.queryFilters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class QueryFiltersService implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(QueryFiltersService.class);
    private List<Filter> filters;

    @Value("classpath:filters/filters.json")
    private Resource filterDefinitions;

    public QueryFiltersService() {
    }

    public Optional<Filter> retrieveFilterByName(String filterName) {
        Optional<Filter> filterByName = filters.stream().filter(f -> f.getFilterName().equals(filterName)).findFirst();
        return filterByName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        filters = populateFilters(filterDefinitions);
    }

    protected List<Filter> populateFilters(Resource filterDefinitions) throws IOException {
        try (InputStream inputStream = filterDefinitions.getInputStream()) {

            ObjectMapper mapper = new ObjectMapper();

            TypeReference<List<Filter>> filterList = new TypeReference<List<Filter>>() {
            };
            List<Filter> filters = Collections.unmodifiableList(mapper.readValue(inputStream, filterList));
            List<String> errors = validateFilters(filters);
            if (!errors.isEmpty()) {
                errors.forEach(logger::error);
                throw new IllegalArgumentException("Invalid filter configuration");
            } else {
                return filters;
            }
        }
    }

    private List<String> validateFilters(List<Filter> filters) {
        List<String> errors = new ArrayList<>();
        filters.forEach(filter -> {
            if (!filter.isValid()) {
                errors.add("Invalid filter " + filter.getFilterName());
            }
        });
        return errors;
    }
}
