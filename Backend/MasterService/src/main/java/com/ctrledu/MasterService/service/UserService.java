package com.ctrledu.MasterService.service;


import com.ctrledu.AuthService.entity.ClassEntity;
import com.ctrledu.AuthService.entity.ModuleEntity;
import com.ctrledu.AuthService.entity.UserEntity;
import com.ctrledu.MasterService.model.StudentDTO;
import com.ctrledu.MasterService.model.TeacherDTO;
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

    public List<TeacherDTO> getModulesForTeacher(Long teacherId) {
        List<ClassEntity> classEntities = masterModuleRepository.findByTeacherId(teacherId);
        List<TeacherDTO> teacherDTOs = new ArrayList<>();

        for (ClassEntity classEntity : classEntities) {
            for (ModuleEntity moduleEntity : classEntity.getModules()) {
                if (moduleEntity.getTeacher().getUserId().equals(teacherId)) {
                    TeacherDTO teacherDTO = new TeacherDTO(
                            moduleEntity.getTeacher().getUserId(),
                            moduleEntity.getTeacher().getUserFirstName()+ moduleEntity.getTeacher().getUserLastName(),
                            moduleEntity.getModuleId(),
                            moduleEntity.getModuleCode(),
                            moduleEntity.getModuleName(),
                            classEntity.getClassId(),
                            classEntity.getClassName()
                    );
                    teacherDTOs.add(teacherDTO);
                }
            }
        }

        return teacherDTOs;

    }

    public List<StudentDTO> getClassesForStudent(Long studentId) {
        Optional<UserEntity> userEntityOpt = masterUserRepository.getUserById(studentId);
        if (userEntityOpt.isEmpty()) {
            throw new EntityNotFoundException("User not found with ID: " + studentId);
        }

        String userName = userEntityOpt.get().getUserFirstName() + userEntityOpt.get().getUserLastName();
        List<ClassEntity> classEntities = masterClassRepository.findClassesForStudent(studentId);
        List<StudentDTO> studentDTOs = new ArrayList<>();

        for (ClassEntity classEntity : classEntities) {
            for (ModuleEntity moduleEntity : classEntity.getModules()) {
                StudentDTO studentDTO = new StudentDTO(
                        studentId,
                        userName,
                        classEntity.getClassId(),
                        classEntity.getClassName(),
                        moduleEntity.getModuleId(),
                        moduleEntity.getModuleName()
                );
                studentDTOs.add(studentDTO);
            }
        }

        return studentDTOs;
    }
}
