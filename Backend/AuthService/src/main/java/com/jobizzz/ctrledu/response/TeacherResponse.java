package com.jobizzz.ctrledu.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeacherResponse {
    private Long teacherId;
    private String firstName;
    private String lastName;
    private boolean isAssigned;
}
