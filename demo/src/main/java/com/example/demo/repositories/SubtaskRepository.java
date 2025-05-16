package com.example.demo.repositories;

import com.example.demo.entities.SubtaskEntity;
import com.example.demo.entities.TaskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtaskRepository  extends CrudRepository<SubtaskEntity, Long> {
    void deleteAllByMainTask(TaskEntity mainTask);
    List<SubtaskEntity> findAllByMainTask(TaskEntity mainTask);
}
