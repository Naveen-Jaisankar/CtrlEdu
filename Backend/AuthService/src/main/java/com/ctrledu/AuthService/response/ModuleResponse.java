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
}
