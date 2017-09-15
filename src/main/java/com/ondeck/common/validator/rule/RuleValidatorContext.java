package com.ondeck.common.validator.rule;

/**
 * Created by rpatel on 11/16/16.
 */
public class RuleValidatorContext<T, R> {
    private final R propertyValue;

    private RuleValidatorContext(R propertyValue) {
        this.propertyValue = propertyValue;
    }

    @SuppressWarnings("unchecked")
    public static <R> RuleValidatorContext using(R propertyValue) {
        return new RuleValidatorContext(propertyValue);
    }

    public R get() {
        return propertyValue;
    }
}
