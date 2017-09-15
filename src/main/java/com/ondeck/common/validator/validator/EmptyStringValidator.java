package com.ondeck.common.validator.validator;

import com.ondeck.common.validator.ValidationResult;
import com.ondeck.common.validator.rule.RuleValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by rpatel on 11/16/16.
 */
public class EmptyStringValidator<T, R> implements RuleValidator<T, R> {
    private final List<RuleValidator<T, R>> validators = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public EmptyStringValidator() {
        validators.add(new NotNullValidator<>());
        validators.add(new LengthValidator<T, R>(1, Integer.MAX_VALUE));
    }

    @Override
    public ValidationResult validate(RuleValidatorContext<T, R> context) {
        Optional<ValidationResult> firstFailed =
            validators.stream()
                .map(tRuleValidator -> tRuleValidator.validate(context))
                .filter(result -> !result.isValid())
                .findFirst();
        if (firstFailed.isPresent()) {
            return firstFailed.get();
        } else {
            return ValidationResult.getSuccessResult();
        }
    }
}
