package com.jobizzz.ctrledu.controller;

import com.jobizzz.ctrledu.entity.Tenant;
import com.jobizzz.ctrledu.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signup")
public class SignupController {

    @Autowired
    private TenantService tenantService;

    public ResponseEntity<Tenant> signup(@RequestParam String organizationName){
        Tenant tenant = tenantService.registerTenant(organizationName);
        return ResponseEntity.ok(tenant);
    }
}
