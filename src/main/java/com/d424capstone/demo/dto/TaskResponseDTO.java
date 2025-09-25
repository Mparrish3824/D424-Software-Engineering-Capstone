package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskResponseDTO {
    private Integer id;
    private Integer eventId;
    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private String taskPriority;
    private LocalDate dueDate;
    private Instant createdAt;
    private Instant updatedAt;
    private Double completionPercentage;
    private List<SubtaskResponseDTO> subtasks;

    public TaskResponseDTO(Integer id, Integer eventId, String taskName, String taskDescription, String taskStatus,
                           String taskPriority, LocalDate dueDate, Instant createdAt, Instant updatedAt, Double completionPercentage, List<SubtaskResponseDTO> subtasks) {
        this.id = id;
        this.eventId = eventId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completionPercentage = completionPercentage;
        this.subtasks = subtasks;
    }

    public TaskResponseDTO() {
    }
}
