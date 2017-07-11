package miniTwitter.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import miniTwitter.demo.models.Post;
import miniTwitter.demo.models.User;

public interface PostRepository extends CrudRepository<Post, Long>{

	List<Post> findByPostedBy_Id(long id);


}