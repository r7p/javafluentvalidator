package com.ondeck.common.validator;

import com.ondeck.common.validator.rule.RuleSeverity;

/**
 * Created by rpatel on 11/16/16.
 */
public class ValidationMessage {
    private final String message;
    private final RuleSeverity ruleSeverity;

    public ValidationMessage(String message, RuleSeverity ruleSeverity) {
        this.message = message;
        this.ruleSeverity = ruleSeverity;
    }

    public String getMessage() {
        return message;
    }

    public RuleSeverity getRuleSeverity() {
        return ruleSeverity;
    }
}
