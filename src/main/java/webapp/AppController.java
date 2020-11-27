package webapp;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import webapp.domain.Document;
import webapp.domain.Subject;
import webapp.domain.User;
import webapp.repositories.DocumentRepository;
import webapp.repositories.SubjectRepository;
import webapp.repositories.UserRepository;

@Controller
public class AppController {
	
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final SubjectRepository subjectRepository;
	@Autowired
	private final DocumentRepository documentRepository;
	
	public AppController(UserRepository userRepository, SubjectRepository subjectRepository, DocumentRepository documentRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.documentRepository = documentRepository;
    }
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/login")
    public String showLogin( Model model) {
		model.addAttribute("user", new User());

    	return "login";
    }
		
	@GetMapping("/home")
    public String showHome( Model model, Authentication authentication) {
		String username = authentication.getName();
    	User user = userRepository.findByUsername(username);
		model.addAttribute("user", user); 
		
		
		if(subjectRepository.count() > 0) {
			model.addAttribute("subjects", subjectRepository.findByUser(user));
		}
		
    	return "index";
    }
	
//	@GetMapping("/download/{fileName:.+}/db")
//	public ResponseEntity downloadFromDB(@PathVariable String fileName) {
//		Document document = documentRepository.findByDocName(fileName);
//		return ResponseEntity.ok()
//				.contentType(MediaType.parseMediaType(contentType))
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//				.body(document.getFile());
//	}
    
    	
	@PostMapping("/adduser")
    public String addUser(@Validated User user, BindingResult result) {
        if (result.hasErrors()) {
            return "login";
        }
        
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "login";
    }    
    
    @PostMapping("/addsubject")
    public String subjectSubmit(@Validated @ModelAttribute Subject subject, BindingResult result, Authentication authentication) {
    	 if (result.hasErrors()) {
             return "index";
         }
    	
    	String username = authentication.getName();
    	User user = userRepository.findByUsername(username);
    	subject.setUser(user);    	
    	subjectRepository.save(subject);
    	return "index";
    }
    
    @PostMapping("/upload/db")
    public ResponseEntity uploadToDB(@RequestParam("file") MultipartFile file, Document document) {
    	Document doc = new Document();
    	String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    	doc.setDocname(fileName);
    	try {
    		doc.setFile(file.getBytes());
    	} catch (IOException e) {
    		e.printStackTrace();
    	} 
    	documentRepository.save(doc);
    	String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
    			.path("/files/download/")
    			.path(fileName).path("/db")
    			.toUriString();
    	return ResponseEntity.ok(fileDownloadUri);
    }

}
