package com.example.demo.repositories;

import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.models.UserRoles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity getUserEntityById(Long id);
    List<UserEntity> findAll();
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByRole(UserRoles role);
    List<UserEntity> findAllByTeamsIs(TeamEntity team);
}
