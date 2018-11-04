package de.elnarion.util.plantuml.generator.classdiagram;

import de.elnarion.util.plantuml.generator.PlantUMLUtil;

/**
 * The Class UMLField encapsulates all information needed for creating a diagram
 * text for a uml field object.
 */
public class UMLField implements PlantUMLDiagramElement {

	private String type;
	private String name;
	private ClassifierType classifierType;
	private VisibilityType visibilityType;

	/**
	 * Instantiates a new uml field.
	 *
	 * @param paramClassifierType - {@link ClassifierType} - the classifier type
	 * @param paramVisibilityType - {@link VisibilityType} - the visibility type
	 * @param paramName           - String - the name
	 * @param paramType           - String - the type name
	 */
	public UMLField(ClassifierType paramClassifierType, VisibilityType paramVisibilityType, String paramName,
			String paramType) {
		type = paramType;
		name = paramName;
		classifierType = paramClassifierType;
		visibilityType = paramVisibilityType;
	}

	/**
	 * Gets the type.
	 *
	 * @return String - the type
	 */
	public String getType() {
		return type;
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
	 * Gets the classifier type.
	 *
	 * @return ClassifierType - the classifier type
	 */
	public ClassifierType getClassifierType() {
		return classifierType;
	}

	/**
	 * Gets the visibility type.
	 *
	 * @return VisibilityType - the visibility type
	 */
	public VisibilityType getVisibilityType() {
		return visibilityType;
	}

	@Override
	public String getDiagramText() {
		StringBuilder builder = new StringBuilder();
		builder.append("{field} ");
		if (classifierType != null && classifierType == ClassifierType.STATIC) {
			builder.append("{static} ");
		}
		builder.append(PlantUMLUtil.getVisibilityText(visibilityType));
		builder.append(name);
		if (type != null) {
			builder.append(" : ");
			builder.append(type);
		}
		return builder.toString();
	}

}
