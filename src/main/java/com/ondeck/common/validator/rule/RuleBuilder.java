package com.ondeck.common.validator.rule;

import com.ondeck.common.validator.AbstractValidator;
import com.ondeck.common.validator.validator.CollectionNotEmptyValidator;
import com.ondeck.common.validator.validator.EmptyStringValidator;
import com.ondeck.common.validator.validator.LengthValidator;
import com.ondeck.common.validator.validator.NotNullValidator;
import com.ondeck.common.validator.validator.PredicateValidator;
import com.ondeck.common.validator.validator.RuleValidator;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Builder for {@linkplain Rule}
 */
@SuppressWarnings("unchecked")
public class RuleBuilder<T, R> {
    private final Rule<T, R> rule;

    private RuleBuilder(Rule<T, R> rule) {
        this.rule = rule;
    }

    public static <T, R> RuleBuilder createWithTarget(Function<T, R> targetFunction) {
        PropertyRule<T, R> rule = new PropertyRule<T, R>(targetFunction);
        return new RuleBuilder(rule);
    }

    public Rule<T, R> getRule() {
        return rule;
    }

    /**
     * Execute this rule only when this predicate is satisfied
     *
     * @param whenFunction  Function that returns true or false
     * @return
     */
    public RuleBuilder<T, R> when(Function<T, Boolean> whenFunction) {
        rule.setWhenFunction(whenFunction);
        return this;
    }

    /**
     * Validation message to return when this rule validation fails.
     * Can use ${propertyValue} to replace property value in message
     *
     * @param ruleMessage   String message to return when validation fails
     * @return
     */
    public RuleBuilder<T, R> withMessage(String ruleMessage) {
        rule.setRuleMessage(ruleMessage);
        return this;
    }

    /**
     * Severity of this validation.  See {@linkplain RuleSeverity}
     *
     * @param ruleSeverity
     * @return
     */
    public RuleBuilder<T, R> severity(RuleSeverity ruleSeverity) {
        rule.setRuleSeverity(ruleSeverity);
        return this;
    }

    /**
     * When validating composite object, use this validator.
     * e.g. If person object contains Address object, and Address has validator defined,
     * use this validator in Person validator to validate person's address.
     *
     * @param validator
     * @return
     */
    public RuleBuilder<T, R> setRuleValidator(AbstractValidator validator) {
        rule.addValidator(validator);
        return this;
    }

    /**
     * Validate each object within a collection with this validator.
     * Will return a message for each validation failure.
     *
     * @param validator
     * @return
     */
    public RuleBuilder<T, R> setCollectionValidator(AbstractValidator validator) {
        rule.setCollectionValidator(validator);
        return this;
    }

    /**
     * Make sure this property is not null, else failed message
     * is returned.
     *
     * @return
     */
    public RuleBuilder<T, R> notNull() {
        rule.addValidator(new NotNullValidator<T>());
        return this;
    }

    /**
     * Make sure this string property is greater than minLength AND
     * less than maxLength, else failure message is returned.
     *
     * @param minLength
     * @param maxLength
     * @return
     */
    public RuleBuilder<T, R> ofLength(int minLength, int maxLength) {
        rule.addValidator(new LengthValidator<>(minLength, maxLength));
        return this;
    }

    /**
     * Make sure this String property is not null and > 0 character long
     *
     * @return
     */
    public RuleBuilder<T, R> notEmpty() {
        rule.addValidator(new EmptyStringValidator<>());
        return this;
    }

    /**
     * Make sure this Collection property is not null and not empty
     *
     * @return
     */
    public RuleBuilder<T, R> collectionNotNullOrEmpty() {
        rule.addValidator(new CollectionNotEmptyValidator<>());
        return this;
    }

    /**
     * Validate this property using your own validator.
     *
     * @param validator
     * @return
     */
    public RuleBuilder<T, R> setValidator(RuleValidator<T, R> validator) {
        rule.addValidator(validator);
        return this;
    }

    public RuleBuilder<T, R> predicate(Predicate<R> predicate) {
        rule.addValidator(new PredicateValidator<>(predicate));
        return this;
    }
}
