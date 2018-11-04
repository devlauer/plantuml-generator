package de.elnarion.util.plantuml.generator.classdiagram;

/**
 * The Enum RelationshipType defines all supported relationship types of this
 * utility.
 */
public enum RelationshipType {

	/** inheritance. */
	INHERITANCE,

	/** realization. */
	REALIZATION,

	/** composition (not used so far). */
	COMPOSITION,

	/** aggregation. */
	AGGREGATION,

	/** association. */
	ASSOCIATION,

	/** directed association. */
	DIRECTED_ASSOCIATION

}
