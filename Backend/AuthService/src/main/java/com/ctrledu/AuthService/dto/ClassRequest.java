package com.ctrledu.AuthService.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ClassRequest {
    private String className;
    private Integer numStudents;
    private List<Long> moduleIds;
}

