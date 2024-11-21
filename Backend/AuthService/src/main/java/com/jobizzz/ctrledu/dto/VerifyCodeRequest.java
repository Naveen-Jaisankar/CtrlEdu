package com.jobizzz.ctrledu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyCodeRequest {
    private String code;
    private String email;
    private String password;

    // Getters and Setters
}
