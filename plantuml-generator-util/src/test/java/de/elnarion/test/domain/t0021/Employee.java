package de.elnarion.test.domain.t0021;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

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