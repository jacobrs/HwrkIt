package app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name="Assignation")
public class Assignation
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="aid")
	private long id;

	@Column(name="userid")
	private long userID;

	@Column(name="classid")
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