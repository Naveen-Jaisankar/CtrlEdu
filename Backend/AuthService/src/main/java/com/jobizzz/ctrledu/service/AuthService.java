package com.jobizzz.ctrledu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobizzz.ctrledu.dto.LoginRequest;
import com.jobizzz.ctrledu.dto.RegisterRequest;
import com.jobizzz.ctrledu.entity.OrganizationEntity;
import com.jobizzz.ctrledu.entity.UserEntity;
import com.jobizzz.ctrledu.repository.OrganizationReporsitory;
import com.jobizzz.ctrledu.repository.UserRepository;
import com.jobizzz.ctrledu.response.LoginResponse;
import com.jobizzz.ctrledu.response.ResponseDTO;
import com.jobizzz.ctrledu.response.SignupResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationReporsitory organizationReporsitory;

    @Autowired
    private KeycloakService keycloakService;

    private final String keycloakServerUrl = "http://localhost:8080/";
    private final String realm = "CtrlEdu";
    private final String clientId = "ctrledu-client";

    public ResponseDTO registerUser(RegisterRequest registerRequest) {

        ResponseDTO registerResponse = new ResponseDTO();

        try{
            // Set the role to super-admin
            String role = "super-admin";

            // Create and save organization
            SignupResponse signupResponse = addRecordsToUserAndOrganization(registerRequest);


            // Integrate with Keycloak
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakServerUrl)
                    .realm("master") // Use "master" realm for admin access
                    .clientId("admin-cli")
                    .username("admin")
                    .password("admin")
                    .build();

            UserRepresentation user = new UserRepresentation();
            user.setUsername(registerRequest.getEmail());
            user.setEmail(registerRequest.getEmail());
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEnabled(true);
            user.setEmailVerified(true);

            CredentialRepresentation password = new CredentialRepresentation();
            password.setTemporary(false);
            password.setType(CredentialRepresentation.PASSWORD);
            password.setValue(registerRequest.getPassword());
            user.setCredentials(Collections.singletonList(password));

            Response response = keycloak.realm("CtrlEdu").users().create(user);

            if (response.getStatus() != 201) {
                registerResponse = new ResponseDTO(HttpStatus.BAD_REQUEST,"Failed to create user in Keycloak. Status Code: " + response.getStatus(),null);
            }

            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            RoleRepresentation roleRepresentation = keycloak.realm("CtrlEdu").roles().get(role).toRepresentation();
            keycloak.realm("CtrlEdu").users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));

            registerResponse = new ResponseDTO(HttpStatus.OK,"Super-admin registered successfully!",signupResponse);
        }catch(Exception e){
            System.err.println("Exception while register super admin");
        }

        return registerResponse;
    }


    public ResponseDTO loginUser(LoginRequest loginRequest){
        ResponseDTO response = new ResponseDTO();
        try{

            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            String clientSecret = keycloakService.getClientSecret();


            // Authenticate with Keycloak and get the access token
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

            // Retrieve the public key dynamically from Keycloak
            String publicKeyPem = keycloakService.fetchKeycloakPublicKey();
            PublicKey publicKey = getPublicKeyFromPEM(publicKeyPem);

            // Parse and validate the JWT using the public key
            String accessToken = tokenResponse.getToken();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            // Extract roles from the token
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                response = new ResponseDTO(HttpStatus.UNAUTHORIZED,"User does not have assigned roles.",null);
            }

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");

            // Determine the role
            String role = getRole(roles);
            if (role == null) {
                response = new ResponseDTO(HttpStatus.UNAUTHORIZED,"Role not assigned",null);
            }


            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAccessToken(tokenResponse.getToken());
            loginResponse.setRefreshToken(tokenResponse.getRefreshToken());
            loginResponse.setRole(role);

            return new ResponseDTO(HttpStatus.OK,"User Logged in successfully", loginResponse);

        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseDTO(HttpStatus.UNAUTHORIZED,"Invalid credentials or token validation failed.",null);
        }
        return response;
    }

    // Convert PEM public key to PublicKey object
    private PublicKey getPublicKeyFromPEM(String publicKeyPem) throws Exception {
        publicKeyPem = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyPem);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
    }

    // Determine the highest priority role
    private String getRole(List<String> roles) {
        if (roles.contains("super-admin")) {
            return "super-admin";
        } else if (roles.contains("teacher")) {
            return "teacher";
        } else if (roles.contains("student")) {
            return "student";
        }
        return null; // No recognized role found
    }

    
    public SignupResponse addRecordsToUserAndOrganization(RegisterRequest registerRequest){
        SignupResponse signupResponse = new SignupResponse();
        try{

            String role = "super-admin";

            OrganizationEntity organizationEntity = new OrganizationEntity();
            organizationEntity.setOrgName(registerRequest.getOrganizationName());
            organizationEntity = organizationReporsitory.save(organizationEntity);

            UserEntity userEntity = new UserEntity();
            userEntity.setOrgId(organizationEntity);
            userEntity.setUserFirstName(registerRequest.getFirstName());
            userEntity.setUserLastName(registerRequest.getLastName());
            userEntity.setUserEmail(registerRequest.getEmail());
            userEntity.setUserRole(role); // Always set to super-admin
            userEntity = userRepository.save(userEntity);

            signupResponse.setUserId(userEntity.getUserId());
            signupResponse.setOrgId(organizationEntity.getOrgId());
            signupResponse.setUserEmail(userEntity.getUserEmail());
            signupResponse.setUserFirstName(userEntity.getUserFirstName());
            signupResponse.setUserLastName(userEntity.getUserLastName());
            // Update organization's super-admin ID
            organizationEntity.setSuperAdminId(userEntity.getUserId());
            organizationReporsitory.save(organizationEntity);
        }catch (Exception e){
            System.err.println("Exception while adding records to user and organization table, Exception : " + e);
        }
        return signupResponse;
    }



}
