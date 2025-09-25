package com.d424capstone.demo.repositories;

import com.d424capstone.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

//    List<User> findAllByUserRole (String user_role);
//
//    List<User> findAllByFirstName (String firstName);
//
//    List<User> findAllByLastName (String lastName);



}
