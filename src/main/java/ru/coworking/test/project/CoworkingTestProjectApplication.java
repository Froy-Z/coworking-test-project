package ru.coworking.test.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoworkingTestProjectApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(CoworkingTestProjectApplication.class, args);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
