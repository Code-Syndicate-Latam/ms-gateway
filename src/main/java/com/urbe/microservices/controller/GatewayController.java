package com.urbe.microservices.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class GatewayController {
    @GetMapping("/authorized")
    public Map<String,String> authorized(@RequestParam String code) {
        return Collections.singletonMap("code: ",code);
    }
}
