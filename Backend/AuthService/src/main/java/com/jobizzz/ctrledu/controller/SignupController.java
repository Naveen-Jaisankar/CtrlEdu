package com.jobizzz.ctrledu.controller;

import com.jobizzz.ctrledu.entity.OrganizationEntity;
import com.jobizzz.ctrledu.request.SignupRequest;
import com.jobizzz.ctrledu.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class SignupController {

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<OrganizationEntity> signup(@RequestBody SignupRequest signupRequest){
        OrganizationEntity organizationEntity = authService.register(signupRequest);
        return ResponseEntity.ok(organizationEntity);
    }
}

