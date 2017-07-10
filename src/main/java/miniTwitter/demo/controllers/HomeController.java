package miniTwitter.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import miniTwitter.demo.models.User;
import miniTwitter.demo.services.UserService;
import miniTwitter.demo.validators.UserValidator;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;

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
        }
        return "newsfeed";
    }
    
    @RequestMapping("/logout")
    public String LoggingOut(){
    	return "landingpage";
    }

    public UserValidator getUserValidator() {
        return userValidator;
    }

    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }
}
