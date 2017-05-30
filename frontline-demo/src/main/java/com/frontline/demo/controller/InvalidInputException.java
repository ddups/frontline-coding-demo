package com.frontline.demo.controller;

/**
 * InvalidInputException.java
 * 
 * @author Derek Dupuis
 *
 *         This class represents an exception thrown when an invalid input
 *         string is passed
 */
public class InvalidInputException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidInputException() {
        super("Invalid input received, could not convert.");
    }
}
