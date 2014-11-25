package app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Class {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String className;
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