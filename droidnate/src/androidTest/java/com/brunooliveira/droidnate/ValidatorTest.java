package com.brunooliveira.droidnate;

import android.test.AndroidTestCase;

import com.brunooliveira.droidnate.exception.DroidnateException;
import com.brunooliveira.droidnate.util.ValidatorUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bruno on 22/03/2015.
 */
public class ValidatorTest extends AndroidTestCase {

    public void testValidateNullEntityObject() {
        User user = null;
        try {
            ValidatorUtil.validate(user);
            fail("Should return DroidnateException");
        } catch (DroidnateException e) {
            assertEquals("The entity can't be null.",
                    e.getMessage());
        }
    }

    public void testValidateNotEntityObject() {
        String object = new String("VALIDATOR TEST");
        try {
            ValidatorUtil.validate(object);
            fail("Should return DroidnateException");
        } catch (DroidnateException e) {
            assertEquals("The class [String] isn't an entity. Please, use @Entity annotation in this class.",
                    e.getMessage());
        }
    }

    public void testValidateCPF() {
        User user = new User();
        user.setCpf("111.111.111-11");
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("CPF format invalid", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

    public void testValidatePast() {
        User user = new User();
        user.setCpf("181.437.116-84");
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(dateOfBirth.get(Calendar.YEAR) + 10, 9, 11);
        user.setDateOfBirth(dateOfBirth);
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("dateOfBirth can't be future", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

    public void testValidateEmail() {
        User user = new User();
        user.setCpf("181.437.116-84");
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(dateOfBirth.get(Calendar.YEAR) - 10, 9, 11);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail("bruno.vky@.gmail.com");
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("Email format invalid", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

    public void testValidateFuture() {
        User user = new User();
        user.setCpf("181.437.116-84");
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(dateOfBirth.get(Calendar.YEAR) - 10, 9, 11);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail("bruno.vky@gmail.com");
        Date endOfContract = dateOfBirth.getTime();
        user.setEndOfContract(endOfContract);
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("endOfContract can't be past", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

    public void testValidateNotNull() {
        User user = new User();
        user.setCpf("181.437.116-84");
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(dateOfBirth.get(Calendar.YEAR) - 10, 9, 11);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail("bruno.vky@gmail.com");
        Calendar endOfContractCalendar = Calendar.getInstance();
        endOfContractCalendar.setTime(dateOfBirth.getTime());
        endOfContractCalendar.set(Calendar.YEAR, dateOfBirth.get(Calendar.YEAR) + 20);
        Date endOfContract = endOfContractCalendar.getTime();
        user.setEndOfContract(endOfContract);
        user.setJob(null);
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("job can't be null", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

    public void testValidateLength() {
        User user = new User();
        user.setCpf("181.437.116-84");
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(dateOfBirth.get(Calendar.YEAR) - 10, 9, 11);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail("bruno.vky@gmail.com");
        Calendar endOfContractCalendar = Calendar.getInstance();
        endOfContractCalendar.setTime(dateOfBirth.getTime());
        endOfContractCalendar.set(Calendar.YEAR, dateOfBirth.get(Calendar.YEAR) + 20);
        Date endOfContract = endOfContractCalendar.getTime();
        user.setEndOfContract(endOfContract);
        user.setJob("Developer");
        user.setName("Test");
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("name length can't be lower than 5 and greater than 100", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

    public void testValidateValidator() {
        User user = new User();
        user.setCpf("181.437.116-84");
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(dateOfBirth.get(Calendar.YEAR) - 10, 9, 11);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail("bruno.vky@gmail.com");
        Calendar endOfContractCalendar = Calendar.getInstance();
        endOfContractCalendar.setTime(dateOfBirth.getTime());
        endOfContractCalendar.set(Calendar.YEAR, dateOfBirth.get(Calendar.YEAR) + 20);
        Date endOfContract = endOfContractCalendar.getTime();
        user.setEndOfContract(endOfContract);
        user.setJob("Developer");
        user.setName("Bruno");
        user.setNumOfFriends(0);
        user.setPassword("bruno_oliveira");
        user.setPhone("12 1111-2222");
        user.setRg("12.345.678-9");
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("Validator invalid", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

    public void testValidateNotEmpty() {
        User user = new User();
        user.setCpf("181.437.116-84");
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(dateOfBirth.get(Calendar.YEAR) - 10, 9, 11);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail("bruno.vky@gmail.com");
        Calendar endOfContractCalendar = Calendar.getInstance();
        endOfContractCalendar.setTime(dateOfBirth.getTime());
        endOfContractCalendar.set(Calendar.YEAR, dateOfBirth.get(Calendar.YEAR) + 20);
        Date endOfContract = endOfContractCalendar.getTime();
        user.setEndOfContract(endOfContract);
        user.setJob("Developer");
        user.setName("Bruno");
        user.setNumOfFriends(10);
        user.setPassword("");
        user.setRg("12.345.678-9");
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("password can't be empty or null", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

    public void testValidatePattern() {
        User user = new User();
        user.setCpf("181.437.116-84");
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(dateOfBirth.get(Calendar.YEAR) - 10, 9, 11);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail("bruno.vky@gmail.com");
        Calendar endOfContractCalendar = Calendar.getInstance();
        endOfContractCalendar.setTime(dateOfBirth.getTime());
        endOfContractCalendar.set(Calendar.YEAR, dateOfBirth.get(Calendar.YEAR) + 20);
        Date endOfContract = endOfContractCalendar.getTime();
        user.setEndOfContract(endOfContract);
        user.setJob("Developer");
        user.setName("Bruno");
        user.setNumOfFriends(10);
        user.setPassword("bruno_oliveira");
        user.setPhone("12 1111-AAAA");
        user.setRg("12.345.678-9");
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("Pattern format invalid", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

    public void testValidateMask() {
        User user = new User();
        user.setCpf("181.437.116-84");
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(dateOfBirth.get(Calendar.YEAR) - 10, 9, 11);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail("bruno.vky@gmail.com");
        Calendar endOfContractCalendar = Calendar.getInstance();
        endOfContractCalendar.setTime(dateOfBirth.getTime());
        endOfContractCalendar.set(Calendar.YEAR, dateOfBirth.get(Calendar.YEAR) + 20);
        Date endOfContract = endOfContractCalendar.getTime();
        user.setEndOfContract(endOfContract);
        user.setJob("Developer");
        user.setName("Bruno");
        user.setNumOfFriends(10);
        user.setPassword("bruno_oliveira");
        user.setPhone("12 1111-2222");
        user.setRg("1.234.567-8");
        try {
            assertFalse(ValidatorUtil.validate(user));
            assertEquals("Mask format invalid", ValidatorUtil.getErrorMessage());
        } catch (DroidnateException e) {
            fail(e.getMessage());
        }
    }

}