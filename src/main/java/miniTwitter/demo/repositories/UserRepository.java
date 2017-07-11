package miniTwitter.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import miniTwitter.demo.models.User;

public interface UserRepository extends CrudRepository<User, Long>{

    User findByEmail(String email);

    Long countByEmail(String email);

}

