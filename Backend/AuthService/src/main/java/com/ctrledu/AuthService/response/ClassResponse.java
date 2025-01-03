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

    // New Constructor
    public ClassResponse(Long classId, String className, Integer numStudents,List<Long> moduleIds, List<Long> studentIds, List<String> studentNames, List<String> moduleNames) {
        this.classId = classId;
        this.className = className;
        this.numStudents = numStudents;
        this.moduleIds = moduleIds;
        this.studentIds = studentIds;
        this.studentNames = studentNames; // Accepting List<String> here
        this.moduleNames = moduleNames;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(Integer numStudents) {
        this.numStudents = numStudents;
    }

    public List<Long> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(List<Long> moduleIds) {
        this.moduleIds = moduleIds;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public List<String> getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(List<String> studentNames) {
        this.studentNames = studentNames;
    }

    public List<String> getModuleNames() {
        return moduleNames;
    }

    public void setModuleNames(List<String> moduleNames) {
        this.moduleNames = moduleNames;
    }
}
