package com.ctrledu.AuthService.service;

import com.ctrledu.AuthService.dto.AddUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    private final String keycloakServerUrl = "http://localhost:8080/";
    private final String realm = "CtrlEdu";
    private final String clientId = "admin-cli";
    private final String adminUsername = "admin";
    private final String adminPassword = "admin";

    private Keycloak getKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm("master")
                .clientId(clientId)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    public void createKeycloakUser(AddUserRequest request, String uniqueCode) throws Exception {
        Keycloak keycloak = getKeycloakClient();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getEmail());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() != 201) {
            throw new Exception("Failed to create user in Keycloak: " + response.getStatusInfo().getReasonPhrase());
        }

        // Step 3: Retrieve the created user's ID
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // Step 4: Assign the role to the user
        RoleRepresentation roleRepresentation = keycloak.realm("CtrlEdu").roles().get(request.getRole()).toRepresentation();
        keycloak.realm("CtrlEdu").users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));

        // Log successful role assignment
        System.out.println("Assigned role '" + request.getRole() + "' to user with ID: " + userId);
    }

    public void updateKeycloakPassword(String email, String password) throws Exception {
        Keycloak keycloak = getKeycloakClient();

        // Search for the user by email
        List<UserRepresentation> users = keycloak.realm(realm).users().search(email);
        if (users.isEmpty()) {
            throw new Exception("User with email " + email + " not found in Keycloak");
        }

        String userId = users.get(0).getId(); // Assuming email is unique

        // Set up the new password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        // Reset password
        keycloak.realm(realm).users().get(userId).resetPassword(credential);

        System.out.println("Password updated successfully for user with email: " + email);
    }


    public String fetchKeycloakPublicKey() throws IOException {
        String url = keycloakServerUrl + "/realms/" + realm;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        Map<String, Object> realmInfo = new ObjectMapper().readValue(response, Map.class);
        return (String) realmInfo.get("public_key");
    }

    public String getClientSecret() {
        String clientSecret = null;
        try{
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakServerUrl)
                    .realm("master")
                    .clientId("admin-cli")
                    .username("admin")
                    .password("admin")
                    .build();

            // Find the client by client ID
            ClientRepresentation client = keycloak.realm(realm)
                    .clients()
                    .findByClientId("ctrledu-client")
                    .get(0);

            // Retrieve the secret
            clientSecret = keycloak.realm(realm)
                    .clients()
                    .get(client.getId())
                    .getSecret()
                    .getValue();

            return clientSecret;
        }catch (Exception e){
            System.err.println("Exception while fetching client secret from KeyCloak");
        }
        return clientSecret;
    }
}
