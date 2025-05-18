package com.example.demo.services;

import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<UserEntity> allUsers(){
        return userRepository.findAll();
    }

    public Optional<UserEntity> findUserByEmail(String email) { return userRepository.findByEmail(email);}
}
