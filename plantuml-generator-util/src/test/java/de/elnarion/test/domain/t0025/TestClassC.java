package de.elnarion.test.domain.t0025;

/**
 * The Class TestClassC.
 */
public class TestClassC extends SuperClassC implements InterfaceC{

	private TestClassB fieldTestClassB;
	
	/**
	 * Gets the test class A.
	 *
	 * @param paramTestclassB the param testclass B
	 * @return the test class A
	 */
	public TestClassA getTestClassA(TestClassB paramTestclassB) {
		return null;
	}

	public TestClassB getFieldTestClassB() {
		return fieldTestClassB;
	}

	public void setFieldTestClassB(TestClassB fieldTestClassB) {
		this.fieldTestClassB = fieldTestClassB;
	}

}
