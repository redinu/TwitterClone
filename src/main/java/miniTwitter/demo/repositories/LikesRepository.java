package miniTwitter.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import miniTwitter.demo.models.Likes;

public interface LikesRepository extends CrudRepository<Likes,Long> {

	Likes findByPost_PostIdAndUser_Id(Long id, Long userid);

	List<Likes> findByPost_PostId(long postId);

	List<Likes> findByPhoto_PhotoId(Long photoId);

	Likes findByPhoto_PhotoIdAndUser_Id(Long photoId, Long userId);

}
