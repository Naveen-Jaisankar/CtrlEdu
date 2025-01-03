package com.ctrledu.MasterService.service;


import com.ctrledu.AuthService.entity.ClassEntity;
import com.ctrledu.AuthService.entity.ModuleEntity;
import com.ctrledu.AuthService.entity.UserEntity;
import com.ctrledu.MasterService.model.UserDTO;
import com.ctrledu.MasterService.repository.MasterClassRepository;
import com.ctrledu.MasterService.repository.MasterModuleRepository;
import com.ctrledu.MasterService.repository.MasterUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private MasterUserRepository masterUserRepository;

    @Autowired
    private MasterClassRepository masterClassRepository;

    @Autowired
    private MasterModuleRepository masterModuleRepository;

    public String getRoleFromToken(String token) {
        // Decode JWT token and extract role
        // (use a library like io.jsonwebtoken)
        return "decoded-role";
    }

    public Long getUserIdFromToken(String token) {
        // Decode JWT token and extract user ID
        return 1L; // Replace with decoded value
    }

//    public List<ClassEntity> getClassesAndModulesForOrg(Long userId) {
//        UserEntity user = userRepository.findById(userId).orElseThrow();
//        return classRepository.findByOrgId(user.getOrgId().getOrgId());
//    }

    public List<UserDTO> getModulesForTeacher(Long teacherId) {
        List<ClassEntity> classEntities = masterModuleRepository.findByTeacherId(teacherId);
        List<UserDTO> teacherDTOs = new ArrayList<>();

        for (ClassEntity classEntity : classEntities) {
            for (ModuleEntity moduleEntity : classEntity.getModules()) {
                if (moduleEntity.getTeacher().getUserId().equals(teacherId)) {
                    UserDTO userDTO = new UserDTO(
                            moduleEntity.getTeacher().getUserId(),
                            moduleEntity.getTeacher().getUserFirstName()+ moduleEntity.getTeacher().getUserLastName(),
                            classEntity.getClassId(),
                            classEntity.getClassName(),
                            moduleEntity.getModuleId(),
                            moduleEntity.getModuleName()

                    );
                    teacherDTOs.add(userDTO);
                }
            }
        }

        return teacherDTOs;

    }

    public List<UserDTO> getClassesForStudent(Long studentId) {
        Optional<UserEntity> userEntityOpt = masterUserRepository.getUserById(studentId);
        if (userEntityOpt.isEmpty()) {
            throw new EntityNotFoundException("User not found with ID: " + studentId);
        }

        String userName = userEntityOpt.get().getUserFirstName() + userEntityOpt.get().getUserLastName();
        List<ClassEntity> classEntities = masterClassRepository.findClassesForStudent(studentId);
        List<UserDTO> userDTOs = new ArrayList<>();

        for (ClassEntity classEntity : classEntities) {
            for (ModuleEntity moduleEntity : classEntity.getModules()) {
                UserDTO userDTO = new UserDTO(
                        studentId,
                        userName,
                        classEntity.getClassId(),
                        classEntity.getClassName(),
                        moduleEntity.getModuleId(),
                        moduleEntity.getModuleName()
                );
                userDTOs.add(userDTO);
            }
        }

        return userDTOs;
    }
}
