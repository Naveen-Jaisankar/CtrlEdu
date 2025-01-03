package com.ctrledu.AuthService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherResponse {
    private Long teacherId;
    private String firstName;
    private String lastName;
    private boolean isAssigned;

    public TeacherResponse(Long teacherId, String firstName, String lastName, boolean isAssigned) {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAssigned = isAssigned;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
}
