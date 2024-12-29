package com.jobizzz.ctrledu.response;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
public class SignupResponse {

    private Long userId;

    private String userEmail;

    private Long orgId;

    private String userFirstName;

    private String userLastName;


    public SignupResponse() {
    }
}
