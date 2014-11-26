package app;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name="Time")
public class Time
{

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="hid")
        private long id;

        @Column(name="starttime")
        private Date startTime;

        @Column(name="endtime")
        private Date endTime;

        @Column(name="classid")
        private long classID;

        @Column(name="ownerid")
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
