package com.ondeck.common.validator;

import com.ondeck.common.validator.model.TestPerson;
import com.ondeck.common.validator.rule.RuleSeverity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for built-in or default validators
 */
public class AbstractValidatorTest {

    class NotEmptyValidator extends AbstractValidator<TestPerson> {
        public NotEmptyValidator() {
            ruleFor(TestPerson::getFirstName).notEmpty().withMessage("First name of person cannot be blank; it is ${propertyValue}");
        }
    }

    private final NotEmptyValidator validator = new NotEmptyValidator();

    @Test
    public void testNotNullValidation() {
        //Define validator
        class PropertyValidator extends AbstractValidator<TestPerson> {
            public PropertyValidator() {
                ruleFor(TestPerson::getFirstName).notNull().withMessage("First name of person cannot be null; it is ${propertyValue}");

            }
        }

        //Setup test data
        TestPerson aPerson = new TestPerson();

        //Execute validator
        PropertyValidator validator = new PropertyValidator();
        ValidationResult result = validator.validate(aPerson);

        //Assert results
        assertNotNull(result);
        assertNotNull(result.getValidationMessages());
        assertEquals(1, result.getValidationMessages().size());
        assertEquals("First name of person cannot be null; it is null", result.getValidationMessages().get(0).getMessage());
        assertEquals(RuleSeverity.ERROR, result.getValidationMessages().get(0).getRuleSeverity());
    }

    @Test
    public void testNotNullAndLengthValidation() {
        String firstName = "abcdefghijklmn";
        int maxLength = 10;
        class PropertyValidator extends AbstractValidator<TestPerson> {
            public PropertyValidator() {
                ruleFor(TestPerson::getFirstName).notNull().ofLength(0, maxLength).withMessage("First name of person cannot be null or more than " + maxLength + " characters; it is ${propertyValue}");
            }
        }

        //Setup test data
        TestPerson aPerson = new TestPerson();
        aPerson.setFirstName(firstName);

        //Execute validator
        PropertyValidator validator = new PropertyValidator();
        ValidationResult result = validator.validate(aPerson);

        //Assert results
        assertNotNull(result);
        assertNotNull(result.getValidationMessages());
        assertEquals(1, result.getValidationMessages().size());
        assertEquals("First name of person cannot be null or more than " + maxLength + " characters; it is " + firstName, result.getValidationMessages().get(0).getMessage());
        assertEquals(RuleSeverity.ERROR, result.getValidationMessages().get(0).getRuleSeverity());
    }

    @Test
    public void testEmptyBlankValidation() {
        //Setup test data
        String firstName = "";
        TestPerson aPerson = new TestPerson();
        aPerson.setFirstName(firstName);

        //Execute validator
        ValidationResult result = validator.validate(aPerson);

        //Assert results
        assertNotNull(result);
        assertNotNull(result.getValidationMessages());
        assertEquals(1, result.getValidationMessages().size());
        assertEquals("First name of person cannot be blank; it is " + firstName, result.getValidationMessages().get(0).getMessage());
        assertEquals(RuleSeverity.ERROR, result.getValidationMessages().get(0).getRuleSeverity());
    }

    @Test
    public void testEmptyNullValidation() {
        //Setup test data
        TestPerson aPerson = new TestPerson();

        //Execute validator
        ValidationResult result = validator.validate(aPerson);

        //Assert results
        assertNotNull(result);
        assertNotNull(result.getValidationMessages());
        assertEquals(1, result.getValidationMessages().size());
        assertEquals("First name of person cannot be blank; it is " + aPerson.getFirstName(), result.getValidationMessages().get(0).getMessage());
        assertEquals(RuleSeverity.ERROR, result.getValidationMessages().get(0).getRuleSeverity());
    }

    @Test
    public void testWhen() {
        class PropertyValidator extends AbstractValidator<TestPerson> {
            public PropertyValidator() {
                    ruleFor(TestPerson::getFirstName)
                        .ofLength(0, 10)
                        .withMessage("First name of person cannot be null or more than 10 characters; it is ${propertyValue}")
                        .when(TestPerson::isSuperHero);
            }
        }
        PropertyValidator validator = new PropertyValidator();

        //should not perform validation as this person is not a super hero
        TestPerson aPerson = new TestPerson();
        ValidationResult result = validator.validate(aPerson);
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getValidationMessages().isEmpty());


        //This super hero has no name, hence validation should fail
        TestPerson aPerson2 = new TestPerson();
        aPerson2.setSuperHero(true);
        result = validator.validate(aPerson2);
        assertFalse(result.isValid());
        assertEquals(1, result.getValidationMessages().size());
        assertEquals("First name of person cannot be null or more than 10 characters; it is null", result.getValidationMessages().get(0).getMessage());

        //Let's give our superhero a name
        aPerson2.setFirstName("Batman");
        result = validator.validate(aPerson2);
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getValidationMessages().isEmpty());

        //This super hero has too long username, which is not valid
        aPerson2.setFirstName("Batman Superman Spiderman");
        result = validator.validate(aPerson2);
        assertNotNull(result);
        assertFalse(result.isValid());
        assertEquals(1, result.getValidationMessages().size());
        assertEquals("First name of person cannot be null or more than 10 characters; it is Batman Superman Spiderman", result.getValidationMessages().get(0).getMessage());

        //This is not a super hero, although he is named one
        aPerson2.setSuperHero(false);
        aPerson2.setFirstName("Batman Superman Spiderman");
        result = validator.validate(aPerson2);
        assertNotNull(result);
        assertTrue(result.isValid());
        assertTrue(result.getValidationMessages().isEmpty());

    }
}
