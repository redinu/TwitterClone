package miniTwitter.demo.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.*;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;

   @NotEmpty
   private String content;
   
   @ManyToOne
   @JoinColumn(name="user_id")
   private User postedBy;
  
   @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
   private List<Likes> likes;
   
   
   private Date postedDate;


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public User getPostedBy() {
		return postedBy;
	}


	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}


	public Date getPostedDate() {
		return postedDate;
	}


	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}


	public long getPostId() {
		return postId;
	}


	public List<Likes> getLikes() {
		return likes;
	}


	public void setLikes(List<Likes> likes) {
		this.likes = likes;
	}


}