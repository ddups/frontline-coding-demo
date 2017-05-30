package com.frontline.demo.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.frontline.demo.controller.InvalidInputException;
import com.frontline.demo.converter.InputConverter;
import com.frontline.demo.domain.Record;

public class ConversionTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private InputConverter converter = new InputConverter();

    /**
     * Testing various valid input strings. Strings are made up of the original
     * sample string and modified by varying the location of the sub-records. 
     * 
     * @formatter:off
     *  1. Original sample string
     *  2. Begins with sub-record
     *  3. Ends with sub-record
     *  4. Begins with two sub-records
     *  5. Ends with two sub-records
     *  6. Record with single child, no sub-record
     *  7. Record with two children, no sub-record
     *  8. Record with sub-record as only child
     *  9. Record with sub-record as only child with second layer sub-record
     * 10. Original sample string with values
     * @formatter:on
     */
    @Test
    public void validInputTest() {
        String[] inputStrings = { // @formatter:off
                "(id,created,employee(id,firstname,employeeType(id),lastname),location)",
                "(employee(id,firstname,employeeType(id),lastname),id,created,location)",
                "(id,created,location,employee(id,firstname,employeeType(id),lastname))",
                "(employee(employeeType(id),id,firstname,lastname),id,created,location)",
                "(id,created,location,employee(id,firstname,lastname,employeeType(id)))",
                "(employee)",
                "(employee,location)",
                "(employee(id))",
                "(employee(employeeType(id)))",
                "(id:123456,created:20170530,employee(id:4444,firstname:derek,employeeType(id:404),lastname:dupuis),location:nashua)"
        }; // @formatter:on

        for (String inputString : inputStrings) {
            assertTrue(converter.validInput(inputString));
        }
    }

    /**
     * Testing that when sorting in alphabetical order, different input strings
     * will produce the same output hierarchy.
     */
    @Test
    public void differentInputSameOutputTest() {
        String baseString = "(id,created,employee(id,firstname,employeeType(id),lastname),location)";
        String[] altStrings = { // @formatter:off
                "(employee(id,firstname,employeeType(id),lastname),id,created,location)",
                "(id,created,location,employee(id,firstname,employeeType(id),lastname))",
                "(employee(employeeType(id),id,firstname,lastname),id,created,location)",
                "(id,created,location,employee(id,firstname,lastname,employeeType(id)))",
        }; // @formatter:on

        Record record1 = null;
        Record record2 = null;
        for (String altString : altStrings) {
            try {
                record1 = converter.convertInputAlpha(baseString);
                record2 = converter.convertInputAlpha(altString);
            } catch (InvalidInputException e) {
            }

            assertNotNull(record1);
            assertNotNull(record2);
            assertEquals(record1, record2);
        }
    }

    /**
     * Testing that when sorting in input order, different input strings will
     * produce different output hierarchies.
     */
    @Test
    public void differentInputDifferentOutputTest() {
        String baseString = "(id,created,employee(id,firstname,employeeType(id),lastname),location)";
        String[] altStrings = { // @formatter:off
                "(employee(id,firstname,employeeType(id),lastname),id,created,location)",
                "(id,created,location,employee(id,firstname,employeeType(id),lastname))",
                "(employee(employeeType(id),id,firstname,lastname),id,created,location)",
                "(id,created,location,employee(id,firstname,lastname,employeeType(id)))",
        }; // @formatter:on

        ArrayList<String> list1 = null;
        ArrayList<String> list2 = null;
        for (String altString : altStrings) {
            try {
                list1 = converter.convertInput(baseString);
                list2 = converter.convertInput(altString);
            } catch (InvalidInputException e) {
            }

            assertNotNull(list1);
            assertNotNull(list2);
            assertNotEquals(list1, list2);
        }
    }

    /**
     * Testing whitespace in valid input strings. Strings are first converted
     * and then the hierarchy of the records are compared.
     */
    @Test
    public void validInputWhitespaceTest() {
        String inputString1 = "(employee(employeeType(id)))";
        String inputString2 = "( employee ( employeeType ( id ) ) )";

        Record record1 = null;
        Record record2 = null;
        try {
            record1 = converter.convertInputAlpha(inputString1);
            record2 = converter.convertInputAlpha(inputString2);
        } catch (InvalidInputException e) {
        }

        assertNotNull(record1);
        assertNotNull(record2);
        assertEquals(record1, record2);
    }

    /**
     * Testing various invalid input strings. 
     * 
     * @formatter:off
     *  1. Empty record
     *  2. Record with empty sub-record
     *  3. Begins with comma
     *  4. Ends with comma
     *  5. Begins with two left parentheses - mismatched
     *  6. Ends with two right parentheses - mismatched
     *  7. Ends with left parenthesis
     *  8. Missing initial left parenthesis
     *  9. Missing ultimate right parenthesis
     * 10. Missing both outside parentheses
     * @formatter:on
     */
    @Test
    public void invalidInputTest() {
        String[] inputStrings = { // @formatter:off
                "()", 
                "(employee())", 
                "(employee,location,)", 
                "(,employee,location)", 
                "((employee)",
                "(employee))", 
                "(employee()", 
                "employee)", 
                "(employee", 
                "employee", 
        }; // @formatter:on

        for (String inputString : inputStrings) {
            assertFalse(converter.validInput(inputString));
        }
    }
    
    /**
     * Testing the exception thrown from an invalid input string
     * 
     * @throws InvalidInputException
     */
    @Test
    public void invalidInputExceptionTest() throws InvalidInputException{
        String inputString = "(employee())";
        
        thrown.expect(InvalidInputException.class);
        thrown.expectMessage("Invalid input received, could not convert.");
        converter.convertInputAlpha(inputString);
    }
}
