package webapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import webapp.domain.Document;
import webapp.domain.Subject;
import webapp.domain.User;
import webapp.repositories.DocumentRepository;
import webapp.repositories.SubjectRepository;
import webapp.repositories.UserRepository;
import webapp.services.DocumentService;

@Controller
public class AppController {
	
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final SubjectRepository subjectRepository;
	@Autowired
	private final DocumentRepository documentRepository;
	@Autowired 
	private DocumentService documentService;
	
	public AppController(UserRepository userRepository, SubjectRepository subjectRepository,  DocumentRepository documentRepository) {
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
		
	@GetMapping("/")
    public String showHome( Model model, Authentication authentication) {
		model.addAttribute("document", new Document());
		
		String username = authentication.getName();
    	User user = userRepository.findByUsername(username);
		model.addAttribute("user", user); 
				
		if(subjectRepository.count() > 0) {
			model.addAttribute("subjects", subjectRepository.findAllByUser(user));
		}
		
		if(documentRepository.count() > 0) {
			List<Document> docs = documentService.getFiles();
			model.addAttribute("documents", docs);
		}
				
		
    	return "index";
    }
	 
    	
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
    	return "redirect:/";
    }
    
    @PostMapping("/upload/db/{id}")
    public String uploadToDB(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) {
    	Subject subject = subjectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    	documentService.saveFile(file, subject);
    	    	    	
    	return "redirect:/";
    }
    
    @GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable long fileId){
		Document doc = documentService.getFile(fileId).get();
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(doc.getType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocname()+"\"")
				.body(new ByteArrayResource(doc.getFile()));
	}
    
    @GetMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
 
        return "redirect:/login";
    }
    
    @GetMapping("/deletesubject/{id}")
    public String deleteSubject(@PathVariable("id") long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        subjectRepository.delete(subject);
        
        return "redirect:/";
    }
    
    @GetMapping("/deletedocument/{id}")
    public String deleteDocument(@PathVariable("id") long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid document Id:" + id));
        documentRepository.delete(document);
        
        return "redirect:/";
    }

}
