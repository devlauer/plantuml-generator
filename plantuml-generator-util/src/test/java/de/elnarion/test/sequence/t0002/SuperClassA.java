package de.elnarion.test.sequence.t0003;


public class SuperClassA {
	
	private SuperClassB superB = new CallerClassB();

	public void testSuperClassSomething() {
		superB.testSuperClassBSomething();
		
	}

}
