package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "teams")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @NotBlank
    private String teamName;

    private String description;

    @ManyToMany
    private Set<UserEntity> users;

    @OneToMany(mappedBy = "team")
    private Set<TaskEntity> tasks;
}
