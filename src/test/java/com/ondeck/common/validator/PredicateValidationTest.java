package com.ondeck.common.validator;

import com.ondeck.common.validator.model.TestPerson;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test Predicate validators
 */
public class PredicateValidationTest {

    class PersonValidator extends AbstractValidator<TestPerson> {
        public PersonValidator() {
            ruleFor(TestPerson::getFirstName)
                .when(TestPerson::isSuperHero)
                .notNull()
                .predicate(PredicateValidationTest::nameStartsWithJ)
                .withMessage("If you're a superhero, your name must NOT start with J; your name is ${propertyValue}");

            ruleFor(TestPerson::getLoanBalance)
                .notNull()
                .predicate(data -> validateFloat(data, 6, 2))
                .withMessage("Loan balance must be in format FLOAT(6, 2), it is ${propertyValue}");
        }
    }

    private final PersonValidator validator = new PersonValidator();

    @Test
    public void testPredicateValidator() {
        //setup test data
        TestPerson aPerson = new TestPerson();
        aPerson.setSuperHero(true);
        aPerson.setFirstName("Jane");
        aPerson.setLoanBalance(new BigDecimal("12345.678"));

        //Execute validation
        ValidationResult result = validator.validate(aPerson);

        //Assert result
        assertNotNull(result);
        assertFalse(result.isValid());
        List<ValidationMessage> messages = result.getValidationMessages();
        assertThat(messages.size(), is(2));
        assertThat(messages.get(0).getMessage(), is("If you're a superhero, your name must NOT start with J; your name is Jane"));
        assertThat(messages.get(1).getMessage(), is("Loan balance must be in format FLOAT(6, 2), it is 12345.678"));
    }

    @Test
    public void testPredicateValidatorSuccess() {
        //setup test data
        TestPerson aPerson = new TestPerson();
        aPerson.setSuperHero(true);
        aPerson.setFirstName("Mary");
        aPerson.setLoanBalance(new BigDecimal("1234.678"));

        //Execute validation
        ValidationResult result = validator.validate(aPerson);

        //Assert result
        assertNotNull(result);
        assertTrue(result.isValid());
        List<ValidationMessage> messages = result.getValidationMessages();
        assertThat(messages.size(), is(0));
    }

    private static boolean nameStartsWithJ(String firstName) {
        if (firstName.startsWith("J")) {
            return false;
        } else {
            return true;
        }
    }

    private static boolean validateFloat(BigDecimal data, int precision, int scale) {
        if (data.scale() > scale) {
            data = data.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        if (data.precision() > precision) {
            return false;
        } else {
            return true;
        }
    }
}
