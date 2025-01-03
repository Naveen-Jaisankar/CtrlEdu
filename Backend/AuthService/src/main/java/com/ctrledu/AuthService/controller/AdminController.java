package com.ctrledu.AuthService.controller;

import com.ctrledu.AuthService.dto.AddUserRequest;
import com.ctrledu.AuthService.dto.ThreadContext;
import com.ctrledu.AuthService.entity.OrganizationEntity;
import com.ctrledu.AuthService.entity.UserEntity;
import com.ctrledu.AuthService.repository.OrganizationRepository;
import com.ctrledu.AuthService.repository.UserRepository;
import com.ctrledu.AuthService.service.AdminService;
import com.ctrledu.AuthService.service.KeycloakService;
import com.ctrledu.CommonService.utilities.ServiceCommunicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceCommunicator serviceCommunicator;

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
            String uniquCode = response.get("uniqueCode");
            Map<String, Object> emailPayload = Map.of(
                    "to", request.getEmail(),
                    "subject", "Welcome to CtrlEdu!",
                    "firstName", request.getFirstName(),
                    "lastName", request.getLastName(),
                    "uniqueCode",uniquCode
            );
            serviceCommunicator.sendPostRequest(
                    "http://NotificationService", // Use http://, not lb://
                    "/api/notify/email/send",
                    emailPayload,
                    String.class
            ).subscribe(res -> {
                System.out.println("Notification Sent: " + res);
            }, error -> {
                System.err.println("Error Sending Notification: " + error.getMessage());
            });

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
