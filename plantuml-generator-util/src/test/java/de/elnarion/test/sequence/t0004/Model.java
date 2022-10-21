package de.elnarion.test.sequence.t0004;

public class Model {

	View view;
	
	public void manipulate() {
		view.notifyView();
	}

	public String getData() {
		return "asdf";
		
	}

}
