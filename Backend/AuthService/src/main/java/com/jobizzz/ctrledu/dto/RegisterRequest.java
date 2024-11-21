package com.jobizzz.ctrledu.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String role;
    private String password;
    private String invitationCode;
    private String firstName; // New field
    private String lastName;  // New field
}
