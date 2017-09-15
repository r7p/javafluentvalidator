package com.ondeck.common.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rpatel on 11/15/16.
 */
public class ValidationResult {
    private final List<ValidationMessage> validationMessages = new ArrayList<>();
    private boolean isValid;


    private ValidationResult(boolean isValid) {
        this.isValid = isValid;
    }

    public static ValidationResult getSuccessResult() {
        return new ValidationResult(true);
    }

    public static ValidationResult getUnSuccessResult() {
        return new ValidationResult(false);
    }

    public ValidationResult add(ValidationResult result) {
        if (!result.isValid) {
            isValid = false;
            validationMessages.addAll(result.validationMessages);
        }
        return this;
    }

    public List<ValidationMessage> getValidationMessages() {
        return Collections.unmodifiableList(validationMessages);
    }

    public void addValidationMessage(ValidationMessage  message) {
        validationMessages.add(message);
    }
    public boolean isValid() {
        return isValid;
    }
    public boolean hasMessages() {
        return !validationMessages.isEmpty();
    }
}
