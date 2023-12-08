package de.elnarion.test.domain.t0021jakarta;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Family {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int id;
	private String description;

	@OneToMany(mappedBy = "family")
	private final List<Person> members = new ArrayList<Person>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Person> getMembers() {
		return members;
	}

}
