package com.justme8code.techtide.repositories;

import com.justme8code.techtide.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsernameEqualsIgnoreCaseOrIdEqualsIgnoreCase(String username, String id);

    Optional<User> findByUsername(String admin);
}