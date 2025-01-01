package com.jobizzz.ctrledu.service;

import com.jobizzz.ctrledu.dto.AddUserRequest;
import com.jobizzz.ctrledu.dto.ThreadContext;
import com.jobizzz.ctrledu.entity.OrganizationEntity;
import com.jobizzz.ctrledu.entity.UserEntity;
import com.jobizzz.ctrledu.repository.OrganizationRepository;
import com.jobizzz.ctrledu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private KeycloakService keycloakService;


    public Long getAdminOrgId(String email){
        Long orgId = null;
        try{
            UserEntity admin = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new IllegalStateException("Admin not found for email: " + email));

            // Fetch all users belonging to the same organization
            orgId = admin.getOrgId().getOrgId();
        }catch (Exception e){
            System.err.println("Exception while fetching Admin user id from the email for user : " +  email);
        }
        return orgId;
    }
    public Map<String, String> addUser(AddUserRequest request) {
        if (userRepository.findByUserEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        String email = ThreadContext.getEmail();
        Long orgId = getAdminOrgId(email);

        OrganizationEntity organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalStateException("Organization not found for ID: " + orgId));

        String uniqueCode = UUID.randomUUID().toString();

        UserEntity user = new UserEntity();
        user.setUserFirstName(request.getFirstName());
        user.setUserLastName(request.getLastName());
        user.setUserEmail(request.getEmail());
        user.setUserRole(request.getRole());
        user.setUniqueCode(uniqueCode);
        user.setOrgId(organization);
        userRepository.saveAndFlush(user);

        try {
            keycloakService.createKeycloakUser(request, uniqueCode);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create Keycloak user: " + e.getMessage());
        }

        return Collections.singletonMap("uniqueCode", uniqueCode);
    }
    public List<UserEntity> getUsers() {
        String email = ThreadContext.getEmail();

        if (email == null) {
            throw new IllegalArgumentException("No email found in context");
        }

        // Get the organization ID for the admin
        Long orgId = getAdminOrgId(email);

        // Fetch all users belonging to the same organization
        return userRepository.findByOrgIdExcludingSuperAdmin(orgId);
    }
    public void deleteUser(Long userId) {
        // Fetch user by ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // Delete the user in Keycloak
        try {
            // Delete the user in Keycloak
            keycloakService.deleteKeycloakUser(user.getUserEmail());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to delete user in Keycloak: " + e.getMessage());
        }

        // Delete the user in the database
        userRepository.delete(user);
    }
    public void editUser(Long userId, AddUserRequest request) throws Exception {
        // Fetch the user from the database
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // Save the old email before updating
        String oldEmail = user.getUserEmail();

        // Update in Keycloak
        keycloakService.updateKeycloakUser(oldEmail, request);

        // Update fields in the database
        user.setUserFirstName(request.getFirstName());
        user.setUserLastName(request.getLastName());
        user.setUserEmail(request.getEmail());
        user.setUserRole(request.getRole());

        // Save the updated user
        userRepository.saveAndFlush(user);
    }
}



