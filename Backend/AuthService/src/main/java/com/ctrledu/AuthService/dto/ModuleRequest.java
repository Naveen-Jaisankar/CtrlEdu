package com.ctrledu.AuthService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleRequest {
    private String moduleCode;
    private String moduleName;
    private Long teacherId;

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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}

