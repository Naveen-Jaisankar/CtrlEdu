package com.jobizzz.ctrledu.response;

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

    public String redirectedPath;

    public LoginResponse() {
    }
}
