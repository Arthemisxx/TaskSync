package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String title;

    private String desciption;

    //private String attachments;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private TaskPriority priority;

    @OneToMany(mappedBy = "task")
    private Set<SubtaskEntity> subtasks;
}
