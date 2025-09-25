package com.d424capstone.demo.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "event_id", nullable = false)
    private Integer id;

    @Column (name = "event_name", nullable = false, length = 100)
    private String eventName;

    @Column (name = "event_type", nullable = false)
    private String eventType;

    @Lob
    @Column (name = "event_description")
    private String eventDescription;

    @Column (name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column (name = "start_time")
    private LocalTime startTime;

    @Column (name = "end_time")
    private LocalTime endTime;

    @Column (name = "location", length = 100)
    private String location;

    @Column (name = "budget", precision = 10, scale = 2)
    private BigDecimal budget;

    @ColumnDefault ("'draft'")
    @Column (name = "event_status")
    private String eventStatus;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "org_id", nullable = false)
    private Organization org;

    public Event(Integer id, String eventName, String eventType, String eventDescription, LocalDate eventDate, LocalTime startTime, LocalTime endTime, String location, BigDecimal budget, String eventStatus, Organization org) {
        this.id = id;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.budget = budget;
        this.eventStatus = eventStatus;
        this.org = org;
    }

    public Event() {

    }
}