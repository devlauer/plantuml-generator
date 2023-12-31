package de.elnarion.test.domain.t0021jakarta;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	private String firstName;
	private String lastName;

	private Family family;

	private String nonsenseField = "";

	private List<Job> jobList = new ArrayList<Job>();

	public String getId() {
		return id;
	}

	public void setId(String Id) {
		this.id = Id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	// Leave the standard column name of the table
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ManyToOne
	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
	}

	@Transient
	public String getNonsenseField() {
		return nonsenseField;
	}

	public void setNonsenseField(String nonsenseField) {
		this.nonsenseField = nonsenseField;
	}

	@OneToMany
	public List<Job> getJobList() {
		return this.jobList;
	}

	public void setJobList(List<Job> nickName) {
		this.jobList = nickName;
	}

}
