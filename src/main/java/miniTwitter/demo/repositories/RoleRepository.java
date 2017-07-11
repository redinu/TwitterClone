package miniTwitter.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import miniTwitter.demo.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{
    Role findByRole(String role);
}
