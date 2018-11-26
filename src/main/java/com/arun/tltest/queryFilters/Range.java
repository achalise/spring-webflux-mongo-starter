package com.arun.tltest.queryFilters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Range {
    private String from;
    private String to;

    boolean validateRange() {
        boolean fromExists = from != null && !from.trim().isEmpty();
        boolean toExists = to != null && !to.trim().isEmpty();
        return fromExists & toExists;
    }
}
