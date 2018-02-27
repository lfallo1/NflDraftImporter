package com.combine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    /**
     * default route
     *
     * @return
     */
    @RequestMapping("/")
    public String getIndex() {
        return "index.html";
    }

}
