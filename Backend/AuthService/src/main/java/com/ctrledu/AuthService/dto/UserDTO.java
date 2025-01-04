package com.ctrledu.AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Long userID;
    private String userName;
    private Long classId;
    private String className;
    private Long moduleId;
    private String moduleName;
}


