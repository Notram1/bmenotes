package webapp.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sid", nullable = false, updatable = false)
	private Long sid;
	
	@Column(nullable = false)
	private String subjectname;
	
	@ManyToOne (optional = false)
	@JoinColumn(name = "uid")
	private User user;
	
	@OneToMany(mappedBy="subject", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Document> documents;
	
	public Subject() {
	}
	
	public Subject(String subjectname) {
		super();
		this.subjectname = subjectname;
	}
	
	public Long getId() {
		return sid;
	}

	public void setId(Long sid) {
		this.sid = sid;
	}
	
	public String getSubjectname() {
		return subjectname;
	}
	
	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
