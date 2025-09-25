package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    List<Organization> findAllByOrgName(String orgName);

    Optional<Organization> findByOrgCode(String orgCode);

}
