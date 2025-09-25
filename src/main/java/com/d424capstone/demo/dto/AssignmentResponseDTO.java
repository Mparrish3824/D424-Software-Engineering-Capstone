package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.Instant;

@Getter
@Setter
public class AssignmentResponseDTO {
    private Integer assignmentId;
    private Integer userId;
    private String username;
    private Integer eventId;
    private String eventName;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private String roleDescription;
    private String assignmentStatus;
    private String notes;
    private Instant createdAt;

    // Constructor
    public AssignmentResponseDTO(Integer assignmentId, Integer userId, String username,
                                 Integer eventId, String eventName, LocalTime shiftStartTime,
                                 LocalTime shiftEndTime, String roleDescription,
                                 String assignmentStatus, String notes, Instant createdAt) {
        this.assignmentId = assignmentId;
        this.userId = userId;
        this.username = username;
        this.eventId = eventId;
        this.eventName = eventName;
        this.shiftStartTime = shiftStartTime;
        this.shiftEndTime = shiftEndTime;
        this.roleDescription = roleDescription;
        this.assignmentStatus = assignmentStatus;
        this.notes = notes;
        this.createdAt = createdAt;
    }


}