package miniTwitter.demo.models;


import javax.persistence.*;
import javax.persistence.Id;


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