package com.jobizzz.ctrledu.controller;

import com.jobizzz.ctrledu.dto.ModuleRequest;
import com.jobizzz.ctrledu.dto.ThreadContext;
import com.jobizzz.ctrledu.entity.ModuleEntity;
import com.jobizzz.ctrledu.repository.ModuleRepository;
import com.jobizzz.ctrledu.repository.OrganizationRepository;
import com.jobizzz.ctrledu.response.ModuleResponse;
import com.jobizzz.ctrledu.response.TeacherResponse;
import com.jobizzz.ctrledu.service.AdminService;
import com.jobizzz.ctrledu.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ModuleRepository moduleRepository;
    @GetMapping("/modules")
    public ResponseEntity<List<ModuleResponse>> getModulesForOrg() {
        String email = ThreadContext.getEmail(); // Retrieve the email from thread context
        Long orgId = adminService.getAdminOrgId(email); // Fetch the orgId using AdminService

        // Fetch modules for the organization
        List<ModuleResponse> modules = moduleService.getModulesByOrgId(orgId);

        return ResponseEntity.ok(modules);
    }
    @GetMapping("/available-teachers")
    public ResponseEntity<List<TeacherResponse>> getAvailableTeachersForAdd() {
        String email = ThreadContext.getEmail();
        Long orgId = adminService.getAdminOrgId(email);
        List<TeacherResponse> teachers = moduleService.getAvailableTeachersForAdd(orgId);
        return ResponseEntity.ok(teachers);
    }
    @GetMapping("/all-teachers")
    public ResponseEntity<List<TeacherResponse>> getAllTeachersForEdit(
            @RequestParam Long currentTeacherId) {
        String email = ThreadContext.getEmail(); // Fetch email from thread-local context
        Long orgId = adminService.getAdminOrgId(email); // Fetch orgId based on email
        List<TeacherResponse> teachers = moduleService.getAllTeachersForEdit(orgId, currentTeacherId);
        return ResponseEntity.ok(teachers);
    }
    @PostMapping("/add-module")
    public ResponseEntity<String> addModule(@RequestBody ModuleRequest request) {
        moduleService.addModule(request);
        return ResponseEntity.ok("Module added successfully.");
    }
    @PutMapping("/edit-module/{moduleId}")
    public ResponseEntity<String> editModule(@PathVariable Long moduleId, @RequestBody ModuleRequest request) {
        moduleService.updateModule(moduleId, request);
        return ResponseEntity.ok("Module updated successfully.");
    }
    @DeleteMapping("/delete-module/{moduleId}")
    public ResponseEntity<String> deleteModule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        return ResponseEntity.ok("Module deleted successfully.");
    }
}
