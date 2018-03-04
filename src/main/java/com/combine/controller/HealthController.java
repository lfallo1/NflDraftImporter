package com.combine.controller;

import com.combine.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/health")
public class HealthController {

    @Autowired
    private ParserService parserService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Void> checkHealth() {
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<Void> checkEndpoint() {
//        this.parserService.loadColleges();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
