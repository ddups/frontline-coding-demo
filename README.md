# frontline-coding-demo
Source control for Frontline coding challenge project

#### Table of Contents
Insert after conversion

---
# Objective
The objective of the coding challenge seemed simple at first. Split up a string and print it line by line, throw in a couple dashes and it's done. But to really achieve what the instructions asked for, a production-caliber solution, I had to re-think things a little. I set out to create a simple, easy-to-use web service that would convert the input string and display the results in an appealing way. I wanted to capture some of my various skills as well as my attention to detail and consideration for completeness.

I hope this document serves useful while evaluating my application. I have included everything needed to run and use the application and associated tests.

---
# Requirements and Assumptions
## Requirements
The project scope is minimal, but there were 5 requirements to which the project needed to adhere:
1) Utilize a language which demonstrates your skill for the position you have applied.
2) Deliver a working runnable solution and include a copy of the source code.
3) Write code typical of something we would be proud to have in Frontline software in production.
4) Independently overcome any challenges you face in delivery. 
5) If applicable, please list your assumptions.

Of these, the most considerable were #2 and #3. There was a strong focus on having a small footprint, and creating a production-caliber solution complete with unit tests, logging, and documentation.

## Assumptions
1) Whitespace within the input string should be ignored and removed before processing
2) Processing of input strings is not case-sensitive
3) Input strings must start and end with '(' and ')' respectively
4) Input strings may contain any alphanumeric characters, and must contain at least one
5) Input strings may contain ':' and a subsequent value (e.g. id:1234)...
> *Note: I added #5 to give the app some semblance of usability*

---
# Implementation
As previously stated, my objective was to create an easy-to-use web service with a self-contained application server.
This solution uses Spring Boot to minimize configuration and includes a Tomcat embedded container to make it simple to
run. The application is a RESTful service and can be queried from an internet browser. The request parameters are
included in section IV.

## First Iteration
I tried to approach the problem from an Agile perspective. Initially I wanted to come up with the minimum solution
and build an MVP. Once I could prove out the framework, I would enhance the application to meet the bonus criteria.

The initial solution, included as the "input-order" implementation, is a simple loop over the input string. It
evaluates each character, building words as it goes. When it encounters a '(' or ')' it increments/decrements the
number of dashes that are prepended to a word. Each word is added to a list in the order in which it is read.
Finally the list is printed one by one on the web page and displayed for the user.
     
## Second Iteration
For the bonus portion, I knew a simple loop would not cut it. I needed to build a data structure to capture the
hierarchy of the input string. I decided on using a recursive strategy to represent the "Records", and utilized a
TreeMap for storing the values to automatically have things in alphabetical order.

The second solution, which is the default mode of the web service, is a more complicated loop over the input string.
It begins by finding the inner-most set of parentheses and building a record of the contents. It uses the value
immediately preceding the parentheses as the name (key) and then removes the parentheses and their contents from the
string. It repeats this until there is nothing remaining in the input string, then returns a top-level record which 
maps the entire input string to a hierarchy.

I also included logging, and additional unit tests as part of the second iteration, as well as completing all
documentation. Some inspiration was taken from other sources for the CSS, HTML and jsp pages.

---
# Testing
## Considerations
I used JUnit to write the unit tests for the application and used a test-driven strategy. I started out with the 
original example input string, and built up a small test suite to ensure the validation was working properly as I 
implemented the solution. I tried to capture a wide breadth of test cases across the validation and processing of
the input, as well as the exception handling.
    
## Running the tests
To run the tests in Eclipse:
1) Right click on the "ConversionTests.java" class
2) Click "Run As -> JUnit Test"

---
# Using the Application

---
# Future Considerations
