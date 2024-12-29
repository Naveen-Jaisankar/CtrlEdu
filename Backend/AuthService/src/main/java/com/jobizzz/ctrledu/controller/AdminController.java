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
        // Check for duplicate email
        if (userRepository.findByUserEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        try {
            String email = ThreadContext.getEmail();

            Long orgId = adminService.getAdminOrgId(email);

            OrganizationEntity organization = organizationRepository.findById(orgId)
                    .orElseThrow(() -> new IllegalStateException("Organization not found for ID: " + orgId));


            //TODO Instead of generating a unique code, encrypt userid-timestamp-orgid
            // Generate unique code
            String uniqueCode = UUID.randomUUID().toString();

            // Save user in the database
            UserEntity user = new UserEntity();
            user.setUserFirstName(request.getFirstName());
            user.setUserLastName(request.getLastName());
            user.setUserEmail(request.getEmail());
            user.setUserRole(request.getRole());
            user.setUniqueCode(uniqueCode);
            user.setOrgId(organization);
            userRepository.saveAndFlush(user);


            // Create user in Keycloak
            keycloakService.createKeycloakUser(request, uniqueCode);

            return ResponseEntity.ok(Collections.singletonMap("uniqueCode", uniqueCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add user");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        try {

            String email = ThreadContext.getEmail();

            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No email found in context");
            }
            // Fetch all users belonging to the same organization
            Long orgId = adminService.getAdminOrgId(email);

            List<UserEntity> users = userRepository.findByOrgIdExcludingSuperAdmin(orgId);

            // Fetch all users from the database
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch users");
        }
    }
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        // Extract user information from SecurityContext or JWT
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", email);
        userInfo.put("role", role);
        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

            // Delete from Keycloak
            keycloakService.deleteKeycloakUser(user.getUserEmail());

            // Delete from database
            userRepository.delete(user);

            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

    @PutMapping("/edit-user/{userId}")
    public ResponseEntity<?> editUser(@PathVariable Long userId, @RequestBody AddUserRequest request) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

            // Save the old email before updating
            String oldEmail = user.getUserEmail();

            // Update in Keycloak first
            keycloakService.updateKeycloakUser(oldEmail, request);

            // Update fields in the database
            user.setUserFirstName(request.getFirstName());
            user.setUserLastName(request.getLastName());
            user.setUserEmail(request.getEmail());
            user.setUserRole(request.getRole());
            userRepository.saveAndFlush(user);

            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }

}
