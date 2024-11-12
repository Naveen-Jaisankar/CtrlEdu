package com.jobizzz.ctrledu.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getDashboard() {
        return "Welcome to the Organization Dashboard!";
    }
}
