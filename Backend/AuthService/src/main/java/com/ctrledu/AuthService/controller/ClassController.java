package com.ctrledu.AuthService.controller;

import com.ctrledu.AuthService.dto.ClassRequest;
import com.ctrledu.AuthService.dto.ThreadContext;
import com.ctrledu.AuthService.response.ClassResponse;
import com.ctrledu.AuthService.response.StudentResponse;
import com.ctrledu.AuthService.response.TeacherResponse;
import com.ctrledu.AuthService.service.AdminService;
import com.ctrledu.AuthService.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ClassController {

    @Autowired
    private ClassService classService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/classes")
    public List<ClassResponse> getClasses() {
        String email = ThreadContext.getEmail(); // Retrieve the email from thread context
        Long orgId = adminService.getAdminOrgId(email); // Fetch the orgId using AdminService
        return classService.getClassesByOrgId(orgId);
    }

    @PostMapping("/add-class")
    public ResponseEntity<String> addClass(@RequestBody ClassRequest request) {
        System.out.println("Received ClassRequest payload: " + request);
        System.out.println("className: " + request.getClassName());
        System.out.println("numberOfStudents: " + request.getNumberOfStudents());
        System.out.println("moduleIds: " + request.getModuleIds());
        System.out.println("studentIds: " + request.getStudentIds());
        classService.addClass(request);
        return ResponseEntity.ok("Class added successfully.");
    }


    @PutMapping("/edit-class/{classId}")
    public ResponseEntity<String> editClass(@PathVariable Long classId, @RequestBody ClassRequest request) {
        classService.editClass(classId, request);
        return ResponseEntity.ok("Class updated successfully.");
    }

    @DeleteMapping("/delete-class/{classId}")
    public ResponseEntity<String> deleteClass(@PathVariable Long classId) {
        classService.deleteClass(classId);
        return ResponseEntity.ok("Class deleted successfully.");
    }

    @GetMapping("/students")
    public List<StudentResponse> getStudents() {
        String email = ThreadContext.getEmail(); // Retrieve the email of the logged-in admin
        Long orgId = adminService.getAdminOrgId(email); // Fetch organization ID
        return classService.getStudentsByOrgId(orgId); // Fetch students by orgId
    }




}

