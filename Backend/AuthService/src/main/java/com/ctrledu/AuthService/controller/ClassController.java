package com.ctrledu.AuthService.controller;

import com.ctrledu.AuthService.dto.ClassRequest;
import com.ctrledu.AuthService.dto.ThreadContext;
import com.ctrledu.AuthService.response.ClassResponse;
import com.ctrledu.AuthService.service.AdminService;
import com.ctrledu.AuthService.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ClassController {

    @Autowired
    private ClassService classService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/classes")
    public List<ClassResponse> getClasses() {
        String email = ThreadContext.getEmail(); // Retrieve the email from thread context
        Long orgId = adminService.getAdminOrgId(email); // Fetch the orgId using AdminService
        return classService.getClassesByOrgId(orgId);
    }

    @PostMapping("/add-class")
    public ResponseEntity<?> addClass(@RequestBody ClassRequest request) {
        String email = ThreadContext.getEmail(); // Retrieve the email from thread context
        Long orgId = adminService.getAdminOrgId(email); // Fetch the orgId using AdminService
        classService.addClass(request, orgId);
        return ResponseEntity.ok("Class added successfully!");
    }
}

