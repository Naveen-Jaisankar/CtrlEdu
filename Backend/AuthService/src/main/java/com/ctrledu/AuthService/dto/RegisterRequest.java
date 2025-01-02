package com.ctrledu.AuthService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterRequest {

    @JsonProperty("organization_name")
    @NotNull(message = "Organization name cannot be null")
    private String organizationName;

    @JsonProperty("email")
    @NotNull(message = "Email cannot be null")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "Password cannot be null")
    private String password;

    @JsonProperty("first_name")
    @NotNull(message = "FirsName cannot be null")
    private String firstName;

    @JsonProperty("last_name")
    @NotNull(message = "LastName cannot be null")
    private String lastName;
}
