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

    @ManyToMany
    private Set<UserEntity> users;

    @OneToMany(mappedBy = "team")
    private Set<TaskEntity> tasks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private UserEntity manager;


    public TeamEntity(String teamName, String description, Set<UserEntity> users, Set<TaskEntity> tasks, UserEntity manager) {
        this.teamName = teamName;
        this.description = description;
        this.users = users;
        this.tasks = tasks;
        this.manager = manager;
    }
}
