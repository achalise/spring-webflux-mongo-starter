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

    /**
     * Assumes that the Filter has been correctly created, validated during their creation at the start up of
     * the application
     *
     * @return Criteria
     */
    public Criteria toCriteria() {
        Criteria criteria;
        FilterOperator filterOperator = FilterOperator.valueOf(operator.toUpperCase());

        switch (filterOperator) {
            case EQ:
                if (range != null) {
                    criteria = new Criteria().andOperator(
                            Criteria.where(attribute).gte(valueOfRange(range.getFrom())),
                            Criteria.where(attribute).lte(valueOfRange(range.getTo()))
                    );
                } else {
                    criteria = (Criteria.where(attribute).regex(value));
                }
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

    /**
     * Only supporting either string or number for the query
     *
     * @param value
     * @return numeric equivalent or the original string
     */
    private Object valueOfRange(String value) {
        try {
            Double d = Double.parseDouble(value);
            return d;
        } catch (NumberFormatException nfe) {
            return value;
        }
    }

    public boolean isValid() {
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
        return (rangeExists && range.validateRange()) ^ valueExists;
    }
}

//- Unknown operators are not allowed.
//        - Both `value` *and* `range` are not allowed.
//        - When operator is `gte` *or* `lte` then `range` is not allowed.
