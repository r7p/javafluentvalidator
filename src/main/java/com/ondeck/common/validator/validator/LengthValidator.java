package com.ondeck.common.validator.validator;

import com.ondeck.common.validator.ValidationResult;
import com.ondeck.common.validator.rule.RuleValidatorContext;

/**
 * Created by rpatel on 11/16/16.
 */
public class LengthValidator<T, R> implements RuleValidator<T, R> {
    private final int minLength, maxLength;

    public LengthValidator(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

   @Override
    public ValidationResult validate(RuleValidatorContext<T, R> context) {
       R t = context.get();
       if (t == null || !(t instanceof String) ||
           ((String) t).length() < minLength ||
           ((String) t).length() > maxLength) {
           return ValidationResult.getUnSuccessResult();
       } else {
           return ValidationResult.getSuccessResult();
       }
    }


}
