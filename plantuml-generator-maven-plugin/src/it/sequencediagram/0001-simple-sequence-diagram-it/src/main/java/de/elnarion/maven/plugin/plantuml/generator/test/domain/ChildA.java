package de.elnarion.maven.plugin.plantuml.generator.test.domain;

/**
 * The Class ChildA.
 */
public class ChildA extends BaseAbstractClass {

	ChildB childB;
	
	public void doSomethingSpecial() {
		childB.getUtil();
		doSomethingWithReturnValue();
		doSomethingWithParameter("");
	}
	

}
