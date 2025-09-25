package com.d424capstone.demo.services;

import com.d424capstone.demo.entities.User;
import com.d424capstone.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername (String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not found"));
    }

    public User findByEmail (String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));
    }

    public Optional<User> findById(Integer id){
        return userRepository.findById(id);
    }

//    Create a New User Account
    public User createNewUser (User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }


//    Authenticate User Login
    public User authenticateUser (String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            if (password.equals(user.get().getUserPassword())) {
                return user.get();
            }
                else {
                    throw new RuntimeException("Invalid username or password");
            }
        }
            else {
            throw new RuntimeException("Invalid username or password");
            }
    }



//    Forgot Username




//    Forgot Password



//    Remove Member from Org


}
