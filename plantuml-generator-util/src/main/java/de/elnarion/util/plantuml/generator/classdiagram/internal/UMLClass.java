package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.util.Comparator;
import java.util.List;

/**
 * The Class UMLClass encapsulates all information needed for creating a diagram
 * text for a uml class object.
 */
public class UMLClass implements PlantUMLDiagramElement {

	/** The name. */
	private final String name;

	/** The class type. */
	private final ClassType classType;

	/** The fields. */
	private final List<UMLField> fields;

	/** The methods. */
	private final List<UMLMethod> methods;

	/** The stereotypes. */
	private final List<UMLStereotype> stereotypes;

	/**
	 * Instantiates a new UML class.
	 *
	 * @param paramClassType   - {@link ClassType} - the class type
	 * @param paramFields      - List&lt;{@link UMLField}&gt; - the uml field
	 *                         information list
	 * @param paramMethods     - List&lt;{@link UMLMethod}&gt; - the uml method
	 *                         information list
	 * @param paramName        - String - the class name
	 * @param paramStereotypes - List&lt;UMLStereotype&gt; - the stereotypes of
	 *                         the class
	 */
	public UMLClass(ClassType paramClassType, List<UMLField> paramFields, List<UMLMethod> paramMethods,
					String paramName, List<UMLStereotype> paramStereotypes) {
		classType = paramClassType;
		name = paramName;
		fields = paramFields;
		methods = paramMethods;
		stereotypes = paramStereotypes;
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
	 * Gets the diagram text.
	 *
	 * @return the diagram text
	 */
	@Override
	public String getDiagramText() {
		StringBuilder builder = new StringBuilder();
		boolean isAnnotation = addClassType(builder);
		builder.append(name);
		addStereotypes(builder);
		if (!isAnnotation) {
			builder.append(" {");
			addStereotypeTaggedValues(builder);
			builder.append(System.lineSeparator());
			if (fields != null && !fields.isEmpty()) {
				fields.sort(Comparator.comparing(UMLField::getName));
				for (UMLField field : fields) {
					builder.append("\t");
					builder.append(field.getDiagramText());
					builder.append(System.lineSeparator());
				}
			}
			if (methods != null && !methods.isEmpty()) {
				methods.sort(new UMLMethodComparator());
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

	/**
	 * Adds the stereotypes.
	 *
	 * @param builder the builder
	 */
	private void addStereotypes(StringBuilder builder) {
		if (stereotypes != null) {
			for (UMLStereotype stereotype : stereotypes) {
				builder.append(" ");
				builder.append(stereotype.getDiagramText());
				builder.append(" ");
			}

		}
	}

	/**
	 * Adds the stereotype tagged values.
	 *
	 * @param builder the builder
	 */
	private void addStereotypeTaggedValues(StringBuilder builder) {
		if (stereotypes != null) {
			boolean addedTaggedValues = false;
			for (UMLStereotype stereotype : stereotypes) {

				if (stereotype.hasTaggedValues()) {
					builder.append(stereotype.getTaggedValueCompartment());
					addedTaggedValues = true;
				}
			}
			if (addedTaggedValues) {
				builder.append(System.lineSeparator());
				builder.append("--");
			}
		}
	}

	/**
	 * Adds the class type.
	 *
	 * @param builder the builder
	 * @return true, if successful
	 */
	private boolean addClassType(StringBuilder builder) {
		boolean isAnnotation = false;
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
		fields.add(paramField);
	}

	/**
	 * Adds the method.
	 *
	 * @param paramMethod the param method
	 */
	public void addMethod(UMLMethod paramMethod) {
		methods.add(paramMethod);
	}

}
