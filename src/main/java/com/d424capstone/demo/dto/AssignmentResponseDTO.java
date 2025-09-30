package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
public class AssignmentResponseDTO {
    private Integer assignmentId;
    private Integer userId;
    private String username;
    private Integer eventId;
    private String eventName;
    private String shiftStartTime;
    private String shiftEndTime;
    private String roleDescription;
    private String assignmentStatus;
    private String notes;
    private Instant createdAt;

    private UserDTO user;

    public AssignmentResponseDTO() {
    }

    public AssignmentResponseDTO(Integer assignmentId, Integer userId, String username,
                                 Integer eventId, String eventName, LocalTime shiftStartTime,
                                 LocalTime shiftEndTime, String roleDescription,
                                 String assignmentStatus, String notes, Instant createdAt,
                                 UserDTO user) {  
        this.assignmentId = assignmentId;
        this.userId = userId;
        this.username = username;
        this.eventId = eventId;
        this.eventName = eventName;
        this.shiftStartTime = shiftStartTime != null ? shiftStartTime.toString() : null;
        this.shiftEndTime = shiftEndTime != null ? shiftEndTime.toString() : null;
        this.roleDescription = roleDescription;
        this.assignmentStatus = assignmentStatus;
        this.notes = notes;
        this.createdAt = createdAt;
        this.user = user;
    }


    public static class UserDTO {
        private Integer id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;

        public UserDTO(Integer id, String username, String email, String firstName, String lastName) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public Integer getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getEmail() {
            return email;
        }

        public String getLastName() {
            return lastName;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }




}