package com.mindsync.mindsync.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @PostMapping("/admin")
    public ResponseEntity<String> getAdminPage() {
        return ResponseEntity.ok("Admin 페이지 오세용!");
    }

    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("test");
    }
}
