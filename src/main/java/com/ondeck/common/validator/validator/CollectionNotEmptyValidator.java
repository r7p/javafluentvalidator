package com.ondeck.common.validator.validator;

import com.ondeck.common.validator.ValidationResult;
import com.ondeck.common.validator.rule.RuleValidatorContext;

import java.util.Collection;

/**
 * Validates whether given collection is null or empty
 */
public class CollectionNotEmptyValidator<T, R> implements RuleValidator<T, R> {

    @Override
    public ValidationResult validate(RuleValidatorContext<T, R> context) {
        R t = context.get();
        if (t != null && t instanceof Collection && !((Collection) t).isEmpty()) {
            return ValidationResult.getSuccessResult();
        } else {
            return ValidationResult.getUnSuccessResult();
        }
    }
}
