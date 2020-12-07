# BMEnotes

Some screenshots of the web application:
<p float="left">
<img src="/src/main/resources/static/images/sign_in.png" width=49% height=49% />
<img src="/src/main/resources/static/images/sign_up.png" width=49% height=49% />
<img src="/src/main/resources/static/images/home.png" width=49% height=49% />
<img src="/src/main/resources/static/images/iframe.png" width=49% height=49% />
</p>

## How to start application

1. Start Mariadb server as follows:
	- (First time create system tables: run bin/mysql_install_db.exe)
	- To start MariaDB: run bin/mysqld.exe --console
2. Start HeidiSQL (to be able to see what is going on in your database)
3. Start http://localhost:8180/login

## Usage

Step by step tutorial:
1. See ‘How to start the application’ above
2. The server will direct you to the login page
	- Login with existing username and password combination
	- Create an account
3. If the login was successful, you will be directed to the main page.
4. To check user data, click on the user icon on the right
	- You can edit and delete your account here
5. To check the users subjects click on the icon on the left	
	- You can open/delete uploaded materials and add new ones
6. To add new material use the drag&drop icon and follow the instructions

## Implementation

### Database

The database was designed based on the entity-relationship model below. Current implementation relies on one-to-many relation between the tables. Each user has its own subjects (courses), which in turn have their own documents. In the future we plan to expand the database to many-to-many relations in order for the users to be able to browse subjects and documents uploaded by other users, thus enhancing networking (see section Further Development).
<div style="text-align:center"><img src="/src/main/resources/static/images/egyed-kapcsolat.png" width=90% height=90% /></div>
The implementation (ORM) was done in the Spring Boot framework with the help of JPA (Java Persistent API). The connfiguration is done for MariaDB and the HeidiSQL client has been used for supervision and testing purposes. CRUD methods are provided by the repositories for the entity classes.

### Backend

The application is based on Spring Boot with the Maven plugin. For user identification purposes Spring Boot Security is used, while the communication between backend and frontend relies on thymeleaf templates. The basic functionality of the application is incorporated in the controller class and some service classes building on the repositories. The controller handles the requests of the user . It invokes services and repositories in order to process business-related tasks. Results are then added to the model which will be forwarded to the view and processed by the thymeleaf parser. 

Without going through the apllication line-by-line, let's got through the basic functionalities of the application by checking out the controller class and its methods.

The showLogin method returns the login page. The user can switch also to the signup form using JavaScript (see Frontend).
```
@GetMapping("/login")
public String showLogin( Model model) {
	model.addAttribute("user", new User());
	
	return "login";
}
```

This returns the main page of the application. The method showHome gives the current user, its subjects and documents (from the database) to the view with the help of the model.
```
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
```

This method is invoked, when a new user tries to sign up. If the requirements are met, a new instance of the user entity class will be created and the login form will be shown.	
```	
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
```

If a user wants to add a new subject, the subjectSubmit method will find the current logged in user in the database and save a new subject in the corresponding table. Afterwards it redirects to the home page.
```
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
```

The user can upload documents for every subject. Method uploadToDB will invoke the saveFile method of the DocumentService class, which will add a new record to the Document table for the respective file name, type and content. Finally, the method redirects to the home page.
```
@PostMapping("/upload/db/{id}")
public String uploadToDB(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) {
	Subject subject = subjectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
	documentService.saveFile(file, subject);    	
	
	return "redirect:/";
}
```

The next method will delete the current user record from the database (along with the associated subjects and documents). It returns the login page.
```
@GetMapping("/deleteuser/{id}")
public String deleteUser(@PathVariable("id") long id) {
	User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
	userRepository.delete(user);

	return "redirect:/login";
}
```

This method will delete the subject with the given id from the database (along with the associated files).
```
@GetMapping("/deletesubject/{id}")
public String deleteSubject(@PathVariable("id") long id) {
	Subject subject = subjectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
	subjectRepository.delete(subject);
	
	return "redirect:/";
}
```

Delete the document having the given id:
```
@GetMapping("/deletedocument/{id}")
public String deleteDocument(@PathVariable("id") long id) {
	Document document = documentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid document Id:" + id));
	documentRepository.delete(document);
	
	return "redirect:/";
}
```
The last method receives a filename. It will try to find the document corresponding to the name and if it succeeds, it will return a response entity with the filename in the header and the file in its body (forcing the user's browser to try to display the file inline).
```
@GetMapping("/files/download/{fileName:.+}")
public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
	String fileBasePath = "src/main/resources/static/files/";	
	Path path = Paths.get(fileBasePath + fileName);
	Resource resource = null;
	try {
		resource = new UrlResource(path.toUri());
	} catch (MalformedURLException e) {
		e.printStackTrace();
	}
	return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType("application/pdf"))
			.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
			.body(resource);
	
}
```

### Frontend


## Further development 

1. Now each user has his own database with his files and profile. It would be useful if he could explore friends, classmates online and they could have shared files.
2. Now each user can add new subjects, but can’t explore courses from friends.
3. The user could see a calendar, where he could see the upcoming events, like conferences
4. There could be online tests for each subject, where the users could test their knowledge.

## Authors

The project was created by students of BME, Faculty of Mechanical Engineering, Mechatronics MSc for the subject "Webes alkalmazások fejlesztése", mentored by Dr. János Hamar.

Developers:
- Anna Bodonhelyi
- Márton Szép
- Márton Cserni
- Ábel Kocsis

## Licence

The used softwares were open source or licenced by BME for academic use.
