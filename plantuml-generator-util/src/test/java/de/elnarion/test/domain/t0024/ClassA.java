package de.elnarion.test.domain.t0024;

import java.util.List;
import java.util.Set;

/**
 * The Class ClassA.
 */
@AnnotationA
public class ClassA implements InterfaceA{

	/** The aggegate relationship B. */
	@SuppressWarnings("unused")
	private List<ClassB> aggegateRelationshipB;
	
	/** The aggegate relationship C. */
	@SuppressWarnings("unused")
	private Set<ClassC> aggegateRelationshipC;

}
