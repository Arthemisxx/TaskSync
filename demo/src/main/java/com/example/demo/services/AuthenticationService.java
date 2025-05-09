package com.example.demo.services;

import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.models.DTOs.LoginUserDTO;
import com.example.demo.models.DTOs.RegisterUserDTO;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TeamRepository teamRepository;


    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.teamRepository = teamRepository;
    }

    public UserEntity signup(RegisterUserDTO input) {
        UserEntity user = new UserEntity();
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setAvatar(input.getAvatar());
        userRepository.save(user);
        String teamName = user.getFirstName() + "'s Team";
        teamRepository.save(new TeamEntity(teamName, "Your private team", Set.of(user), null, user));

        return user;
    }

    public UserEntity authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
