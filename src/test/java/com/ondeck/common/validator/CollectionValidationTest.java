package com.ondeck.common.validator;

import com.ondeck.common.validator.model.Car;
import com.ondeck.common.validator.model.TestPerson;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for collection validation
 */
public class CollectionValidationTest {

    //Define validators
    class CarValidator extends AbstractValidator<Car> {
        public CarValidator() {
            ruleFor(Car::getMake).notEmpty().withMessage("Car make cannot be empty");
            ruleFor(Car::getModel).notNull().withMessage("Car model cannot be null");
        }
    }

    class CollectionValidator extends AbstractValidator<TestPerson> {
        public CollectionValidator() {
            ruleFor(TestPerson::getFirstName).notEmpty().withMessage("First name cannot be blank; it is ${propertyValue}");
            ruleFor(TestPerson::getCars)
                .collectionNotNullOrEmpty()
                .setCollectionValidator(new CarValidator())
                .withMessage("Person must have at least one car");
        }
    }

    private final CollectionValidator validator = new CollectionValidator();

    @Test
    public void testPreCollectionNullCheck() {
        //setup data
        TestPerson aPerson = new TestPerson();
        aPerson.setFirstName("");
        aPerson.setCars(null);

        //Execute validator
        ValidationResult result = validator.validate(aPerson);

        //Assert results
        assertNotNull(result);
        assertFalse(result.isValid());

        List<ValidationMessage> validationMessages = result.getValidationMessages();
        assertThat(validationMessages.size(), is(2));
        assertThat(validationMessages.get(0).getMessage(), is("First name cannot be blank; it is "));
        assertThat(validationMessages.get(1).getMessage(), is("Person must have at least one car"));
    }

    @Test
    public void testPreCollectionEmptyCheck() {
        //setup data
        TestPerson aPerson = new TestPerson();
        aPerson.setFirstName("");

        //Execute validator
        ValidationResult result = validator.validate(aPerson);

        //Assert results
        assertNotNull(result);
        assertFalse(result.isValid());

        List<ValidationMessage> validationMessages = result.getValidationMessages();
        assertThat(validationMessages.size(), is(2));
        assertThat(validationMessages.get(0).getMessage(), is("First name cannot be blank; it is "));
        assertThat(validationMessages.get(1).getMessage(), is("Person must have at least one car"));
    }

    @Test
    public void testCollectionInvalid() {
        class CollectionValidatorLocal extends AbstractValidator<TestPerson> {
            public CollectionValidatorLocal() {
                ruleFor(TestPerson::getFirstName).notEmpty().withMessage("First name cannot be blank; it is ${propertyValue}");
                ruleFor(TestPerson::getCars)
                    .setCollectionValidator(new CarValidator())
                    .withMessage("Person must have at least one car");
            }
        }

        //Set up test data
        TestPerson aPerson = new TestPerson();
        aPerson.setFirstName("");
        aPerson.setCars(Arrays.asList(new Car("", "Pinto"), new Car("Tesla", "S80"), new Car("VW", null)));

        //Execute validator
        ValidationResult result = new CollectionValidatorLocal().validate(aPerson);

        //Assert results
        assertNotNull(result);
        assertFalse(result.isValid());

        List<ValidationMessage> validationMessages = result.getValidationMessages();
        assertThat(validationMessages.size(), is(3));
        assertThat(validationMessages.get(0).getMessage(), is("First name cannot be blank; it is "));
        assertThat(validationMessages.get(1).getMessage(), is("Car make cannot be empty"));
        assertThat(validationMessages.get(2).getMessage(), is("Car model cannot be null"));
    }

    @Test
    public void testCollectionValid() {
        //Set up test data
        TestPerson aPerson = new TestPerson();
        aPerson.setFirstName("John");
        aPerson.setCars(Arrays.asList(new Car("Ford", "Pinto"), new Car("Tesla", "S80"), new Car("VW", "")));

        //Execute validator
        ValidationResult result = validator.validate(aPerson);

        //Assert results
        assertNotNull(result);
        assertTrue(result.isValid());

        List<ValidationMessage> validationMessages = result.getValidationMessages();
        assertThat(validationMessages.size(), is(0));
    }


}
