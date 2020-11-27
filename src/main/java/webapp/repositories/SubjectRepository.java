package webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import webapp.domain.Subject;
import webapp.domain.User;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
//	Subject findBySubjectname(String subjectname);
	Subject findByUser(User user);
	
}
