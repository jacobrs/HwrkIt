package app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;

@Entity
@Table(name="Assignation")
public class Assignation
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="aid")
	private long id;

	@ManyToOne
	@JoinColumn(name="userid")
	private User theUser;

	@ManyToOne
	@JoinColumn(name="classid")
	private Class theClass;
	
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