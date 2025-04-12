package com.example.demo.entities;
import com.example.demo.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@Entity
@Table(name = "subtasks")
public class SubtaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    //@Column(name = "tresc", nullable = false)
    private String content;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "status", columnDefinition = "ENUM('to do', 'in progress', 'done')", nullable = false)
//    private Status status;
    @NotBlank
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity mainTask;

}
