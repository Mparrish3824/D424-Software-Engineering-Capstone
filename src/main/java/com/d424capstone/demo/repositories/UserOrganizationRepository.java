package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.Organization;
import com.d424capstone.demo.entities.User;
import com.d424capstone.demo.entities.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Integer> {

    List<UserOrganization> findAllByOrgId(Integer orgId);

    List<UserOrganization> findAllByUserId(Integer userId);

    Optional<UserOrganization> findByUserIdAndOrgId(Integer userID, Integer orgID);

    List<UserOrganization> findAllByOrgIdAndOrgRole(Integer orgId, String role);

}
