package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "subtasks")
public class SubtaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
