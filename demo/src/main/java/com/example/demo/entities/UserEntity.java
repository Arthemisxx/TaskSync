package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;
import java.util.HashSet;


@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String first_name;

    @NotBlank
    private String last_name;

    @NotBlank
//    TODO: or BLOB type (binary large object)
    private String avatar_url;

    @NotBlank
    @Email
    private String email;

//    TODO: MAYBE ENUM???
//    @Enumerated(EnumType.STRING)
//    private UserRoles role;
    @NotBlank
    private String role;

    @ManyToMany
    @JoinTable(
            name = "user_team",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<TeamEntity> teams = new HashSet<>();


}
