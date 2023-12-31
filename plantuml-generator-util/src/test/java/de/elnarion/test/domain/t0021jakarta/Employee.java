package de.elnarion.test.domain.t0021jakarta;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public class Employee {

	@Id
	protected Integer empId;
	@Version
	protected Integer version;
	@ManyToOne
	@JoinColumn(name = "ADDR")
	protected Address address;

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer paramId) {
		empId = paramId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address paramAddress) {
		address = paramAddress;
	}
}
