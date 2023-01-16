package de.elnarion.test.domain.t0025;

import java.util.List;

import de.elnarion.test.domain.t0024.ClassA;

/**
 * The Class TestClassB.
 */
public class TestClassB {

	private List<TestClassA> listTestClassA;
	
	@SuppressWarnings("unused")
	private ClassA internalField;
	
	/**
	 * Instantiates a new test class B.
	 *
	 * @param paramTestclA the param testcl A
	 */
	public TestClassB(TestClassA paramTestclA) {
		
	}

	public List<TestClassA> getListTestClassA() {
		return listTestClassA;
	}

	public void setListTestClassA(List<TestClassA> listTestClassA) {
		this.listTestClassA = listTestClassA;
	}

}
