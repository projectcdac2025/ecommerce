package com.yourapp.repository;

import com.yourapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);//Used to fetch roles like ROLE_USER or ROLE_ADMIN.
}
