package com.ondeck.common.validator.rule;

/**
 * Created by rpatel on 11/16/16.
 */
public enum RuleSeverity {
    INFO(0), WARN(1), ERROR(2);

    private final int weight;
    RuleSeverity(int weight) {
        this.weight = weight;
    }

    public RuleSeverity add(RuleSeverity newRuleSeverity) {
        int newWeight = newRuleSeverity.weight;
        if (newWeight > weight) {
            return newRuleSeverity;
        } else {
            return this;
        }
    }
}
