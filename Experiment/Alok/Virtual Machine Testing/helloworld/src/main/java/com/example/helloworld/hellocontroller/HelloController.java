package com.example.helloworld.hellocontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String greeting() {
        return "Listen here, you have till the count of 10 to leave or else...";
    }
}
