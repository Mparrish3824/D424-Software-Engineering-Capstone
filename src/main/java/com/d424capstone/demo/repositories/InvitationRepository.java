package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.Invitation;
import com.d424capstone.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

//    List<Invitation> findAllByInvitationStatus(String invitationStatus);

    Invitation findByEmail(String email);

//    @Query("select i from Invitation i where i.expiresAt < current_timestamp ")
//    List<Invitation> findAllByInvitationExpired();

    Optional<Invitation> findByInvitationToken(String invitationToken);

    List<Invitation> findAllByOrgId(Integer orgId);

    @Query("select i from Invitation i where i.invitationStatus = 'pending' AND i.expiresAt > current_timestamp AND i" +
            ".org.id = ?1")
    List<Invitation> findAllByInvitationPendingAndOrgId(Integer orgId);

    @Query("select i from Invitation i where i.expiresAt < current_timestamp AND i.org.id = ?1")
    List<Invitation> findAllByInvitationExpiredAndOrgId(Integer orgId);
}
