package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskUpdateRequest {
    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private String taskPriority;
    private LocalDate dueDate;
}