package de.elnarion.test.sequence.t0001;

public class CallerB {

	public void callSomething() {
		privateMethodCall();
		protectedMethodCall();
	}
	
	private void protectedMethodCall() {
		
	}

	private void privateMethodCall() {
		
	}

	public String callSomethingElse() {
		return "";
	}
}
