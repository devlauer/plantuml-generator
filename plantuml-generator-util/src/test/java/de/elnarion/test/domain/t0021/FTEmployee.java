package de.elnarion.test.domain.t0021;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "FTEmployee", indexes = { @Index(columnList = "salaray,empId", unique = true),
		@Index(columnList = "version") }, uniqueConstraints = { @UniqueConstraint(columnNames = "empId"),
				@UniqueConstraint(columnNames = "empId,version") })
public class FTEmployee extends Employee {

	// Inherited empId field mapped to FTEMPLOYEE.EMPID
	// Inherited version field mapped to FTEMPLOYEE.VERSION
	// Inherited address field mapped to FTEMPLOYEE.ADDR fk

	// Defaults to FTEMPLOYEE.SALARY
	protected Integer salary;

	public FTEmployee() {
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer paramSalary) {
		this.salary = paramSalary;
	}
}