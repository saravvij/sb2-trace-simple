package com.rc.trace.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping("/home")
public class Home {
    private static final Logger log = LoggerFactory.getLogger(Home.class.getName());

    @GetMapping()
    public String health() {
        log.info("Received a new request");
        log.info("Completing the request");
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
