package com.ctrledu.AuthService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    public String accessToken;

    public String refreshToken;

    public String role;

    public LoginResponse() {
    }
}
