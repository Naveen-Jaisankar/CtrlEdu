package com.jobizzz.ctrledu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleRequest {
    private String moduleCode;
    private String moduleName;
    private Long teacherId;
}

