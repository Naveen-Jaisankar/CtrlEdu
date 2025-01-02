package com.ctrledu.AuthService.service;

import com.ctrledu.AuthService.dto.ModuleRequest;
import com.ctrledu.AuthService.dto.ThreadContext;
import com.ctrledu.AuthService.entity.ModuleEntity;
import com.ctrledu.AuthService.entity.OrganizationEntity;
import com.ctrledu.AuthService.entity.UserEntity;
import com.ctrledu.AuthService.repository.ModuleRepository;
import com.ctrledu.AuthService.repository.OrganizationRepository;
import com.ctrledu.AuthService.repository.UserRepository;
import com.ctrledu.AuthService.response.ModuleResponse;
import com.ctrledu.AuthService.response.TeacherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private AdminService adminService;

    public List<TeacherResponse> getAvailableTeachersForAdd(Long orgId) {
        OrganizationEntity orgEntity = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found for ID: " + orgId));

        return userRepository.findAvailableTeachersByOrgEntity(orgEntity).stream()
                .map(teacher -> new TeacherResponse(
                        teacher.getUserId(),
                        teacher.getUserFirstName(),
                        teacher.getUserLastName(),
                        isTeacherAssignedToModule(teacher.getUserId())
                ))
                .collect(Collectors.toList());
    }
    public List<TeacherResponse> getAllTeachersForEdit(Long orgId, Long currentTeacherId) {
        OrganizationEntity orgEntity = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found for ID: " + orgId));

        List<UserEntity> teachers = userRepository.findAllTeachersForEdit(orgEntity, currentTeacherId);

        return teachers.stream()
                .map(teacher -> new TeacherResponse(
                        teacher.getUserId(),
                        teacher.getUserFirstName(),
                        teacher.getUserLastName(),
                        isTeacherAssignedToModule(teacher.getUserId())
                ))
                .collect(Collectors.toList());
    }
    public List<ModuleResponse> getModulesByOrgId(Long orgId) {
        List<ModuleEntity> modules = moduleRepository.findByOrgId(orgId);
        if (modules.isEmpty()) {
            System.out.println("No modules found for orgId: " + orgId);
        }
        return modules.stream()
                .map(module -> new ModuleResponse(
                        module.getModuleId(), // Long: moduleId
                        module.getModuleCode(), // String: moduleCode
                        module.getModuleName(), // String: moduleName
                        module.getTeacher().getUserFirstName() + " " + module.getTeacher().getUserLastName(), // String: teacherName
                        module.getTeacher().getUserId() // Long: teacherId
                ))
                .collect(Collectors.toList());
    }
    private boolean isTeacherAssignedToModule(Long teacherId) {
        return moduleRepository.isTeacherAssigned(teacherId);
    }
    public void addModule(ModuleRequest request) {
        // Retrieve the logged-in user's organization ID
        String email = ThreadContext.getEmail(); // Fetch the logged-in user's email
        Long orgId = adminService.getAdminOrgId(email); // Fetch the orgId using the adminService

        // Fetch the organization entity
        OrganizationEntity organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalStateException("Organization not found for ID: " + orgId));

        // Fetch the teacher entity
        UserEntity teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found."));

        // Check if the teacher is already assigned to another module
        if (moduleRepository.existsByTeacherUserId(request.getTeacherId())) {
            throw new IllegalArgumentException("Teacher is already assigned to another module.");
        }

        // Create and save the module entity
        ModuleEntity module = new ModuleEntity();
        module.setModuleCode(request.getModuleCode());
        module.setModuleName(request.getModuleName());
        module.setTeacher(teacher);
        module.setOrgId(organization); // Set the organization entity

        moduleRepository.save(module);
    }
    public void updateModule(Long moduleId, ModuleRequest request) {
        ModuleEntity module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module not found."));
        UserEntity teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found."));
        if (moduleRepository.isTeacherAssignedToAnotherModule(request.getTeacherId(), moduleId)) {
            throw new IllegalArgumentException("Teacher is already assigned to another module.");
        }
        module.setModuleCode(request.getModuleCode());
        module.setModuleName(request.getModuleName());
        module.setTeacher(teacher);
        moduleRepository.save(module);
    }
    public void deleteModule(Long moduleId) {
        moduleRepository.deleteById(moduleId);
    }
}










