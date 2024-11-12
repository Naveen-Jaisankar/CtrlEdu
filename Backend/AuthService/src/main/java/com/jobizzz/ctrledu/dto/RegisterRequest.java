package com.jobizzz.ctrledu.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String role;
    private String password;
    private String invitationCode;
}
