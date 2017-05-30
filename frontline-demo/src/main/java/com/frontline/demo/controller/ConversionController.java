package com.frontline.demo.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.frontline.demo.converter.InputConverter;
import com.frontline.demo.domain.Record;

/**
 * ConversionController.java
 * 
 * @author Derek Dupuis
 *
 *         This is the controller responsible for mapping the request URL.
 */
@Controller
public class ConversionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private InputConverter inputConverter = new InputConverter();

    /**
     * This method is mapped to the "/conversion" pattern. It calls a conversion
     * method depending on the input parameters. If there is an invalid input
     * string, the method returns an error page, otherwise it returns a web page
     * with the result of the string conversion.
     * 
     * @param model
     * @param sortOrder
     * @param inputString
     * @return a view presenting the result of the string conversion, or an
     *         error page
     */
    @RequestMapping("/conversion" )
    public String conversion(Model model,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "alpha") String sortOrder,
            @RequestParam(value = "inputString", required = false, 
                defaultValue = "(id,created,employee(id,firstname,employeeType(id),lastname),location)") String inputString) {
        long startTime = System.currentTimeMillis();
        
        logger.info("Request received. Input string: " + inputString + ", Sort order: " + sortOrder);
        model.addAttribute("input", inputString);

        // convert the input and get the hierarchy of the records
        Record top = new Record("");
        ArrayList<String> convertedString = new ArrayList<>();
        try {
            if (sortOrder.toLowerCase().equals("input")) {
                convertedString = inputConverter.convertInput(inputString);
            } else if (sortOrder.toLowerCase().equals("alpha")) {
                top = inputConverter.convertInputAlpha(inputString);
                convertedString = top.getHierarchy(0);
            }
        } catch (InvalidInputException e) {
            logger.error("An exception occurred:", e);
            model.addAttribute("status", "error");
            model.addAttribute("message", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Input conversion FAILED. Duration: " + duration + " ms.");
            return "conversion";
        }        

        // set the converted string in the model and return
        model.addAttribute("status", "success");
        model.addAttribute("convertedString", convertedString);
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Input conversion SUCCESSFUL. Converted string: " + convertedString.toString() );
        logger.info("Input conversion completed in " + duration + " ms.");
        return "conversion";
    }
}
