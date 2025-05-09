package com.example.demo.models.DTOs;

import com.example.demo.models.UserRoles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String avatar;
    private String email;
    private UserRoles role;
    private Set<Long> teamIds;
}
