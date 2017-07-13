package miniTwitter.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import miniTwitter.demo.models.Friendship;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

	Friendship findByFollower_IdAndFollowing_Id(long id, long id2);

	List<Friendship> findByFollower_Id(long id);
	
	List<Friendship> findByFollowing_Id(long id);

}
