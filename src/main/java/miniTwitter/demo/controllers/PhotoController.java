package miniTwitter.demo.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.utils.ObjectUtils;

import miniTwitter.demo.configs.CloudinaryConfig;
import miniTwitter.demo.models.Friendship;
import miniTwitter.demo.models.Likes;
import miniTwitter.demo.models.Photo;
import miniTwitter.demo.models.Post;
import miniTwitter.demo.models.User;
import miniTwitter.demo.repositories.FriendshipRepository;
import miniTwitter.demo.repositories.LikesRepository;
import miniTwitter.demo.repositories.PhotoRepository;
import miniTwitter.demo.repositories.PostRepository;
import miniTwitter.demo.repositories.UserRepository;



@Controller
public class PhotoController {
		
	@Autowired
    CloudinaryConfig cloudc;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PhotoRepository photoRepository;
    
    @Autowired
    private FriendshipRepository friendshipRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private LikesRepository likesRepository;
    
	
    	@GetMapping("/upload")
	    public String uploadForm(){
	        return "upload";
	    }
	   
	   	@GetMapping("/uploadProfile")
	    public String uploadProfileForm(){
	        return "uploadprofile";
	    }

	    @PostMapping("/upload")
	    public String singleImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,Principal p, Model model){

	        if (file.isEmpty()){
	            model.addAttribute("message","Please select a file to upload");
	            return "upload";
	        }

	        try {
	            Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));

	            model.addAttribute("message",
	                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
	            
	            String filename = uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
	            
	           
	            model.addAttribute("imageurl", uploadResult.get("url"));
	            model.addAttribute("imagename", filename);
	            
	        } catch (IOException e){
	            e.printStackTrace();
	            model.addAttribute("message", "Sorry I can't upload that!");
	        }
	        return "filter";
	    }
	    
	    @PostMapping("/uploadProfile")
	    public String profileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,Principal p, Model model){

	        if (file.isEmpty()){
	            redirectAttributes.addFlashAttribute("message","Please select a file to upload");
	            return "redirect:uploadStatus";
	        }

	        try {
	            Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));

	            model.addAttribute("message",
	                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
	            
	            String filename = uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
	            
	            User user = userRepository.findByEmail(p.getName());
	            List<Friendship> following = friendshipRepository.findByFollower_Id(user.getId());
	        	List<Friendship> follower = friendshipRepository.findByFollowing_Id(user.getId());
	        	
	            Photo photo = new Photo();
	            photo.setImage(cloudc.createautoUrl(filename, 150, 150));
	            photo.setFileName(filename);
	            photo.setCreatedAt(new Date());
	            photo.setUser(user);
	            photoRepository.save(photo);
	            
	            user.setProfilePicture(photo);
	            userRepository.save(user);
	            
	            model.addAttribute("imageurl", cloudc.createautoUrl(filename, 150, 150));
	            model.addAttribute("user", user);
	            model.addAttribute("imagename", filename);
	            model.addAttribute("following", following);
	        	model.addAttribute("follower", follower);
	            
	        } catch (IOException e){
	            e.printStackTrace();
	            model.addAttribute("message", "Sorry I can't upload that!");
	        }
	        return "profile";
	    }
	    
	    @RequestMapping("/filter")
	    public String filter(String imagename, int width, int height, String action,String filter,Principal principal, Model model){
	    	
	    	if(width==0 ){
	    		width=250;
	    	}
	    	if(height==0){
	    		height=250;
	    	}
	    	
	    	model.addAttribute("sizedimageurl", cloudc.createUrl(imagename, width, height, action, filter));
	    	model.addAttribute("imagename", imagename);
	    	 
	    	return "filter";
	    }
	    
	    
	    @RequestMapping(path="/save" , method=RequestMethod.POST)
	    public String save(String sizedimageurl, String imagename, Principal principal, Model model){
	    	
	    	Photo photo = new Photo();
            photo.setImage(sizedimageurl);
            photo.setFileName(imagename);
            photo.setCreatedAt(new Date());
            User user = userRepository.findByEmail(principal.getName());
            photo.setUser(user);
            photoRepository.save(photo);
	    	 
	    	return "redirect:/newsfeed";
	    }
	    
	    @RequestMapping(value="/photo/{id}", method = RequestMethod.GET)
	    public String savePost(@PathVariable(value="id") Long id,  Principal p, Model m){
	    	
	    	List<Post> post = postRepository.findByPostedBy_Id(id);
	    	List<Photo> photos = photoRepository.findByUser_Id(id);
	    	User user = userRepository.findOne(id);
	    	List<Friendship> following = friendshipRepository.findByFollower_Id(user.getId());
	    	List<Friendship> follower = friendshipRepository.findByFollowing_Id(user.getId());
	    	
	    	m.addAttribute("allPosts",post);
	    	m.addAttribute("photos", photos);
	    	m.addAttribute("user", user);
	    	m.addAttribute("following", following);
	    	m.addAttribute("follower", follower);
	    	return "profile";
	    }
	    
	    
	    @RequestMapping(path="/getphotos")
	    public String getGallery(Principal p, Model model){
	    	
	    	User user = userRepository.findByEmail(p.getName());
	    	
	    	List<Photo> pictures = photoRepository.findByUser_Id(user.getId());
	    	
	    	model.addAttribute("user", user);
	    	model.addAttribute("photos", pictures);
	  
	    	return "newsfeed";
	    }
	    
	    @RequestMapping(path="/delete/{photoId}")
	    public String deletePhoto(@PathVariable Long photoId, Principal p, Model model){
	    	
	    	User user = userRepository.findByEmail(p.getName());
	    	Photo photo = photoRepository.findOne(photoId);
	    	if(photo.getUser() == user){
		    	
	    		photoRepository.delete(photo);
		    	
		    } 
	    	
	    	return "redirect:/newsfeed";
	    }
	    
	    @RequestMapping(value="/like/photo/{id}", method = RequestMethod.POST)
	    public String likePhoto(@PathVariable(value="id") Long id,  Principal p, Model m){
	    	
	    	Photo photo = photoRepository.findOne(id);
	    	User user = userRepository.findByEmail(p.getName());
	    	
	    	Likes like = likesRepository.findByPhoto_PhotoIdAndUser_Id(id,user.getId());
	    	if(like==null){
	    		like =  new Likes();
	    		like.setUser(user);
	        	like.setPhoto(photo);
	        	likesRepository.save(like);
	    	}
	    	
	    	return "redirect:/newsfeed";
	    }
	    
	    @RequestMapping(value="/likedby/photo/{id}")
	    public String findlikedBy(@PathVariable(value="id") Long id,  Principal p, RedirectAttributes redirectAttributes){
	    	
	    	Photo photo = photoRepository.findOne(id);
	    	
	    	List<Likes> likes = likesRepository.findByPhoto_PhotoId(photo.getPhotoId());
	    	photo.setLikes(likes) ;
	    	redirectAttributes.addFlashAttribute("photodetail", photo);
	    	
	    	return "redirect:/newsfeed";
	    }
}
