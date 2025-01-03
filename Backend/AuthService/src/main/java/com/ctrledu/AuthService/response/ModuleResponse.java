package com.ctrledu.AuthService.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleResponse {
    private Long moduleId;
    private String moduleCode;
    private String moduleName;
    private String teacherName;
    private Long teacherId;

    public ModuleResponse(Long moduleId, String moduleCode, String moduleName, String teacherName,Long teacherId) {
        this.moduleId = moduleId;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
