package webapp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Document {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fid", nullable = false, updatable = false)
    private Long fid;
	
	@Column(nullable = false)
    private String docname;
	
	@Column
	private String type;
	
	@Column(nullable = false) 
    @Lob
    private byte[] file;
	
	@ManyToOne (optional = false)
	@JoinColumn(name = "sid")
	private Subject subject;
	
	public Document() {	
	}
	
	public Document(String docname, String type, byte[] file) {
		super();
		this.docname = docname;
		this.type = type;
		this.file = file;
	}
	
	public Long getId() {
		return fid;
	}

	public void setId(Long fid) {
		this.fid = fid;
	}
	
	public String getDocname() {
		return docname;
	}
	
	public void setDocname(String docname) {
		this.docname = docname;
	}
	
	public String getType() {
	    return type;
	}
	
	public void setType(String type) {
	    this.type = type;
	}
	
	
	public Subject getSubject() {
		return subject;
	}
	
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public byte[] getFile() {
		return file;
	}
	
	public void setFile(byte[] file) {
		this.file = file;
	}

}
