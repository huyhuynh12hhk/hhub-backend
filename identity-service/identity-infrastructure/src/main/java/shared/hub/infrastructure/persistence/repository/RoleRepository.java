package shared.hub.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shared.hub.domain.model.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
