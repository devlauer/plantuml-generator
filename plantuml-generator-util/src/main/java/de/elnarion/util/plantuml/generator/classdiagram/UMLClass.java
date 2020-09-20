package de.elnarion.util.plantuml.generator.classdiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.elnarion.util.plantuml.generator.util.UMLMethodComparator;

/**
 * The Class UMLClass encapsulates all information needed for creating a diagram
 * text for a uml class object.
 */
public class UMLClass implements PlantUMLDiagramElement {

	private VisibilityType visibilityType;
	private String name;
	private ClassType classType;
	private List<UMLField> fields;
	private List<UMLMethod> methods;
	private List<UMLStereotype> stereotypes;

	/**
	 * Instantiates a new UML class.
	 *
	 * @param paramVisibilityType - {@link VisibilityType} - the visibility type
	 * @param paramClassType      - {@link ClassType} - the class type
	 * @param paramFields         - List&lt;{@link UMLField}&gt; - the uml field
	 *                            information list
	 * @param paramMethods        - List&lt;{@link UMLMethod}&gt; - the uml method
	 *                            information list
	 * @param paramName           - String - the class name
	 * @param paramStereotypes    - List&lt;UMLStereotype&gt; - the stereotypes of
	 *                            the class
	 */
	public UMLClass(VisibilityType paramVisibilityType, ClassType paramClassType, List<UMLField> paramFields,
			List<UMLMethod> paramMethods, String paramName, List<UMLStereotype> paramStereotypes) {
		visibilityType = paramVisibilityType;
		classType = paramClassType;
		name = paramName;
		fields = paramFields;
		methods = paramMethods;
		stereotypes = paramStereotypes;
	}

	/**
	 * Gets the stereotypes.
	 *
	 * @return List - the stereotypes
	 */
	public List<UMLStereotype> getStereotypes() {
		return stereotypes;
	}

	/**
	 * Gets the visibility type.
	 *
	 * @return VisibilityType - the visibility type
	 */
	public VisibilityType getVisibilityType() {
		return visibilityType;
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
	 * Gets the class type.
	 *
	 * @return ClassType - the class type
	 */
	public ClassType getClassType() {
		return classType;
	}

	/**
	 * Gets the fields.
	 *
	 * @return List - the fields
	 */
	public List<UMLField> getFields() {
		return fields;
	}

	/**
	 * Gets the methods.
	 *
	 * @return List - the methods
	 */
	public List<UMLMethod> getMethods() {
		return methods;
	}

	@Override
	public String getDiagramText() {
		boolean isAnnotation = false;
		StringBuilder builder = new StringBuilder();
		isAnnotation = addClassType(isAnnotation, builder);
		builder.append(name);
		addStereotypes(builder);
		if (!isAnnotation) {
			builder.append(" {");
			builder.append(System.lineSeparator());
			if (fields != null && !fields.isEmpty()) {
				Collections.sort(fields, new Comparator<UMLField>() {
					@Override
					public int compare(UMLField o1, UMLField o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
				for (UMLField field : fields) {
					builder.append("\t");
					builder.append(field.getDiagramText());
					builder.append(System.lineSeparator());
				}
			}
			if (methods != null && !methods.isEmpty()) {
				Collections.sort(methods, new UMLMethodComparator());
				for (UMLMethod method : methods) {
					builder.append("\t");
					builder.append(method.getDiagramText());
					builder.append(System.lineSeparator());
				}
			}
			builder.append("}");
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}

	private void addStereotypes(StringBuilder builder) {
		if (stereotypes != null) {
			for (UMLStereotype stereotype : stereotypes) {
				builder.append(" ");
				builder.append(stereotype.getDiagramText());
				builder.append(" ");
			}
		}
	}

	private boolean addClassType(boolean isAnnotation, StringBuilder builder) {
		switch (classType) {
		case ABSTRACT_CLASS:
			builder.append("abstract class ");
			break;
		case ANNOTATION:
			builder.append("annotation ");
			isAnnotation = true;
			break;
		case CLASS:
			builder.append("class ");
			break;
		case ENUM:
			builder.append("enum ");
			break;
		case INTERFACE:
			builder.append("interface ");
			break;
		default:
			break;
		}
		return isAnnotation;
	}

	/**
	 * Adds the field.
	 *
	 * @param paramField the param field
	 */
	public void addField(UMLField paramField) {
		if (fields == null) {
			fields = new ArrayList<>();
		}
		fields.add(paramField);
	}

	/**
	 * Adds the method.
	 *
	 * @param paramMethod the param method
	 */
	public void addMethod(UMLMethod paramMethod) {
		if (methods == null) {
			methods = new ArrayList<>();
		}
		methods.add(paramMethod);
	}

}
