package app;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.ManyToOne;

@Entity
@Table(name="time")
public class Time
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="hid")
    private long id;

    @Column(name="startime")
    private Date startTime;

    @Column(name="endtime")
    private Date endTime;

   	@ManyToOne(targetEntity=Class.class)
	@JoinColumn(name="classid")
    private Class theClass;

	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="ownerid")
	private User theOwner;
	
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
	
	public void setCourse(Class course) {
		this.theClass = course;
	}
	
	public Class getClassID() {
		return this.theClass;
	}
	
	public void setOwner(User owner) {
		this.theOwner = owner;
	}
	
	public User getOwner() {
		return this.theOwner;
	}
}
