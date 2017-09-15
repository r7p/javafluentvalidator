package com.ondeck.common.validator;

import com.ondeck.common.validator.model.TestPerson;
import com.ondeck.common.validator.validator.IntegerValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Custom validator
 */
public class CustomValidatorTest {

    class PersonValidator extends AbstractValidator<TestPerson> {
        public PersonValidator() {
            ruleFor(TestPerson::getCapacityToPay)
                .notNull()
                .setValidator(new IntegerValidator<>(6))
                .withMessage("Person's capacity to pay cannot be more than 6 digits; it is ${propertyValue}");
        }
    }

    private final PersonValidator validator = new PersonValidator();

    @Test
    public void testCustomValidator() {
        TestPerson aPerson = new TestPerson();
        aPerson.setCapacityToPay(1000000);

        ValidationResult result = validator.validate(aPerson);

        assertNotNull(result);
        assertFalse(result.isValid());
        List<ValidationMessage> messages = result.getValidationMessages();
        assertThat(messages.size(), is(1));
        assertThat(messages.get(0).getMessage(), is("Person's capacity to pay cannot be more than 6 digits; it is 1000000"));
    }

    @Test
    public void testCustomValidatorSuccess() {
        TestPerson aPerson = new TestPerson();
        aPerson.setCapacityToPay(999999);

        ValidationResult result = validator.validate(aPerson);

        assertNotNull(result);
        assertTrue(result.isValid());
        List<ValidationMessage> messages = result.getValidationMessages();
        assertTrue(messages.isEmpty());
    }

}
