package app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Assignation
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long userID;
	private long classID;
	
	public void setUserID(long userID) {
		this.userID = userID;
	}
	
	public long getUserID() {
		return userID;
	}
	
	public void setClassID(long classID) {
		this.classID = classID;
	}
	
	public long getClassID() {
		return classID;
	}
}