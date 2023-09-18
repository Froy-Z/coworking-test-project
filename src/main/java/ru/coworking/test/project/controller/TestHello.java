package ru.coworking.test.project.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.coworking.test.project.dto.AskDto;


@RestController
public class TestHello {

    @GetMapping("/hello")
    public AskDto sayHello() {
        return AskDto.builder()
                .answer("IT'S ALIVE! WOW!'")
                .build();
    }
}
