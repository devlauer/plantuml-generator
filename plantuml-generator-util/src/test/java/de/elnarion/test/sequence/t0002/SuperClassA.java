package de.elnarion.test.sequence.t0002;


public class SuperClassA {
	
	private SuperClassB superB = new CallerClassB();

	public void testSuperClassSomething() {
		superB.testSuperClassBSomething();
		
	}

}
