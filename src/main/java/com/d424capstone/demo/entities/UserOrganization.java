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
@Table(name = "user_organizations")
public class UserOrganization {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "user_org_id", nullable = false)
    private Integer id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @OnDelete (action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @OnDelete (action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "org_id", nullable = false)
    private Organization org;

    @ColumnDefault ("'member'")
    @Column (name = "org_role")
    private String orgRole;

    @Column (name = "joined_at")
    private Instant joinedAt;


    public UserOrganization(Integer id, User user, Organization org, String orgRole, Instant joinedAt) {
        this.id = id;
        this.user = user;
        this.org = org;
        this.orgRole = orgRole;
        this.joinedAt = joinedAt;
    }
    public UserOrganization(User user, Organization org, String orgRole) {
        this.user = user;
        this.org = org;
        this.orgRole = orgRole;
    }

    public UserOrganization() {
    }
}