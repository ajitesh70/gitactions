package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class DemoController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Docker Demo";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is Healthy";
    }

    @GetMapping("/version")
    public String version() {
        return "Version 1.0";
    }

    @GetMapping("/time")
    public String time() {
        return LocalDateTime.now().toString();
    }
}
