package com.frontline.demo.converter;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frontline.demo.Constants;
import com.frontline.demo.controller.InvalidInputException;
import com.frontline.demo.domain.Record;

/**
 * InputConverter.java
 * 
 * @author Derek Dupuis
 * 
 *         The main functional class. Contains two conversion methods, one that
 *         produces a result in the order as input, and another that produces a
 *         result in alphabetical order, as well as some helper methods.
 */
public class InputConverter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * Original quick and dirty solution. Simply iterates over each character
     * and builds a list of words. If a "(" is found, a dash is prepended to the
     * next word. If a ")" is found, the number of dashes is decremented by 1.
     * The list is then printed in order.
     * 
     * @param inputString
     * @return
     * @throws InvalidInputException 
     */
    public ArrayList<String> convertInput(String inputString) throws InvalidInputException {
        // clean up the input string to fix small issues with syntax
        inputString = inputString.toLowerCase().replaceAll("\\s", "");
        
        if (!validInput(inputString))
            throw new InvalidInputException();
        
        ArrayList<String> returnArray = new ArrayList<String>();

        int index = 0, dashCount = 0;
        StringBuilder dashBldr = new StringBuilder();
        StringBuilder wordBldr = new StringBuilder();
        for (char c : inputString.toCharArray()) {
            if (c == ' ') {// ignore whitespace
            } else if (c == '(') {
                // if a left parenthesis is found and the index is at 0 throw
                // away, else add the current word and increment the number
                // of dashes
                if (index > 0) {
                    logger.debug("Adding word: " + wordBldr.toString());
                    returnArray.add(wordBldr.toString());
                    dashBldr.append('-');
                    dashCount++;
                    wordBldr = new StringBuilder(dashBldr.toString() + " ");
                }
            } else if (c == ')') {
                // if a right parenthesis is found, decrement dash count unless
                // it is the last char, then the current word can be added
                if (index < inputString.length() - 1) {
                    dashBldr.deleteCharAt(--dashCount);
                } else {
                    returnArray.add(wordBldr.toString());
                }
            } else if (c == ',') {
                // if a comma is found, add the current word and reset builder
                logger.debug("Adding word: " + wordBldr.toString());
                returnArray.add(wordBldr.toString());
                wordBldr = (dashBldr.length() > 0) ? new StringBuilder(dashBldr.toString() + " ")
                        : new StringBuilder(dashBldr.toString());
            } else {
                // it's a letter, append it
                wordBldr.append(c);
            }

            index++;
        }

        return returnArray;
    }

    /**
     * Breaks down the input string into Record objects and returns a top level
     * Record which maps the entire input string to a hierarchy.
     * 
     * The method loops through the input string and finds the sets of
     * parentheses, starting with the inner-most set. It then reads whatever is
     * between the parentheses as "children" of the record, and the word
     * immediately preceding the parentheses is used to create the record. As
     * each record is created, it is added to a map. If a record has a "child"
     * that is also a Record present in the map, the child is set as a
     * sub-record. Once all children have been processed the record string is
     * removed from the input string. This process repeats until there is
     * nothing left to parse. It then returns the top record.
     * 
     * @param inputString
     * @return a Record object representing the top level record from the
     *         converted input string.
     * @throws InvalidInputException
     *             if an invalid input string is passed
     */
    public Record convertInputAlpha(String inputString) throws InvalidInputException {
        // clean up the input string to fix small issues with syntax
        inputString = inputString.toLowerCase().replaceAll("\\s", "");

        if (!validInput(inputString))
            throw new InvalidInputException();

        TreeMap<String, Record> recordMap = new TreeMap<>();

        Record topRecord = null;
        StringBuilder bldr;
        int rightParenIdx, leftParenIdx;

        // break down input string into records, starting with the inner-most
        // record. as each record is created, remove it from the input string
        // until there is nothing left to parse. then return the top record
        // which contains all the strings that need to be printed.
        while (topRecord == null) {
            bldr = new StringBuilder();
            rightParenIdx = inputString.indexOf(')');
            leftParenIdx = inputString.lastIndexOf('(');

            // get children (values between parentheses)
            String children = inputString.substring(leftParenIdx + 1, rightParenIdx);

            // now read the name of the record (value before parentheses),
            // unless leftIndex == 0, then leave blank. create new record
            String recordName = "";
            if (leftParenIdx != 0)
                recordName = readRecordName(inputString, leftParenIdx);
            Record record = new Record(recordName);
            logger.debug("Creating Record with name: " + recordName + " and children: " + children);

            // iterate over child strings and add to record. if there is a
            // sub-record with the same name, also set the record's sub-record
            for (String child : children.split(",")) {
                Record subRecord = recordMap.get(child);
                record.addChild(child);
                if (subRecord != null) {
                    logger.debug("Adding subrecord with name: " + child);
                    record.setSubRecord(subRecord);
                }
            }

            // if there is no name this is the top record, exit on next loop
            if (recordName.equals("")) {
                logger.debug("Top record created, returning.");
                topRecord = record;
            } else {
                // otherwise, add this record to the map and remove it from the
                // input string
                logger.debug("Adding Record with name: " + recordName + " to map.");
                recordMap.put(recordName, record);

                // now remove the word in parentheses (including parentheses)
                // and continue to the next loop
                bldr = new StringBuilder();
                inputString = bldr.append(inputString.substring(0, leftParenIdx))
                        .append(inputString.substring(rightParenIdx + 1)).toString();
            }
        }

        return topRecord;
    }

    /**
     * Helper method to ensure valid input
     * 
     * @param inputString
     * @return true if inputString is valid, false otherwise
     */
    public boolean validInput(String inputString) {
        // ensure the parentheses are in matching sets
        if (!isValidParentheses(inputString))
            return false;

        // ensure that the input matches the valid format
        if (!inputString.matches(Constants.VALID_REGEX))
            return false;

        return true;
    }

    /**
     * Helper method to check for matching sets of parentheses
     * 
     * @param inputString
     * @return true if valid, false otherwise
     */
    public boolean isValidParentheses(String inputString) {
        Stack<Character> stack = new Stack<Character>();

        char c;
        for (int i = 0; i < inputString.length(); i++) {
            c = inputString.charAt(i);

            if (c == '(')
                stack.push(c);
            else if (c == ')')
                if (stack.empty())
                    return false;
                else if (stack.peek() == '(')
                    stack.pop();
                else
                    return false;
        }
        return stack.empty();
    }

    /**
     * Helper method that back-tracks through a string starting from a left
     * parenthesis and gets values until it finds a non-alphabetical character
     * 
     * @param inputString
     * @param leftIdx
     * @return a String representing the name of the record
     */
    private String readRecordName(String inputString, int leftIdx) {
        // move backward from the index until not a letter, return substring
        int rightIdx = leftIdx;
        leftIdx = rightIdx - 1;

        char c = inputString.charAt(leftIdx);
        while (Character.isLetter(c) && leftIdx > 0) {
            c = inputString.charAt(--leftIdx);
        }

        return inputString.substring(leftIdx + 1, rightIdx);
    }
}
