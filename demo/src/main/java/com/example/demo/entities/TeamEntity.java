package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teams")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @NotBlank
    private String teamName;

    private String description;

    @ManyToMany(mappedBy = "teams")
    private Set<UserEntity> users;

    @OneToMany(mappedBy = "team")
    private Set<TaskEntity> tasks;


    public TeamEntity(String teamName, String description, Set<UserEntity> users, Set<TaskEntity> tasks) {
        this.teamName = teamName;
        this.description = description;
        this.users = users;
        this.tasks = tasks;
    }
}
