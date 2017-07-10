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
import java.util.Date;

import javax.validation.Valid;

@Controller
public class PostController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;


    
    @RequestMapping(value="/getpost", method = RequestMethod.GET)
    public String getPostForm(Model m){
    	
    	m.addAttribute("post",new Post());
    	return "postForm";
    	
    }
    
    @RequestMapping(value="/savepost", method = RequestMethod.POST)
    public String savePost(@Valid @ModelAttribute("post") Post post,BindingResult br,  Principal p, Model m){
    	
    	if (br.hasErrors()){
    	return "postForm";
    	}
    	
    	post.setPostedBy(userRepository.findByEmail(p.getName()));
    	post.setPostdate(new Date());
    	postRepository.save(post);
    	m.addAttribute("allPosts",postRepository.findAll());
    	return "newsfeed";
    }
    

    }