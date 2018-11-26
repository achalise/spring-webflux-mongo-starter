package com.arun.tltest.models;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class EventPOJOTest {
    @Test
    public void testShouldConstruct() {
        Event event = new Event("testid", "testtype", 123123123L, "testuser");
        assertThat(event.getUser()).as("User is correctly populated").isEqualTo("testuser");
        assertThat(event.getTime()).isEqualTo(123123123L);
        assertThat(event.getType()).isEqualTo("testtype");
        assertThat(event.get_id()).isEqualTo("testid");
    }
}