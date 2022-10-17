package de.elnarion.test.sequence.t0001;

public class CallerA {
	
	CallerB b = new CallerB();
	
	public void callSomething() {
		b.callSomething();
		b.callSomethingElse();
	}

}
