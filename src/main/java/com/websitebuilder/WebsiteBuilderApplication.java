package com.websitebuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsiteBuilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsiteBuilderApplication.class, args);
        System.out.println("==========================================================");
        System.out.println(" Webloom - Industry-Grade Website Builder is RUNNING!");
        System.out.println(" Open in Browser at:     http://localhost:8080");
        System.out.println(" Visual Customizer at:   http://localhost:8080/editor.html");
        System.out.println(" H2 Database Console at: http://localhost:8080/h2-console");
        System.out.println("==========================================================");
    }
}
