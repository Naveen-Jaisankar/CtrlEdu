package com.ctrledu.AuthService.controller;

import com.ctrledu.AuthService.dto.AddUserRequest;
import com.ctrledu.AuthService.entity.UserEntity;
import com.ctrledu.AuthService.repository.UserRepository;
import com.ctrledu.AuthService.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;




@RestController
@RequestMapping("/api/admin")
public class AdminController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeycloakService keycloakService;

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody AddUserRequest request) {
        // Check for duplicate email
        if (userRepository.findByUserEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        try {
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
            // Fetch all users from the database
            return ResponseEntity.ok(userRepository.findAll());
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

}
