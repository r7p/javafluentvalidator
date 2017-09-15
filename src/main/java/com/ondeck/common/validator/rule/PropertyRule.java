package com.ondeck.common.validator.rule;

import com.ondeck.common.validator.AbstractValidator;
import com.ondeck.common.validator.ValidationMessage;
import com.ondeck.common.validator.ValidationResult;
import com.ondeck.common.validator.Validator;
import com.ondeck.common.validator.ValidatorContext;
import com.ondeck.common.validator.message.MessageBuilder;
import com.ondeck.common.validator.validator.RuleValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Main implementation of {@linkplain Rule} interface.  Validates POJO's property.
 */
@SuppressWarnings("unchecked")
public class PropertyRule<T, R> implements Rule {
    private final List<Validator> validators = new ArrayList<>();
    private AbstractValidator<R> collectionValidator;

    private final Function<T, R> targetFunction;
    private Optional<Function<T, Boolean>> whenFunction = Optional.empty();

    private String ruleMessage;
    private RuleSeverity ruleSeverity = RuleSeverity.ERROR;


    protected PropertyRule(Function<T, R> targetFunction) {
        this.targetFunction = targetFunction;
    }

    @Override
    public void setRuleMessage(String ruleMessage) {
        this.ruleMessage = ruleMessage;
    }

    @Override
    public void setRuleSeverity(RuleSeverity ruleSeverity) {
        this.ruleSeverity = ruleSeverity;
    }

    @Override
    public void setWhenFunction(Function whenFunction) {
        this.whenFunction = Optional.ofNullable(whenFunction);
    }

    @Override
    public void setCollectionValidator(AbstractValidator validator) {
        this.collectionValidator = validator;
    }

    @Override
    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    @Override
    public ValidationResult validate(ValidatorContext context) {
        if (whenConditionSatisfied(context)) {
            Optional<ValidationResult> resultOptional = doValidation(context);
            if (resultOptional.isPresent()) {
                ValidationResult validationResult = resultOptional.get();
                addValidationMessage(context, validationResult);
                return validationResult;
            }
        }
        return ValidationResult.getSuccessResult();
    }

    private Optional<ValidationResult> doValidation(ValidatorContext context) {
        Optional<ValidationResult> propertyResultOptional = doPropertyValidation(context);
        Optional<ValidationResult> collectionResultOptional = doCollectionValidation(context);

        return combineValidationResults(propertyResultOptional, collectionResultOptional);
    }

    private Optional<ValidationResult> doPropertyValidation(ValidatorContext context) {
        return
            validators.stream()
                .map(validator -> {
                    if (validator instanceof RuleValidator) {
                        return ((RuleValidator) validator).validate(RuleValidatorContext.using(context.getPropertyValue(targetFunction)));
                    } else {
                        //since this is composite validator, target object will be property value itself
                        return ((AbstractValidator) validator).validate(context.getPropertyValue(targetFunction));
                    }
                })
                .filter(validationResult -> !validationResult.isValid())
                .findFirst();
    }

    private Optional<ValidationResult> doCollectionValidation(ValidatorContext context) {
        if (isCollectionValidation()) {
            Collection<R> propertyValues = (Collection) context.getPropertyValue(targetFunction);
            ValidationResult validationResult = ValidationResult.getSuccessResult();
            if (propertyValues != null) {
                propertyValues.forEach(r -> {
                    validationResult.add(collectionValidator.validate(r));
                });
            }
            return Optional.of(validationResult);
        } else {
            return Optional.empty();
        }
    }

    private void addValidationMessage(ValidatorContext context, ValidationResult validationResult) {
        if (!validationResult.hasMessages() && !validationResult.isValid()) {
            //only add message for individual rules, skip for composite rules
            String message = MessageBuilder.buildMessageWithValue(context.getPropertyValue(targetFunction), ruleMessage);
            validationResult.addValidationMessage(new ValidationMessage(message, ruleSeverity));
        }
    }

    private Optional<ValidationResult> combineValidationResults(Optional<ValidationResult> resultOptional1,
        Optional<ValidationResult> resultOptional2) {
        if (resultOptional1.isPresent() && resultOptional2.isPresent()) {
            return Optional.of(resultOptional1.get().add(resultOptional2.get()));
        } else if (resultOptional1.isPresent()) {
            return resultOptional1;
        } else if (resultOptional2.isPresent()) {
            return resultOptional2;
        } else {
            return Optional.empty();
        }
    }

    private boolean whenConditionSatisfied(ValidatorContext<T, R> context) {
        return whenFunction.map(tBooleanFunction -> tBooleanFunction.apply(context.getTargetObject())).orElse(true);
    }

    private boolean isCollectionValidation() {
        return collectionValidator != null;
    }
}
