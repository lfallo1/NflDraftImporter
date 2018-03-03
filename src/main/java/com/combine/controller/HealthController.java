package com.combine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/health")
public class HealthController {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Void> checkHealth() {
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
