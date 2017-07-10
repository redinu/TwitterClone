package miniTwitter.demo.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.*;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.Collection;

@Entity

public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long friendship_id;
    
   
    @ManyToOne
    @JoinColumn(name="follower_id")
    private User follower;
    @ManyToOne
    @JoinColumn(name="following_id")
    private User following;
	public User getFollower() {
		return follower;
	}
	public void setFollower(User follower) {
		this.follower = follower;
	}
	public User getFollowing() {
		return following;
	}
	public void setFollowing(User following) {
		this.following = following;
	}
	public long getFriendship_id() {
		return friendship_id;
	}

}