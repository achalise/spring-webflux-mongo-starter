package com.arun.tltest.queryFilters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Filter {
    private String filterName;
    private String attribute;
    private String operator;
    private String value;
    private Range range;

    public Criteria toCriteria() {
        Criteria criteria;
        FilterOperator filterOperator = FilterOperator.valueOf(operator.toUpperCase());
        switch (filterOperator) {
            case EQ:
                criteria = (Criteria.where(attribute).regex(value));
                break;
            case GTE:
                criteria = (Criteria.where(attribute).gte(value));
                break;
            case LTE:
                criteria = (Criteria.where(attribute).lte(value));
                break;
            default:
                criteria = new Criteria();

        }
        return criteria;
    }

    public boolean validate() {
        return validateEitherRangeOrValue() && validateOperator();
    }

    private boolean validateOperator() {
        if (FilterOperator.valueOf(operator.toUpperCase()) == null) {
            return false;
        }
        FilterOperator filterOperator = FilterOperator.valueOf(operator.toUpperCase());
        boolean eq = filterOperator.equals(FilterOperator.EQ);

        boolean rangeExists = range != null;
        boolean valueExists = value != null;


        return (rangeExists & eq) ^ (valueExists);
    }

    private boolean validateEitherRangeOrValue() {
        boolean rangeExists = range != null;
        boolean valueExists = value != null;
        return rangeExists ^ valueExists;
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Range {
    private String from;
    private String to;
}

//- Unknown operators are not allowed.
//        - Both `value` *and* `range` are not allowed.
//        - When operator is `gte` *or* `lte` then `range` is not allowed.
