package com.jobizzz.ctrledu.controller;

import com.jobizzz.ctrledu.dto.RegisterRequest;
import com.jobizzz.ctrledu.entity.Organization;
import com.jobizzz.ctrledu.repository.OrganizationRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OrganizationRepository organizationRepository;

    private final String keycloakServerUrl = "http://localhost:8080/";
    private final String realm = "CtrlEdu";
    private final String clientId = "ctrledu-client";
    private final String clientSecret = "NqTNda6RzhlqnK7W9QSg66tdheVteaEi"; // Replace with actual secret
    @CrossOrigin(origins = "http://localhost:5173/**")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if ("student".equals(request.getRole()) || "teacher".equals(request.getRole())) {
            if (!isValidInvitationCode(request.getInvitationCode())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid invitation code");
            }
        }

        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setEmail(request.getEmail());
        organization.setRole(request.getRole());
        organizationRepository.save(organization);

        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakServerUrl)
                    .realm("master") // Use "master" realm for admin access
                    .clientId("admin-cli")
                    .username("admin") // Admin username in Keycloak
                    .password("admin") // Admin password in Keycloak
                    .build();

            // Set up the user details for Keycloak
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setRealmRoles(Collections.singletonList(request.getRole())); // Assign role

            // Set up the password
            CredentialRepresentation password = new CredentialRepresentation();
            password.setTemporary(false);
            password.setType(CredentialRepresentation.PASSWORD);
            password.setValue(request.getPassword());
            user.setCredentials(Collections.singletonList(password));

            // Create the user in Keycloak
            Response response = keycloak.realm("CtrlEdu").users().create(user);
            if (response.getStatus() != 201) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user in Keycloak");
            }

            return ResponseEntity.ok("Registration successful!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakServerUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(email)
                    .password(password)
                    .grantType("password")
                    .build();

            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", tokenResponse.getToken());
            tokens.put("refreshToken", tokenResponse.getRefreshToken());

            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    private boolean isValidInvitationCode(String invitationCode) {
        return "VALID_CODE".equals(invitationCode); // Placeholder, replace with actual logic
    }
}
