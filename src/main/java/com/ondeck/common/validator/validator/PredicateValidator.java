package com.ondeck.common.validator.validator;

import com.ondeck.common.validator.ValidationResult;
import com.ondeck.common.validator.rule.RuleValidatorContext;

import java.util.function.Predicate;

/**
 * Validates using given Predicate
 */
public class PredicateValidator<T, R> implements RuleValidator<T, R> {
    private final Predicate<R> predicate;

    public PredicateValidator(Predicate<R> predicate) {
        this.predicate = predicate;
    }

    @Override
    public ValidationResult validate(RuleValidatorContext<T, R> context) {
        if (predicate.test(context.get())) {
            return ValidationResult.getSuccessResult();
        } else {
            return ValidationResult.getUnSuccessResult();
        }
    }
}
