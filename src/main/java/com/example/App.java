package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "DevOps CI/CD Working 🚀";
    }

    @GetMapping("/calc")
    public String calc() {
        int x = 10;
        int y = 2;
        return "Result: " + (x / y);
    }
}
