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

    @Column(name="starttime")
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

	public Date getStartTime() {
		return startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setTheClass(Class theClass) {
		this.theClass = theClass;
	}

	public Class getTheClass() {
		return this.theClass;
	}

	public void setTheOwner(User theOwner) {
		this.theOwner = theOwner;
	}

	public User getTheOwner() {
		return this.theOwner;
	}
}
