package com.arun.tltest.queryFilters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(QueryFiltersService.class)
public class QueryFiltersServiceTest {

    @Autowired
    QueryFiltersService queryFiltersService;

    @Test
    public void testQueryFilterServiceIsCorrectlyInitialised() {
        assertThat(queryFiltersService).isNotNull();
        Filter filter1 =  queryFiltersService.retrieveFilterByName("filter1").get();
        assertThat(filter1.getFilterName()).isEqualTo("filter1");

        Optional<Filter> nonExistent = queryFiltersService.retrieveFilterByName("none");
        assertThat(nonExistent.isPresent()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFilterThrowsIllegalStateException() throws Exception {
        List<Filter> filters = queryFiltersService.populateFilters(new ClassPathResource("filters/invalid-filters.json"));
    }

}