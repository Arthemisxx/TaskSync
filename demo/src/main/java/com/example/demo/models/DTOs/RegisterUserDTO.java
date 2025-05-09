package com.example.demo.models.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterUserDTO {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String avatar;
    private Set<TeamDTO> teams;

}
