package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "teams")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    // TODO Chyba opis nie musi być @notblank?
    //@NotBlank
    private String description;

    @ManyToMany(mappedBy = "teams")
    private Set<UserEntity> users = new HashSet<>();
}
