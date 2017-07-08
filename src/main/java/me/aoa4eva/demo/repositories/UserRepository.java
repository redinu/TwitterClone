package me.aoa4eva.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import me.aoa4eva.demo.models.User;

public interface UserRepository extends CrudRepository<User, Long>{

    User findByUsername(String username);

    User findByEmail(String email);

    Long countByEmail(String email);

    Long countByUsername(String username);
}

