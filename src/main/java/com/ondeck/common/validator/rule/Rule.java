package com.ondeck.common.validator.rule;


import com.ondeck.common.validator.AbstractValidator;
import com.ondeck.common.validator.ValidationResult;
import com.ondeck.common.validator.Validator;
import com.ondeck.common.validator.ValidatorContext;

import java.util.function.Function;

/**
 * Created by rpatel on 11/15/16.
 */
public interface Rule<T, R> {
    //void setTargetFunction(Function<T, R> targetFunction);
    void setWhenFunction(Function<T, Boolean> whenFunction);
    void setCollectionValidator(AbstractValidator<T> validator);
    void addValidator(Validator<T> validator);
    void setRuleMessage(String ruleMessage);
    void setRuleSeverity(RuleSeverity ruleSeverity);

    ValidationResult validate(ValidatorContext context);
}
