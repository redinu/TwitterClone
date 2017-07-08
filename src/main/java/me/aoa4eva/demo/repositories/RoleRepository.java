package me.aoa4eva.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import me.aoa4eva.demo.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{
    Role findByRole(String role);
}
