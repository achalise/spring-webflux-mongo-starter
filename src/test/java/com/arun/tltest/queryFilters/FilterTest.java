package com.arun.tltest.queryFilters;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.junit.Assert.assertThat;

public class FilterTest {

    @Test
    public void testFilterOnlyAllowsEitherRangeOrValue() {
        Filter filter = Filter.builder().filterName("f1").attribute("a1").operator("eq").build();
        assertThat("Filter must have either range or value", filter.isValid(), Matchers.equalTo(false));
        filter = Filter.builder().filterName("f1").attribute("a1").operator("eq").value("123").build();
        assertThat("Filter must have either range or value", filter.isValid(), Matchers.equalTo(true));
        filter = Filter.builder().filterName("f1").attribute("a1").operator("eq").value("123").range(Range.builder().from("1").to("5").build()).build();
        assertThat("Filter cannot have both range and value", filter.isValid(), Matchers.equalTo(false));
    }

    @Test
    public void testCriteriaIsGeneratedFromFilter() {
        Filter filter = Filter.builder().filterName("f1")
                .attribute("username")
                .operator("eq")
                .value("userone")
                .build();
        Criteria criteria = filter.toCriteria();
        Criteria expectedCriteria = new Criteria().andOperator(Criteria.where("username").regex("userone"));
        Assertions.assertThat(criteria.equals(expectedCriteria)).as("Criteria correctly populated");
    }

    @Test
    public void testRangeFilterIsCorrectlyValidated() {
        Filter filter = Filter.builder().filterName("f1")
                .attribute("time")
                .operator("gte")
                .range(Range.builder().from("123").to("890").build())
                .build();
        Assertions.assertThat(filter.isValid()).isFalse().as("Invalid operator " + filter.getOperator() + ", Only 'eq' operator is allowed for Range");

        filter.setOperator("lte");
        Assertions.assertThat(filter.isValid()).isFalse().as("Invalid operator " + filter.getOperator() + ", Only 'eq' operator is allowed for Range");

        filter.setOperator("eq");
        Assertions.assertThat(filter.isValid()).isTrue().as("Only 'eq' operator is allowed for Range");
    }
}
