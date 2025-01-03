package com.ctrledu.AuthService.service;

import com.ctrledu.AuthService.dto.ClassRequest;
import com.ctrledu.AuthService.dto.ThreadContext;
import com.ctrledu.AuthService.entity.ClassEntity;
import com.ctrledu.AuthService.entity.ModuleEntity;
import com.ctrledu.AuthService.entity.OrganizationEntity;
import com.ctrledu.AuthService.entity.UserEntity;
import com.ctrledu.AuthService.repository.ClassRepository;
import com.ctrledu.AuthService.repository.ModuleRepository;
import com.ctrledu.AuthService.repository.OrganizationRepository;
import com.ctrledu.AuthService.repository.UserRepository;
import com.ctrledu.AuthService.response.ClassResponse;
import com.ctrledu.AuthService.response.StudentResponse;
import com.ctrledu.AuthService.response.TeacherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private AdminService adminService;

    public List<ClassResponse> getClassesByOrgId(Long orgId) {
        List<ClassEntity> classes = classRepository.findByOrgIdWithStudentsAndModules(orgId);

        return classes.stream().map(classEntity -> {
            List<Long> moduleIds = classEntity.getModules().stream()
                    .map(ModuleEntity::getModuleId)
                    .collect(Collectors.toList()); // Get module IDs

            List<Long> studentIds = classEntity.getStudents().stream()
                    .map(UserEntity::getUserId)
                    .collect(Collectors.toList());

            List<String> studentNames = classEntity.getStudents().stream()
                    .map(student -> student.getUserFirstName() + " " + student.getUserLastName())
                    .collect(Collectors.toList());

            List<String> moduleNames = classEntity.getModules().stream()
                    .map(ModuleEntity::getModuleName)
                    .collect(Collectors.toList());

            return new ClassResponse(
                    classEntity.getClassId(),
                    classEntity.getClassName(),
                    classEntity.getNumberOfStudents(),
                    moduleIds, // Include moduleIds
                    studentIds,
                    studentNames,
                    moduleNames
            );
        }).collect(Collectors.toList());
    }


    public void addClass(ClassRequest request) {
        System.out.println("Received request: " + request);

        String email = ThreadContext.getEmail();
        Long orgId = adminService.getAdminOrgId(email);

        // Validate inputs
        if (request.getClassName() == null || request.getClassName().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be null or empty");
        }
        if (request.getNumberOfStudents() <= 0) {
            throw new IllegalArgumentException("Number of students must be greater than zero");
        }
        if (request.getModuleIds() == null || request.getModuleIds().isEmpty()) {
            throw new IllegalArgumentException("At least one module must be associated with the class");
        }
        if (request.getStudentIds() == null || request.getStudentIds().isEmpty()) {
            throw new IllegalArgumentException("At least one student must be associated with the class");
        }

        // Fetch organization
        OrganizationEntity org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid organization ID"));
        System.out.println("Fetched organization: " + org);

        // Instantiate ClassEntity
        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassName(request.getClassName());
        classEntity.setNumberOfStudents(request.getNumberOfStudents());
        classEntity.setOrg(org);

        // Fetch and associate modules
        List<ModuleEntity> modules = moduleRepository.findAllById(request.getModuleIds());
        if (modules.isEmpty()) {
            throw new IllegalArgumentException("Invalid module IDs provided");
        }
        System.out.println("Fetched modules: " + modules);
        classEntity.setModules(new HashSet<>(modules));

        // Fetch and associate students
        List<UserEntity> students = userRepository.findAllById(request.getStudentIds());
        if (students.isEmpty()) {
            throw new IllegalArgumentException("Invalid student IDs provided");
        }
        System.out.println("Fetched students: " + students);
        classEntity.setStudents(new HashSet<>(students));

        // Save class entity
        classRepository.save(classEntity);
        System.out.println("Class saved successfully: " + classEntity);
    }





//    public void editClass(Long classId, ClassRequest request) {
//        ClassEntity classEntity = classRepository.findById(classId)
//                .orElseThrow(() -> new IllegalArgumentException("Class not found."));
//
//        classEntity.setClassName(request.getClassName());
//        classEntity.setNumberOfStudents(request.getNumberOfStudents());
//
//        // Update modules
//        List<ModuleEntity> modules = moduleRepository.findAllById(request.getModuleIds());
//        classEntity.setModules(new HashSet<>(modules));
//
//        // Update students
//        List<UserEntity> students = userRepository.findAllById(request.getStudentIds());
//        classEntity.setStudents(new HashSet<>(students));
//
//        classRepository.save(classEntity);
//    }
//    public List<ClassResponse> getClasses(Long orgId) {
//        List<ClassEntity> classes = classRepository.findByOrgIdWithStudents(orgId);
//
//        return classes.stream().map(cls -> {
//            List<Long> studentIds = cls.getStudents().stream()
//                    .map(UserEntity::getUserId)
//                    .collect(Collectors.toList());
//            List<String> studentNames = cls.getStudents().stream()
//                    .map(student -> student.getUserFirstName() + " " + student.getUserLastName())
//                    .collect(Collectors.toList());
//
//            return new ClassResponse(
//                    cls.getClassId(),
//                    cls.getClassName(),
//                    cls.getNumberOfStudents(), // Fixed getter method
//                    cls.getModules().stream().map(ModuleEntity::getModuleId).collect(Collectors.toList()),
//                    studentIds,
//                    studentNames
//            );
//        }).collect(Collectors.toList());
//    }
public List<StudentResponse> getStudentsByOrgId(Long orgId) {
    List<UserEntity> students = userRepository.findUnassignedStudentsByOrgId(orgId);
    return students.stream()
            .map(student -> new StudentResponse(
                    student.getUserId(),
                    student.getUserFirstName(),
                    student.getUserLastName(),
                    false // Unassigned students
            ))
            .collect(Collectors.toList());
}
    public void editClass(Long classId, ClassRequest request) {
        // Fetch class entity
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found."));

        // Validate inputs
        if (request.getClassName() == null || request.getClassName().trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be null or empty");
        }
        if (request.getNumberOfStudents() <= 0) {
            throw new IllegalArgumentException("Number of students must be greater than zero");
        }
        if (request.getModuleIds() == null || request.getModuleIds().isEmpty()) {
            throw new IllegalArgumentException("At least one module must be associated with the class");
        }
        if (request.getStudentIds() == null || request.getStudentIds().isEmpty()) {
            throw new IllegalArgumentException("At least one student must be associated with the class");
        }

        // Update class details
        classEntity.setClassName(request.getClassName());
        classEntity.setNumberOfStudents(request.getNumberOfStudents());

        // Update associated modules
        List<ModuleEntity> modules = moduleRepository.findAllById(request.getModuleIds());
        if (modules.isEmpty()) {
            throw new IllegalArgumentException("Invalid module IDs provided");
        }
        classEntity.setModules(new HashSet<>(modules));

        // Update associated students
        List<UserEntity> students = userRepository.findAllById(request.getStudentIds());
        if (students.isEmpty()) {
            throw new IllegalArgumentException("Invalid student IDs provided");
        }
        classEntity.setStudents(new HashSet<>(students));

        // Save updated class
        classRepository.save(classEntity);
    }


    public void deleteClass(Long classId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found."));

        classRepository.delete(classEntity);
    }








}


