package com.example.demo.entities;
import com.example.demo.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "subtasks")
public class SubtaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "tresc", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('to do', 'in progress', 'done')", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "id_zadanie_glowne", nullable = false)
    private TaskEntity mainTask;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskEntity getMainTask() {
        return mainTask;
    }

    public void setMainTask(TaskEntity mainTask) {
        this.mainTask = mainTask;
    }
}
