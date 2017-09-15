package com.ondeck.common.validator.validator;

import com.ondeck.common.validator.ValidationResult;
import com.ondeck.common.validator.Validator;
import com.ondeck.common.validator.rule.RuleValidatorContext;

/**
 * Created by rpatel on 11/16/16.
 */
public interface RuleValidator<T, R> extends Validator {
    ValidationResult validate(RuleValidatorContext<T, R> context);
}
