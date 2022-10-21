package de.elnarion.test.sequence.t0004;

public class Controller {

	Model model;
	
	public void handleEvent(int i) {
		model.manipulate();
	}

}
