package com.jobizzz.ctrledu.controller;

import com.jobizzz.ctrledu.request.SignupRequest;
import com.jobizzz.ctrledu.dto.VerifyCodeRequest;
import com.jobizzz.ctrledu.entity.UserEntity;
import com.jobizzz.ctrledu.repository.UserRepository;
import com.jobizzz.ctrledu.service.KeycloakService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeycloakService keycloakService;

    private final String keycloakServerUrl = "http://localhost:8080/";
    private final String realm = "CtrlEdu";
    private final String clientId = "ctrledu-client";
    private final String clientSecret = "NqTNda6RzhlqnK7W9QSg66tdheVteaEi";
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignupRequest request) {
        try {
            // Validate required fields
            if (request.getEmail() == null || request.getEmail().isEmpty() ||
                    request.getPassword() == null || request.getPassword().isEmpty() ||
                    request.getFirstName() == null || request.getLastName() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email, password, first name, and last name are required.");
            }

            // Set the role to super-admin
            String role = "super-admin";

            // Save the organization in the database
            UserEntity organization = new UserEntity();
            organization.setOrgId(1l);
            organization.setUserEmail(request.getEmail());
            organization.setUserRole(role); // Always set to super-admin
            userRepository.save(organization);

            // Register the organization in Keycloak
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakServerUrl)
                    .realm("master") // Use "master" realm for admin access
                    .clientId("admin-cli")
                    .username("admin") // Admin username in Keycloak
                    .password("admin") // Admin password in Keycloak
                    .build();

            // Create Keycloak user details
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName()); // Set first name
            user.setLastName(request.getLastName());   // Set last name
            user.setEnabled(true);
            user.setEmailVerified(true);

            // Set up the password
            CredentialRepresentation password = new CredentialRepresentation();
            password.setTemporary(false);
            password.setType(CredentialRepresentation.PASSWORD);
            password.setValue(request.getPassword());
            user.setCredentials(Collections.singletonList(password));

            // Create the user in Keycloak
            Response response = keycloak.realm("CtrlEdu").users().create(user);
            if (response.getStatus() != 201) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create super-admin user in Keycloak");
            }

            // Retrieve the created user's ID
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            // Assign the super-admin role to the user
            RoleRepresentation roleRepresentation = keycloak.realm("CtrlEdu").roles().get(role).toRepresentation();
            keycloak.realm("CtrlEdu").users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));

            return ResponseEntity.ok("Super-admin registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register super-admin");
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

            // Retrieve the public key from Keycloak
            String publicKeyPem = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArBh0pA+aEkDNSz8S/VzsyLND6H8nQrely5SJXp6Ge0AM4NHNWHcits4e6ZMBf5QQvsGcQFkrDdEDzUu+hq0Md2H/KwhbDA6Gwtkxy+8bltDdNgv/s204vqVpXTyVd004bQBPQcilHusoPs1uECLcqAx3CPO8o5MYHBhwEBfOFyrEpbiNGglzXlMzDZCFEMDv1G9PEssfWOm6AfFpKwmaCSis20JxmdAwHg4R+IkCbQWSRZnjtdMDkiWKdNFcUsLCjmvxZYdlM9wFvWVzXosGLJ6wDv33T5nZQIeHGoI9M5zdqxhnYVRYiFj+nNWOpSCtTieHdqKJzlNOtOk8tCTijQIDAQAB";

            // Remove PEM header and footer, and decode the key
            publicKeyPem = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decodedKey = Base64.getDecoder().decode(publicKeyPem);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));

            // Parse and validate the JWT using the public key
            String accessToken = tokenResponse.getToken();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            // Extract roles from the token
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");

// Ensure we get the most relevant role
            String role = null;
            if (roles.contains("super-admin")) {
                role = "super-admin";
            } else if (roles.contains("teacher")) {
                role = "teacher";
            } else if (roles.contains("student")) {
                role = "student";
            }

            if (role == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Role not assigned");
            }


            // Add redirect path based on role
            String redirectPath;
            switch (role) {
                case "super-admin":
                    redirectPath = "/admin-dashboard";
                    break;
                case "teacher":
                    redirectPath = "/teacher-dashboard";
                    break;
                case "student":
                    redirectPath = "/student-dashboard";
                    break;
                default:
                    redirectPath = "/";
            }

            // Include roles, tokens, and redirect path in the response
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", tokenResponse.getToken());
            response.put("refreshToken", tokenResponse.getRefreshToken());
            response.put("role", role);
            response.put("redirectPath", redirectPath);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }



    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeRequest request) {
        try {
            // Step 1: Validate the unique code
            Optional<UserEntity> userOptional = userRepository.findByUserUniqueCode(request.getCode());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid code");
            }

            UserEntity user = userOptional.get();

            // Step 2: Update the Keycloak password if email and password are provided
            if (request.getEmail() != null && request.getPassword() != null) {
                user.setUserEmail(request.getEmail());
                userRepository.save(user); // Update email in DB

                keycloakService.updateKeycloakPassword(request.getEmail(), request.getPassword());
                return ResponseEntity.ok("Password successfully set");
            }

            // Step 3: Return success if the unique code is valid (without email/password)
            return ResponseEntity.ok(Collections.singletonMap("message", "Code validated. Please set your email and password."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to verify code");
        }
    }

}


