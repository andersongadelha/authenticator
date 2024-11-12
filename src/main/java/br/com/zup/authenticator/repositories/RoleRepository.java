package br.com.zup.authenticator.repositories;

import br.com.zup.authenticator.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
