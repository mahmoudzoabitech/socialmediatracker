package com.tsofnsalesforce.LoginandRegistration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class demoController {

    @GetMapping("/")
    public String checkPermission(){
        return "all users have the permission to get this method";
    }
}
