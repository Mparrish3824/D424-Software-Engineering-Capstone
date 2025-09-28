package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventResponseDTO {
    private Integer id;
    private String eventName;
    private String eventDescription;
    private String eventType;
    private String eventStatus;
    private String eventDate;
    private Integer orgId;

    public EventResponseDTO() {}

    public EventResponseDTO(Integer id, String eventName, String eventDescription,
                            String eventType, String eventStatus, String eventDate, Integer orgId) {
        this.id = id;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventType = eventType;
        this.eventStatus = eventStatus;
        this.eventDate = eventDate;
        this.orgId = orgId;
    }
}
