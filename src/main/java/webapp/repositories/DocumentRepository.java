package webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import webapp.domain.Document;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
//	Document findByDocName(String docName);
//	Document setDocName(String docName);
	
}
