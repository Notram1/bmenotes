package webapp.repositories;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import webapp.domain.Subject;
import webapp.domain.User;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
//	Subject findBySubjectname(String subjectname);
	List<Subject> findAllByUser(User user);
	
}
