package de.elnarion.test.domain.t0021;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TABLENAME", schema = "SCHEMA")
public class Todo {

	@Id
	@Column(name = "IDX")
	private Long id;
	@Column(name = "DESCR")
	private String description;
	
	@Enumerated
	@Column(name="STATE")
	private TodoStateEnum state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TodoStateEnum getState() {
		return state;
	}

	public void setState(TodoStateEnum state) {
		this.state = state;
	}

}
