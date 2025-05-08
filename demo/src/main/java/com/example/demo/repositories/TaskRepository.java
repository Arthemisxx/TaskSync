package com.example.demo.repositories;

import com.example.demo.entities.TaskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository  extends CrudRepository<TaskEntity, Long> {
    TaskEntity findTaskEntityById(Long id);
}
