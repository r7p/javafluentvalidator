package com.ondeck.common.validator.message;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rpatel on 11/16/16.
 */
public class MessageBuilder {
    private static final String PROPERTY_VALUE_TOKEN = "propertyValue";

    public static <T> String buildMessageWithValue(T propertyValue, String msgString) {
        if (msgString == null) {
            return "Property failed to meet validation; value was " + propertyValue;
        } else {
            Map<String, Object> values = new HashMap<>();
            if (propertyValue == null) {
                values.put(PROPERTY_VALUE_TOKEN, "null");
            } else {
                values.put(PROPERTY_VALUE_TOKEN, propertyValue);
            }
            return StrSubstitutor.replace(msgString, values);
        }
    }
}
