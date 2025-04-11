package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String desciption;

    private String attachments;

    @Column(name = "from_date")  // Custom column name in DB
    private LocalDate fromDate;

    @Column(name = "to_date")    // Custom column name in DB
    private LocalDate toDate;

//    TODO: MAYBE ENUM???
//    @Enumerated(EnumType.STRING)
//    private TaskStatus status;
    @NotBlank
    private String status;

    //    TODO: MAYBE ENUM???
//    @Enumerated(EnumType.STRING)
//    private TaskPriority priority;
    @NotBlank
    private int priority;

    //czy ma to sens jak jest opis?
    @NotBlank
    private String comments;

}
