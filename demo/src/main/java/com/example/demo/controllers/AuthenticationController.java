package com.example.demo.controllers;

import com.example.demo.entities.UserEntity;
import com.example.demo.models.DTOs.LoginUserDTO;
import com.example.demo.models.DTOs.RegisterUserDTO;
import com.example.demo.models.DTOs.response.LoginResponse;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterUserDTO registerUserDTO){
        UserEntity registeredUser = authenticationService.signup(registerUserDTO);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDTO){
        UserEntity authenticatedUser = authenticationService.authenticate(loginUserDTO);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
