package app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(name="assignations")
public class Assignation
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="aid")
	private long id;

	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="uid")
	private User theUser;

	@ManyToOne(targetEntity=Class.class)
	@JoinColumn(name="classid")
	private Class theClass;


	public void setTheUser(User myUser) {
		this.theUser = myUser;
	}

	public User getTheUser() {
		return this.theUser;
	}

	public void setTheClass(Class myClass) {
		this.theClass = myClass;
	}

	public Class getTheClass() {
		return this.theClass;
	}
}