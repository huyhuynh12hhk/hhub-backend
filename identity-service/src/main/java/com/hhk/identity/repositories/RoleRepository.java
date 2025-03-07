package com.hhk.identity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhk.identity.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
