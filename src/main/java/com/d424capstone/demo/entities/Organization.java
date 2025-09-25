package com.d424capstone.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "org_id", nullable = false)
    private Integer id;

    @Column (name = "org_name", nullable = false, length = 250)
    private String orgName;

    @Column (name = "org_description")
    private String orgDescription;

    @Column (name = "location", length = 250)
    private String location;

    @Column (name = "contact_email", length = 100)
    private String contactEmail;

    @Column (name = "contact_phone", length = 15)
    private String contactPhone;

    @Column (name = "org_code", nullable = false, length = 6)
    private String orgCode;

    @ColumnDefault ("1")
    @Column (name = "is_active")
    private Boolean isActive;

    @Column (name = "created_at")
    private Instant createdAt;


    public Organization(Integer id, String orgName, String orgDescription, String location, String contactEmail, String contactPhone, String orgCode, Boolean isActive, Instant createdAt) {
        this.id = id;
        this.orgName = orgName;
        this.orgDescription = orgDescription;
        this.location = location;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.orgCode = orgCode;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }


    public Organization() {

    }
}
