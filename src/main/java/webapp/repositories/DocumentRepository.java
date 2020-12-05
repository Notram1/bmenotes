package webapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import webapp.domain.Document;
import webapp.domain.Subject;
import webapp.domain.User;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
//	List<Document> findAllByUser(Subject subject);
//	Document setDocName(String docName);
	
}
