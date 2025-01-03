package com.ctrledu.MasterService.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherDTO {
    private Long teacherId;
    private String teacherName;
    private Long moduleId;
    private String moduleCode;
    private String moduleName;
    private Long classId;
    private String className;

    // Constructor
    public TeacherDTO(Long teacherId, String teacherName,
                      Long moduleId, String moduleCode, String moduleName,
                      Long classId, String className) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.moduleId = moduleId;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.classId = classId;
        this.className = className;
    }

    // Getters and Setters
}
