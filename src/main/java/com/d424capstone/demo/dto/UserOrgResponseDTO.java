package com.d424capstone.demo.dto;

public class UserOrgResponseDTO {
    private Integer orgId;
    private String orgRole;

    public UserOrgResponseDTO() {}

    public UserOrgResponseDTO(Integer orgId, String orgRole) {
        this.orgId = orgId;
        this.orgRole = orgRole;
    }

    public Integer getOrgId() { return orgId; }
    public void setOrgId(Integer orgId) { this.orgId = orgId; }

    public String getOrgRole() { return orgRole; }
    public void setOrgRole(String orgRole) { this.orgRole = orgRole; }
}
