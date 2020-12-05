package webapp.services;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import webapp.domain.Document;
import webapp.domain.Subject;
import webapp.repositories.DocumentRepository;

@Service
public class DocumentService {
	
	@Autowired
	private DocumentRepository docRepository;
	
	public Document saveFile(MultipartFile file, Subject subject) {
		  String docname = StringUtils.cleanPath(file.getOriginalFilename());
		  String fileBasePath = "src/main/resources/static/files/";		  
			Path path = Paths.get(fileBasePath + docname);
			try {
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/files/download/")
					.path(docname)
					.toUriString();
			
		  
		  try {
			  Document doc = new Document(docname,file.getContentType(),file.getBytes());
			  doc.setSubject(subject);
			  return docRepository.save(doc);
		  }
		  catch(Exception e) {
			  e.printStackTrace();
		  }
		  return null;
	  }
	  public Optional<Document> getFile(Long fileId) {
		  return docRepository.findById(fileId);
	  }
	  public List<Document> getFiles(){
		  return docRepository.findAll();
	  }

}
