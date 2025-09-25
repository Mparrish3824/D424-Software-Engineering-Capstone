package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class AssignmentRequestDTO {
    private Integer userId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roleDescription;
    private String notes;

    public AssignmentRequestDTO(){

    }

    public AssignmentRequestDTO(Integer userId, LocalTime startTime, LocalTime endTime, String roleDescription, String notes) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roleDescription = roleDescription;
        this.notes = notes;
    }
}
