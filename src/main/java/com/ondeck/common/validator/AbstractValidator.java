package com.ondeck.common.validator;

import com.ondeck.common.validator.rule.Rule;
import com.ondeck.common.validator.rule.RuleBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * All validator must extend this class
 */
@SuppressWarnings("unchecked")
public abstract class AbstractValidator<T> implements Validator<T> {
    private final List<Rule> rules = new ArrayList<>();

    protected <R> RuleBuilder<T, R> ruleFor(Function<T, R> targetFunction) {
        //create PropertyRule by default
        RuleBuilder<T, R> ruleBuilder = RuleBuilder.createWithTarget(targetFunction);
        rules.add(ruleBuilder.getRule());
        return ruleBuilder;
    }



    public ValidationResult validate(T targetObject) {
        final ValidatorContext context = new ValidatorContext(targetObject);

        ValidationResult validationResult = ValidationResult.getSuccessResult();
        rules.forEach(rule -> {
                validationResult.add(rule.validate(context));
        });
        return validationResult;
    }
}
