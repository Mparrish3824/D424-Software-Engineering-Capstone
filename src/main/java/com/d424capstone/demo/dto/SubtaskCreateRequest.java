package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SubtaskCreateRequest {
    private String subtaskName;
    private String subtaskStatus;
    private LocalDate dueDate;
    private String subtaskDescription;
}
