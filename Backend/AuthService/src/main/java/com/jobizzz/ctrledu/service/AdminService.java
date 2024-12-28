package com.jobizzz.ctrledu.service;

import com.jobizzz.ctrledu.entity.UserEntity;
import com.jobizzz.ctrledu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;


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

}
