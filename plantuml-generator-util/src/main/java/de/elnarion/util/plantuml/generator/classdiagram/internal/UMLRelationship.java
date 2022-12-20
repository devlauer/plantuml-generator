package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.util.List;

/**
 * The Class UMLRelationship encapsulates all information needed for creating a
 * diagram text for a uml relationship object..
 */
public class UMLRelationship implements PlantUMLDiagramElement {

	/** The from multiplicity. */
	private String fromMultiplicity;
	
	/** The to multiplicity. */
	private String toMultiplicity;
	
	/** The name. */
	private String name;
	
	/** The from name. */
	private String fromName;
	
	/** The to name. */
	private String toName;
	
	/** The relationshiptype. */
	private RelationshipType relationshiptype;
	
	/** The annotations. */
	private List<String> annotations;

	/**
	 * Instantiates a new UML relationship.
	 *
	 * @param paramFromMultiplicity - String - the from multiplicity of this
	 *                              association as String
	 * @param paramToMultiplicity   - String - the to multiplicity of this
	 *                              association as String
	 * @param paramName             - String - the name of this relationship
	 *                              (normally the field name in the referencing
	 *                              class object)
	 * @param paramFromName         - String - the class name where this
	 *                              relationship starts (from)
	 * @param paramToName           - String - the class name where this
	 *                              relationship ends (to)
	 * @param paramRelationshipType - {@link RelationshipType} - the type of this
	 *                              relationship
	 * @param paramAnnotations      - List - a list of annotations for this
	 *                              relationship
	 */
	public UMLRelationship(String paramFromMultiplicity, String paramToMultiplicity, String paramName,
			String paramFromName, String paramToName, RelationshipType paramRelationshipType,
			List<String> paramAnnotations) {
		fromMultiplicity = paramFromMultiplicity;
		toMultiplicity = paramToMultiplicity;
		name = paramName;
		fromName = paramFromName;
		toName = paramToName;
		relationshiptype = paramRelationshipType;
		annotations = paramAnnotations;
	}

	/**
	 * Gets the from multiplicity.
	 *
	 * @return String - the from multiplicity
	 */
	public String getFromMultiplicity() {
		return fromMultiplicity;
	}

	/**
	 * Gets the to multiplicity.
	 *
	 * @return String - the to multiplicity
	 */
	public String getToMultiplicity() {
		return toMultiplicity;
	}

	/**
	 * Gets the name.
	 *
	 * @return String - the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the from name.
	 *
	 * @return String - the from name
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * Gets the to name.
	 *
	 * @return String - the to name
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * Gets the relationshiptype.
	 *
	 * @return RelationshipType - the relationshiptype
	 */
	public RelationshipType getRelationshiptype() {
		return relationshiptype;
	}

	/**
	 * Gets the diagram text.
	 *
	 * @return the diagram text
	 */
	@Override
	public String getDiagramText() {
		StringBuilder builder = new StringBuilder();

		builder.append(fromName);
		builder.append(" ");
		if (fromMultiplicity != null) {
			builder.append("\"");
			builder.append(fromMultiplicity);
			builder.append("\" ");
		}
		switch (relationshiptype) {
		case AGGREGATION:
			builder.append("o--");
			break;
		case ASSOCIATION:
			builder.append("--");
			break;
		case COMPOSITION:
			builder.append("*--");
			break;
		case DIRECTED_ASSOCIATION:
			builder.append("-->");
			break;
		case INHERITANCE:
			builder.append("--|>");
			break;
		case REALIZATION:
			builder.append("..|>");
			break;
		default:
			break;
		}
		builder.append(" ");
		if (toMultiplicity != null) {
			builder.append("\"");
			builder.append(toMultiplicity);
			builder.append("\" ");
		}
		builder.append(" ");
		builder.append(toName);
		if(annotations!=null&&!annotations.isEmpty())
		{
			builder.append(" : ");
			for(String annotation:annotations)
			{
				builder.append(" ");
				builder.append(annotation);
			}
			if(name!=null)
			{
				builder.append("\\n");
				builder.append(name);
			}
		}
		else if (name != null) {
			builder.append(" : ");
			builder.append(name);
		}
		return builder.toString();
	}

	/**
	 * Gets the annotations.
	 *
	 * @return the annotations
	 */
	public List<String> getAnnotations() {
		return annotations;
	}

	/**
	 * Sets the annotations.
	 *
	 * @param annotations the new annotations
	 */
	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}

}
