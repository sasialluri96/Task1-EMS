package com.example.Task1.service;
import com.example.Task1.DTO.UserDTO;
import com.example.Task1.entities.User;
import com.example.Task1.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
    @Service
    public class UserService {
        @Autowired
        private UserRepo userRepo;
        public User saveUser(UserDTO userDTO) {
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setRoles(userDTO.getRoles());
            return userRepo.save(user);
        }
    }

