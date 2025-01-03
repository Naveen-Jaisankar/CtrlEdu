package com.ctrledu.MasterService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentDTO {
    private Long studentId;
    private String studentName;
    private Long classId;
    private String className;
    private Long moduleId;
    private String moduleName;
}
