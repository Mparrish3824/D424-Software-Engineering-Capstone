package com.d424capstone.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "user_id", nullable = false)
    private Integer id;

    @Column (name = "username", nullable = false, length = 50)
    private String username;

    @Column (name = "user_password", nullable = false)
    private String userPassword;

    @Column (name = "email", nullable = false, length = 100)
    private String email;

    @Column (name = "first_name", length = 50)
    private String firstName;

    @Column (name = "last_name", length = 50)
    private String lastName;

    @Column (name = "phone", length = 15)
    private String phone;

    @ColumnDefault ("'user'")
    @Column (name = "user_role")
    private String userRole;

    @ColumnDefault ("1")
    @Column (name = "is_active")
    private Boolean isActive;

    @Column (name = "created_at")
    private Instant createdAt;

    @Column (name = "updated_at")
    private Instant updatedAt;


    public User(Integer id, String username, String userPassword, String email, String firstName, String lastName, String phone, String userRole, Boolean isActive, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.username = username;
        this.userPassword = userPassword;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.userRole = userRole;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public User() {

    }
}