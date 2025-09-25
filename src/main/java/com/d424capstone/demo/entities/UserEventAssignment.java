package com.d424capstone.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table (name = "user_event_assignments")
public class UserEventAssignment {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "assignment_id", nullable = false)
    private Integer id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @OnDelete (action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "event_id")
    private Event event;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @OnDelete (action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Column (name = "shift_start_time")
    private LocalTime shiftStartTime;

    @Column (name = "shift_end_time")
    private LocalTime shiftEndTime;

    @Column (name = "role_description", length = 250)
    private String roleDescription;

    @ColumnDefault ("'pending'")
    @Column (name = "assignment_status")
    private String assignmentStatus;

    @Lob
    @Column (name = "notes")
    private String notes;

    @Column (name = "created_at")
    private Instant createdAt;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @OnDelete (action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "org_id", nullable = false)
    private Organization org;


    public UserEventAssignment(Integer id, Event event, Organization org, User user, LocalTime shiftStartTime, LocalTime shiftEndTime, String roleDescription, String assignmentStatus, String notes, Instant createdAt) {
        this.id = id;
        this.event = event;
        this.org = org;
        this.user = user;
        this.shiftStartTime = shiftStartTime;
        this.shiftEndTime = shiftEndTime;
        this.roleDescription = roleDescription;
        this.assignmentStatus = assignmentStatus;
        this.notes = notes;
        this.createdAt = createdAt;
    }


    public UserEventAssignment() {

    }
}