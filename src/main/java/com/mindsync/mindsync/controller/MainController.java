package com.mindsync.mindsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    // 테스트 API
    @GetMapping("/")
    public String mainP() {
        return "main Controller";
    }
}
