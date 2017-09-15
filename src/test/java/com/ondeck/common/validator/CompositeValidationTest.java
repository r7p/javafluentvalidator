package com.ondeck.common.validator;

import com.ondeck.common.validator.model.Address;
import com.ondeck.common.validator.model.TestPerson;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for composite validator
 */
public class CompositeValidationTest {
    //Define validators

    //Validates an address
    class AddressValidator extends AbstractValidator<Address> {
        public AddressValidator() {
            ruleFor(Address::getStreetAddress).notNull().withMessage("Street address cannot be null; it is ${propertyValue}");
            ruleFor(Address::getZipCode).ofLength(5, 5).withMessage("Zipcode must be 5 character long; it is ${propertyValue}");
        }
    }

    //Validates Person POJO, uses AddressValidator to validate address
    class TestPersonValidator extends AbstractValidator<TestPerson> {
        public TestPersonValidator() {
            ruleFor(TestPerson::getFirstName).notEmpty().withMessage("First name cannot be blank; it is ${propertyValue}");
            ruleFor(TestPerson::getAddress).setRuleValidator(new AddressValidator());
        }
    }

    private final  TestPersonValidator validator = new TestPersonValidator();

    @Test
    public void testCompositeInvalid() {
        //Data that is to be validated
        TestPerson aPerson = new TestPerson();
        aPerson.setAddress(new Address("abc", "NY", "NY", "01"));

        //Execute validator
        ValidationResult result = validator.validate(aPerson);

        //Assertions
        assertNotNull(result);
        assertFalse(result.isValid());
        assertEquals(2, result.getValidationMessages().size());
        assertThat(result.getValidationMessages().get(0).getMessage(), equalTo("First name cannot be blank; it is null"));
        assertThat(result.getValidationMessages().get(1).getMessage(), equalTo("Zipcode must be 5 character long; it is 01"));
    }

    @Test
    public void testCompositeValid() {
        //Data that is to be validated
        TestPerson aPerson = new TestPerson();
        aPerson.setFirstName("Jane");
        aPerson.setAddress(new Address("1400 Broadway", "NY", "NY", "10018"));

        //Execute validator
        ValidationResult result = validator.validate(aPerson);

        //Assertions
        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(0, result.getValidationMessages().size());
    }
}
