package com.ctrledu.AuthService.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponse {
    private Long studentId;
    private String firstName;
    private String lastName;
    private boolean isAssigned;

    public StudentResponse(Long studentId, String firstName, String lastName, boolean isAssigned) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAssigned = isAssigned;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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


