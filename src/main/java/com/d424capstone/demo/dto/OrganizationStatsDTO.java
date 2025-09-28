package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationStatsDTO {
    private Integer totalMembers;
    private Integer coordinators;
    private Integer totalEvents;
    private Integer activeEvents;
    private Integer pendingInvitations;
    private Double totalBudget;

    public OrganizationStatsDTO() {}

    public OrganizationStatsDTO(Integer totalMembers, Integer coordinators, Integer totalEvents,
                                Integer activeEvents, Integer pendingInvitations, Double totalBudget) {
        this.totalMembers = totalMembers;
        this.coordinators = coordinators;
        this.totalEvents = totalEvents;
        this.activeEvents = activeEvents;
        this.pendingInvitations = pendingInvitations;
        this.totalBudget = totalBudget;
    }


}