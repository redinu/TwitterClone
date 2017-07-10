package miniTwitter.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import miniTwitter.demo.models.Post;
import miniTwitter.demo.models.User;

public interface PostRepository extends CrudRepository<Post, Long>{


}