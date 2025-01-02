package com.ctrledu.AuthService.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClassRequest {
    private String className;
    private int numberOfStudents;
    private List<Long> moduleIds; // List of associated module IDs
    private List<Long> studentIds; // List of associated student IDs
}



