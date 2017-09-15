package com.ondeck.common.validator.validator;

import com.ondeck.common.validator.ValidationResult;
import com.ondeck.common.validator.rule.RuleValidatorContext;

/**
 * Created by rpatel on 11/15/16.
 */
public class NotNullValidator<T> implements RuleValidator {
    @Override
    public ValidationResult validate(RuleValidatorContext context) {
        if (context.get() == null) {
            return ValidationResult.getUnSuccessResult();
        } else {
            return ValidationResult.getSuccessResult();
        }
    }
}
