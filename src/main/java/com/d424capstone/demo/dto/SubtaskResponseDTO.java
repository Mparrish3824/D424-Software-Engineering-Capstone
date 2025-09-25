package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class SubtaskResponseDTO {
    private Integer id;
    private String subtaskName;
    private String subtaskStatus;
    private String subtaskDescription;
    private LocalDate dueDate;
    private Instant createdAt;
    private Instant updatedAt;

    public SubtaskResponseDTO(Integer id, String subtaskName, String subtaskStatus, String subtaskDescription,
                              LocalDate dueDate,Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.subtaskName = subtaskName;
        this.subtaskStatus = subtaskStatus;
        this.subtaskDescription = subtaskDescription;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public SubtaskResponseDTO() {
    }
}
