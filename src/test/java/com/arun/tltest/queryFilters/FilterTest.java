package com.arun.tltest.queryFilters;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class FilterTest {

    @Test
    public void testFilterOnlyAllowsEitherRangeOrValue() {
        Filter filter = Filter.builder().filterName("f1").attribute("a1").operator("eq").build();
        assertThat("Filter must have either range or value", filter.validate(), Matchers.equalTo(false));
        filter = Filter.builder().filterName("f1").attribute("a1").operator("eq").value("123").build();
        assertThat("Filter must have either range or value", filter.validate(), Matchers.equalTo(true));
        filter = Filter.builder().filterName("f1").attribute("a1").operator("eq").value("123").range(Range.builder().from("1").to("5").build()).build();
        assertThat("Filter cannot have both range and value", filter.validate(), Matchers.equalTo(false));
    }


}