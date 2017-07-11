package miniTwitter.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import miniTwitter.demo.models.Post;
import miniTwitter.demo.models.User;
import miniTwitter.demo.repositories.PostRepository;
import miniTwitter.demo.repositories.UserRepository;
import miniTwitter.demo.services.UserService;
import miniTwitter.demo.validators.UserValidator;

import java.security.Principal;
import java.util.List;

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

    @RequestMapping("/")
    public String index(){
        return "landingpage";
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
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
            model.addAttribute("newpost", new Post());
        }
        return "newsfeed";
    }
    
    @RequestMapping("/logout")
    public String LoggingOut(){
    	return "landingpage";
    }
    
    @RequestMapping("/me")
    public String profile(Principal principal, Model model){
    	User user = userRepository.findByEmail(principal.getName());
    	List<Post> posts = postRepository.findByPostedBy_Id(user.getId());
    	
    	if(posts!=null){
    		model.addAttribute("myposts", posts);
    	}else{
    		model.addAttribute("message", "Send your first tweet!");
    	}
    	return "profile";
    }

    public UserValidator getUserValidator() {
        return userValidator;
    }

    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }
}
