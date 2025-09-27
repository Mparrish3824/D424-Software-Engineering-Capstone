package com.d424capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrganizationResponseDTO {
    private Integer id;
    private Integer userId;
    private String username;
    private String userEmail;
    private String firstName;
    private String lastName;
    private Integer orgId;
    private String orgName;
    private String orgCode;
    private String orgRole;
    private String joinedAt;

    public UserOrganizationResponseDTO() {}

    public UserOrganizationResponseDTO(Integer id, Integer userId, String username, String userEmail, String firstName, String lastName, Integer orgId, String orgName, String orgCode, String orgRole, String joinedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgCode = orgCode;
        this.orgRole = orgRole;
        this.joinedAt = joinedAt;
    }
}
