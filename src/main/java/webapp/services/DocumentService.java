package webapp.services;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import webapp.domain.Document;
import webapp.domain.Subject;
import webapp.repositories.DocumentRepository;

@Service
public class DocumentService {
	
	@Autowired
	private DocumentRepository docRepository;
	
	public Document saveFile(MultipartFile file, Subject subject) {
		  String docname = file.getOriginalFilename();
		  //String docname = "Filename";
		  
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
