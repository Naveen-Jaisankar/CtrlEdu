package com.jobizzz.ctrledu.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ClassResponse {
    private Long classId;
    private String className;
    private Integer numStudents;
    private List<String> moduleNames;

    // Parameterized Constructor
    public ClassResponse(Long classId, String className, Integer numStudents, List<String> moduleNames) {
        this.classId = classId;
        this.className = className;
        this.numStudents = numStudents;
        this.moduleNames = moduleNames;
    }

    // Getters and Setters
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

    public List<String> getModuleNames() {
        return moduleNames;
    }

    public void setModuleNames(List<String> moduleNames) {
        this.moduleNames = moduleNames;
    }
}
