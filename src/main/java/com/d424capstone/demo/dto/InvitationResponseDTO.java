package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationResponseDTO {
    private Integer invitationId;
    private String email;
    private String orgRole;
    private String invitedBy;
    private String invitationStatus;
    private String createdDate;
    private String expiresAt;
    private String invitationToken;

    public InvitationResponseDTO(Integer invitationId, String email, String orgRole,
                                 String invitedBy, String invitationStatus,
                                 String createdDate, String expiresAt, String invitationToken) {
        this.invitationId = invitationId;
        this.email = email;
        this.orgRole = orgRole;
        this.invitedBy = invitedBy;
        this.invitationStatus = invitationStatus;
        this.createdDate = createdDate;
        this.expiresAt = expiresAt;
        this.invitationToken = invitationToken;
    }
}