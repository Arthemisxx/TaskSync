package com.example.demo.repositories;

import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TeamRepository extends CrudRepository<TeamEntity, Long> {
    TeamEntity findTeamEntityById(Long id);
    TeamEntity getTeamEntityById(Long id);
    List<TeamEntity> findAllByUsersIs(Set<UserEntity> users);
}
