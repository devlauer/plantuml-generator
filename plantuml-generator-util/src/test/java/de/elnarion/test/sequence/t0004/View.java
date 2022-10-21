package de.elnarion.test.sequence.t0004;

public class View {

	private Controller controller;
	private Model model;
	
	public void interact(String string) {
				controller.handleEvent(222);
	}

	public void notifyView() {
		model.getData();
		
	}

}
