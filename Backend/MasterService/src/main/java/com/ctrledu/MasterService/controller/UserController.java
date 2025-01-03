package com.ctrledu.MasterService.controller;

//import com.ctrledu.AuthService.entity.ClassEntity;
//import com.ctrledu.AuthService.entity.ModuleEntity;
//import com.ctrledu.AuthService.entity.UserEntity;
import com.ctrledu.AuthService.entity.UserEntity;
import com.ctrledu.AuthService.repository.UserRepository;
import com.ctrledu.MasterService.model.Thread;
import com.ctrledu.MasterService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Get Contacts or Groups based on the logged-in user
    @GetMapping("/contacts")
    public List<?> getContacts(@RequestHeader("Authorization") String token) {
        // Extract user role and ID from token
//        String role = userService.getRoleFromToken(token);
//        Long userId = userService.getUserIdFromToken(token);
        String email = Thread.getEmail();
        Optional<UserEntity> admin = userRepository.findByUserEmail(email);




        String role = "teacher";
        role = admin.get().getUserRole();
        Long userId = 9L;
        userId = admin.get().getUserId();

        switch (role.toLowerCase()) {
//            case "super-admin":
//                return userService.getClassesAndModulesForOrg(userId);
            case "teacher":
                return userService.getModulesForTeacher(userId);
            case "student":
                return userService.getClassesForStudent(userId);
            default:
                throw new IllegalArgumentException("Invalid user role");
        }
    }
}
