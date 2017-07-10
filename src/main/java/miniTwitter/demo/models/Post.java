package miniTwitter.demo.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.*;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "userData")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long post_id;

   @NotEmpty
   private String content;
   
   @ManyToOne
   @JoinColumn(name="user_id")
   private User postedBy;
   
   
	private Date postdate;


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


	public Date getPostdate() {
		return postdate;
	}


	public void setPostdate(Date postdate) {
		this.postdate = postdate;
	}


	public long getPost_id() {
		return post_id;
	}

}