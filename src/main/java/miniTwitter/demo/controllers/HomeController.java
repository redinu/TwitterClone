package miniTwitter.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import miniTwitter.demo.models.Friendship;
import miniTwitter.demo.models.Photo;
import miniTwitter.demo.models.Post;
import miniTwitter.demo.models.User;
import miniTwitter.demo.repositories.FriendshipRepository;
import miniTwitter.demo.repositories.PhotoRepository;
import miniTwitter.demo.repositories.PostRepository;
import miniTwitter.demo.repositories.UserRepository;
import miniTwitter.demo.services.UserService;
import miniTwitter.demo.validators.UserValidator;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private FriendshipRepository friendshipRepository;
    
    @Autowired
    private PhotoRepository photoRepository;

    @RequestMapping("/")
    public String index(){
    	
    	return "loginpage";
    }
    
    @RequestMapping("/allposts")
    public String index(Model m){
    	
    	m.addAttribute("allPosts",postRepository.findAll());
    	return "newsfeed";
    }

    @RequestMapping("/login")
    public String login(){
        return "loginpage";
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        model.addAttribute("user", user);
        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return "registration";
        } else {
        	Photo p = photoRepository.findOne((long) 1);
        	user.setProfilePicture(p);
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
            model.addAttribute("newpost", new Post());
        }
        return "redirect:/login";
    }
    
    @RequestMapping("/logout")
    public String LoggingOut(){
    	return "landingpage";
    }
    
    @RequestMapping("/me")
    public String profile(Principal principal, Model model){
    	User user = userRepository.findByEmail(principal.getName());
    	List<Post> posts = postRepository.findByPostedBy_Id(user.getId());
    	List<Photo> pictures =  new ArrayList<Photo>();
    	List<Friendship> following = friendshipRepository.findByFollower_Id(user.getId());
    	List<Friendship> follower = friendshipRepository.findByFollowing_Id(user.getId());
    	pictures.addAll(photoRepository.findByUser_Id(user.getId()));
    	
    	model.addAttribute("allPosts", posts);
    	model.addAttribute("photos", pictures);
    	model.addAttribute("user", user);
    	model.addAttribute("following", following);
    	model.addAttribute("follower", follower);
    	return "profile";
    }
    
    @RequestMapping("/allusers")
    public String getallUsers( Model model){
    	
    	Iterable<User> users = userRepository.findAll();
    	model.addAttribute("everyUser", users);
    	
    	return "allusers";
    }
    
    @RequestMapping("/allusers/me")
    public String allUsers(Principal principal, Model model){
    	User u = userRepository.findByEmail(principal.getName());
    	List<Friendship> friends = friendshipRepository.findByFollower_Id(u.getId());
    	Iterable<User> users = userRepository.findAll();
    	Map<User, Boolean> allusers = new HashMap<User, Boolean>();
    	
    	for(User user: users){
    			Friendship fd = friendshipRepository.findByFollower_IdAndFollowing_Id(u.getId(), user.getId());
    			if(fd!=null){
    				allusers.put(user, fd.isConfirmed());
    			
    			}else{
    				fd = new Friendship();
    				fd.setConfirmed(false);
    				allusers.put(user, fd.isConfirmed());
    			}
    	}
    	
    	model.addAttribute("allUsers", allusers);
    	
    	return "allusers";
    }
    
    @RequestMapping("/following")
    public String listfollower(Principal principal, Model model){
    	
    	User user = userRepository.findByEmail(principal.getName());
    	
    	List<Friendship> friends = friendshipRepository.findByFollower_Id(user.getId());
    	
    	List<User> users = new ArrayList<User>();
    	
    	for(Friendship friend : friends){
    		
    		users.add(friend.getFollowing());
    	}
    	
    	model.addAttribute("followingUsers", users);
    	
    	return "allusers";
    }
    
    @RequestMapping("/follower")
    public String listfollowing(Principal principal, Model model){
    	
    	User user = userRepository.findByEmail(principal.getName());
    	
    	List<Friendship> friends = friendshipRepository.findByFollowing_Id(user.getId());
    	
    	Map<User, Boolean> users = new HashMap<User, Boolean>();
    	
    	for(Friendship friend : friends){
    		
    		users.put(friend.getFollower(), friend.isConfirmed());
    	}
    	
    	model.addAttribute("followers", users);
    	
    	return "allusers";
    }
    
    @RequestMapping("/follow/{id}")
    public String follow(@PathVariable("id") Long id, Principal principal, Model model){
    	User followed = userRepository.findOne(id);
    	User follower = userRepository.findByEmail(principal.getName());
    	Friendship friendship = friendshipRepository.findByFollower_IdAndFollowing_Id(follower.getId(),followed.getId());
    	
    	if(friendship==null){
    		friendship = new Friendship();
    		friendship.setFollower(follower);
        	friendship.setFollowing(followed);
        	friendship.setConfirmed(true);
        	friendshipRepository.save(friendship);
    	}
    	
    	return "redirect:/following";
    }
    
    
    
    public UserValidator getUserValidator() {
        return userValidator;
    }

    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }
}
