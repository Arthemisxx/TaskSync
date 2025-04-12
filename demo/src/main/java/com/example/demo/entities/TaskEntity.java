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

    //@NotBlank
    private String desciption;

    private String attachments;

    @Column(name = "from_date")  // Custom column name in DB
    private LocalDate fromDate;

    @Column(name = "to_date")    // Custom column name in DB
    private LocalDate toDate;

//    TODO: MAYBE ENUM???       - done
    @Enumerated(EnumType.STRING)
    @NotBlank
    private TaskStatus status;
//    @NotBlank
//    private String status;

    //    TODO: MAYBE ENUM???       - done
    @Enumerated(EnumType.STRING)
    @NotBlank
    private TaskPriority priority;
//    @NotBlank
//    private int priority;

    //czy ma to sens jak jest opis?
    //TODO
    @NotBlank
    private String comments;


    @OneToMany(mappedBy = "task")
    private Set<SubtaskEntity> subtasks;
}
