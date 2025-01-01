package com.jobizzz.ctrledu.controller;

import com.jobizzz.ctrledu.dto.LoginRequest;
import com.jobizzz.ctrledu.dto.RegisterRequest;
import com.jobizzz.ctrledu.dto.VerifyCodeRequest;
import com.jobizzz.ctrledu.entity.UserEntity;
import com.jobizzz.ctrledu.repository.OrganizationRepository;
import com.jobizzz.ctrledu.repository.UserRepository;
import com.jobizzz.ctrledu.response.ResponseDTO;
import com.jobizzz.ctrledu.service.AuthService;
import com.jobizzz.ctrledu.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequest request) {
        try {
            // Validate required fields
            if (request.getEmail() == null || request.getEmail().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty() || request.getFirstName() == null || request.getLastName() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST,"Email, password, first name, and last name are required.", null));
            }

            ResponseDTO response = authService.registerUser(request);

            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to register super-admin", request));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequest request) {
        try {

            if(request.getEmail() == null ||  request.getEmail().isEmpty() ||  request.getPassword() == null || request.getPassword().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST,"Email and password are required", null));
            }

            ResponseDTO response = authService.loginUser(request);

            return ResponseEntity.status(response.getStatus()).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(HttpStatus.UNAUTHORIZED,"Failed to login user", request));
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeRequest request) {
        try {
            String response = authService.verifyCode(request);
            return ResponseEntity.ok(Collections.singletonMap("message", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to verify code");
        }
    }

}






