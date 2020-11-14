package webapp;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import webapp.domain.User;
import webapp.domain.UserRepository;

@Controller
public class AppController {
	
	private final UserRepository userRepository;
	public AppController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/login")
    public String showLogin() {
    	return "login";
    }
	
	@GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }
    
    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        //model.addAttribute("users", userRepository.findAll());       
        return "login";
    }

}
