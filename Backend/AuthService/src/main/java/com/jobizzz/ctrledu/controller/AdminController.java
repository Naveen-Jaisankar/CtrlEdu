package com.jobizzz.ctrledu.controller;

import com.jobizzz.ctrledu.dto.AddUserRequest;
import com.jobizzz.ctrledu.dto.ThreadContext;
import com.jobizzz.ctrledu.entity.OrganizationEntity;
import com.jobizzz.ctrledu.entity.UserEntity;
import com.jobizzz.ctrledu.repository.OrganizationRepository;
import com.jobizzz.ctrledu.repository.UserRepository;
import com.jobizzz.ctrledu.service.AdminService;
import com.jobizzz.ctrledu.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody AddUserRequest request) {
        try {
            Map<String, String> response = adminService.addUser(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add user");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        try {
            List<UserEntity> users = adminService.getUsers();
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch users");
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

    @PutMapping("/edit-user/{userId}")
    public ResponseEntity<?> editUser(@PathVariable Long userId, @RequestBody AddUserRequest request) {
        try {
            adminService.editUser(userId, request);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }

}
