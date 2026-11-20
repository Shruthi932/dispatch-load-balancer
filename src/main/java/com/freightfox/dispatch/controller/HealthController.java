package com.freightfox.dispatch.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(value = "/", produces = "text/html")
    public String home() {
        return "<h2>Dispatch Load Balancer API is running successfully!</h2>";
    }
}