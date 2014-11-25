package app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Time
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Date startTime;
	private Date endTime;
	private long classID;
	private long ownerID;
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getClassName() {
		return startTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setClassID(long classID) {
		this.classID = classID;
	}
	
	public long getClassID() {
		return classID;
	}
	
	public void setOwnerID(long ownerID) {
		this.ownerID = ownerID;
	}
	
	public long getOwnerID() {
		return ownerID;
	}
}