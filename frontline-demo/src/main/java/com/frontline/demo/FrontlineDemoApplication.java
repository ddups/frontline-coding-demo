package com.frontline.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * FrontlineDemoApplication.java
 * 
 * @author Derek Dupuis
 * 
 *         This is the main class of the application. It uses Spring Boot to
 *         start a web service running on an embedded Tomcat server
 */
@SpringBootApplication
public class FrontlineDemoApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FrontlineDemoApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FrontlineDemoApplication.class, args);
    }

}
