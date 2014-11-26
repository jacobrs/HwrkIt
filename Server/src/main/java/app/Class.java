package app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name="Classes")
public class Class {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="cid")
	private long id;

	@Column(name="classname")
	private String className;

	@Column(name="teacherid")
	private long teacherID;
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setTeacherID(long teacherID) {
		this.teacherID = teacherID;
	}
	
	public long getTeacherID() {
		return teacherID;
	}
}