package com.ctrledu.AuthService.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ClassResponse {
    private Long classId;
    private String className;
    private Integer numStudents;
    private List<Long> moduleIds;
    private List<Long> studentIds;
    private List<String> studentNames;
    private List<String> moduleNames; // Add this field

    // Existing Constructor
    public ClassResponse(Long classId, String className, Integer numStudents, List<String> moduleNames) {
        this.classId = classId;
        this.className = className;
        this.numStudents = numStudents;
        this.moduleNames = moduleNames;
    }

    // New Constructor
    public ClassResponse(Long classId, String className, Integer numStudents, List<Long> studentIds, List<String> studentNames, List<String> moduleNames) {
        this.classId = classId;
        this.className = className;
        this.numStudents = numStudents;
        this.studentIds = studentIds;
        this.studentNames = studentNames; // Accepting List<String> here
        this.moduleNames = moduleNames;
    }
}
