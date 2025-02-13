package com.justme8code.techtide.repositories;

import com.justme8code.techtide.models.Role;
import com.justme8code.techtide.util.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findRoleByUserRole(UserRole userRole);
}