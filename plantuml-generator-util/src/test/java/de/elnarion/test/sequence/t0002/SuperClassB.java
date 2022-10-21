package de.elnarion.test.sequence.t0002;

public abstract class SuperClassB {

	public String testSuperClassBSomething() {
		testProtectedSomething();
		return "";
	}

	protected abstract void testProtectedSomething();

}
