package com.example.demo.repositories;

import com.example.demo.entities.TaskEntity;
import com.example.demo.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository  extends CrudRepository<TaskEntity, Long> {
    TaskEntity findTaskEntityById(Long id);
    List<TaskEntity> findAllByCreator(UserEntity creator);
}
