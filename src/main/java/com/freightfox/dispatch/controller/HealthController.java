package com.freightfox.dispatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "message", "Dispatch Load Balancer API is running",
                "status", "success"
        );
    }
}