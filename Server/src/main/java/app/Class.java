package app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Table(name="classes")
public class Class {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@OneToMany(mappedBy="theClass")
	@Column(name="classid")
	private long id;

	@Column(name="classname")
	private String className;

	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="teacherid", referencedColumnName="uid")
	private User theTeacher;

	@OneToMany(mappedBy="theClass")
	private List<Time> times;

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setTheTeacher(User teacher) {
		this.theTeacher = teacher;
	}

	public User getTheTeacher() {
		return theTeacher;
	}
}