package com.jobizzz.ctrledu.controller;

import com.jobizzz.ctrledu.entity.Tenant;
import com.jobizzz.ctrledu.request.SignupRequest;
import com.jobizzz.ctrledu.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class SignupController {

    @Autowired
    private TenantService tenantService;

    @PostMapping
    public ResponseEntity<Tenant> signup(@RequestBody SignupRequest signupRequest){
        Tenant tenant = tenantService.registerTenant(signupRequest.getOrganizationName());
        return ResponseEntity.ok(tenant);
    }
}
