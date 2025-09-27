package com.d424capstone.demo.services;

import com.d424capstone.demo.dto.UserOrganizationResponseDTO;
import com.d424capstone.demo.entities.Organization;
import com.d424capstone.demo.entities.User;
import com.d424capstone.demo.entities.UserOrganization;
import com.d424capstone.demo.repositories.OrganizationRepository;
import com.d424capstone.demo.repositories.UserOrganizationRepository;
import com.d424capstone.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserOrganizationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserOrganizationRepository  userOrganizationRepository;


    // Add user to organization
    public UserOrganization addUserToOrganization(String username, Integer organizationId, String role) {
        UserOrganization userOrganization = new UserOrganization();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not found: " + username));
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new RuntimeException("Organization not found: "));

        Optional<UserOrganization> exists = userOrganizationRepository.findByUserIdAndOrgId(user.getId(),
                organization.getId());
        if (exists.isPresent()) {
            throw new RuntimeException(username + " already joined this organization");
        }
        userOrganization.setUser(user);
        userOrganization.setOrg(organization);
        userOrganization.setOrgRole(role);
        return userOrganizationRepository.save(userOrganization);
    }

    // Remove user from organization
    public void removeUserFromOrganization(String username, Integer organizationId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not " +
                "found: " + username));
        UserOrganization userOrg =
                userOrganizationRepository.findByUserIdAndOrgId(user.getId(), organizationId).orElseThrow(() -> new RuntimeException("User is not a member of this organization"));
        userOrganizationRepository.delete(userOrg);
    }

    // Change user role in organization
    public void updateUserRole(String username, Integer organizationId, String role) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not " +
                "found: " + username));
        UserOrganization userOrg =
                userOrganizationRepository.findByUserIdAndOrgId(user.getId(), organizationId).orElseThrow(() -> new RuntimeException("User is not a member of this organization"));
        userOrg.setOrgRole(role);
        userOrganizationRepository.save(userOrg);
    }


    // find all users by organization id
    public List<UserOrganization> getUsersByOrganization(Integer organizationId) {
        return userOrganizationRepository.findAllByOrgId(organizationId);
    }

    // find all organizations by user id
    public List<UserOrganization> getOrganizationsByUser(Integer userid) {
        return userOrganizationRepository.findAllByUserId(userid);
    }

    // find all users by role in organization
    public List<UserOrganization> getUsersByRoleInOrganization(Integer organizationId, String role) {
        return userOrganizationRepository.findAllByOrgIdAndOrgRole(organizationId, role);
    }

    // find a user/org relationship
    public Optional<UserOrganization> getUserOrganization(Integer userId, Integer organizationId) {
        return userOrganizationRepository.findByUserIdAndOrgId(userId, organizationId);
    }

    // DTO mapping
    @Transactional (readOnly = true)
    public List<UserOrganizationResponseDTO> getOrganizationsByUserId(Integer userId) {
        List<UserOrganization> userOrganizations = userOrganizationRepository.findAllByUserId(userId);
        return userOrganizations.stream()
                .map(this::mapToUserOrganizationResponseDTO)
                .collect(Collectors.toList());
    }

    private UserOrganizationResponseDTO mapToUserOrganizationResponseDTO(UserOrganization userOrg) {
        return new UserOrganizationResponseDTO(
                userOrg.getId(),
                userOrg.getUser().getId(),
                userOrg.getUser().getUsername(),
                userOrg.getUser().getEmail(),
                userOrg.getUser().getFirstName(),
                userOrg.getUser().getLastName(),
                userOrg.getOrg().getId(),
                userOrg.getOrg().getOrgName(),
                userOrg.getOrg().getOrgCode(),
                userOrg.getOrgRole(),
                userOrg.getJoinedAt().toString()
        );
    }










}

