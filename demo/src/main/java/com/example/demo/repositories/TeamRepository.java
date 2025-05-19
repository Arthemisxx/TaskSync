package com.example.demo.repositories;

import com.example.demo.entities.TeamEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends CrudRepository<TeamEntity, Long> {
    TeamEntity findTeamEntityById(Long id);
    TeamEntity getTeamEntityById(Long id);
}
