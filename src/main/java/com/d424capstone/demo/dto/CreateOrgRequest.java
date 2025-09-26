package com.d424capstone.demo.dto;

import com.d424capstone.demo.entities.Organization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrgRequest {
    private String orgName;
    private String orgDescription;
    private String contactEmail;
    private String contactPhone;
    private String location;

    // Constructors
    public CreateOrgRequest() {}

    public CreateOrgRequest(String orgName, String orgDescription, String contactEmail, String contactPhone, String location) {
        this.orgName = orgName;
        this.orgDescription = orgDescription;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.location = location;
    }

    // Getters and setters
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public String getOrgDescription() { return orgDescription; }
    public void setOrgDescription(String orgDescription) { this.orgDescription = orgDescription; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Organization toOrganization() {

        Organization org = new Organization();
        org.setOrgName(this.orgName);
        org.setOrgDescription(this.orgDescription);
        org.setLocation(this.location);
        org.setContactEmail(this.contactEmail);
        org.setContactPhone(this.contactPhone);
        org.setIsActive(true);
        return org;
    }
}