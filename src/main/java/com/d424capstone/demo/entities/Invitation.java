package com.d424capstone.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "invitation_id", nullable = false)
    private Integer id;

    @Column (name = "email", nullable = false, length = 100)
    private String email;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @OnDelete (action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "org_id", nullable = false)
    private Organization org;

    @ColumnDefault ("'member'")
    @Column (name = "org_role")
    private String orgRole;

    @ColumnDefault ("'pending'")
    @Column (name = "invitation_status")
    private String invitationStatus;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "invited_by", nullable = false)
    private User invitedBy;

    @Column (name = "invitation_token")
    private String invitationToken;

    @Column (name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column (name = "created_date")
    private Instant createdDate;

    @Column (name = "responded_at")
    private Instant respondedAt;

    public Invitation(Integer id, String email, Organization org, String orgRole, String invitationStatus, User invitedBy, String invitationToken, Instant expiresAt, Instant createdDate, Instant respondedAt) {
        this.id = id;
        this.email = email;
        this.org = org;
        this.orgRole = orgRole;
        this.invitationStatus = invitationStatus;
        this.invitedBy = invitedBy;
        this.invitationToken = invitationToken;
        this.expiresAt = expiresAt;
        this.createdDate = createdDate;
        this.respondedAt = respondedAt;
    }

    public Invitation() {

    }
}
