package com.ondeck.common.validator;

import java.util.function.Function;

/**
 * Created by rpatel on 11/15/16.
 */
public class ValidatorContext<T, R> {
    private final T targetObject;

    public ValidatorContext(T targetObject) {
        this.targetObject = targetObject;
    }

    public T getTargetObject() {
        return targetObject;
    }

    public R getPropertyValue(Function<T, R> targetFunction) {
        R propertyValue = targetFunction.apply(targetObject);
        return propertyValue;
    }
}
