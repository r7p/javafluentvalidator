package com.ondeck.common.validator.validator;

import com.ondeck.common.validator.ValidationResult;
import com.ondeck.common.validator.rule.RuleValidatorContext;

/**
 * Make sure that integer is of specified number of digits
 */
public class IntegerValidator<T, R> implements RuleValidator<T, R> {
    private final int intLength;

    public IntegerValidator(int intLength) {
        if (intLength <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer");
        }
        this.intLength = intLength;
    }

    @Override
    public ValidationResult validate(RuleValidatorContext<T, R> context) {
        R data = context.get();
        if (data instanceof Integer && (Integer) data <= getMaxDigitInt(intLength)) {
            return ValidationResult.getSuccessResult();
        } else {
            return ValidationResult.getUnSuccessResult();
        }
    }

    private int getMaxDigitInt(int maxDigits) {
        return ((int) Math.pow(10, maxDigits)) - 1 ;
    }
}
