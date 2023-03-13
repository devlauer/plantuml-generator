package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The Class UMLStereotype.
 */
public class UMLStereotype {

	/** The name. */
	private String name;

	/** The attributes. */
	private Map<String, List<String>> attributes;

	/**
	 * Instantiates a new UML stereotype.
	 *
	 * @param paramName       the param name
	 * @param paramAttributes the param attributes
	 */
	public UMLStereotype(String paramName, Map<String, List<String>> paramAttributes) {
		setName(paramName);
		setAttributes(paramAttributes);
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the attributes.
	 *
	 * @param attributes the attributes
	 */
	public void setAttributes(Map<String, List<String>> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the diagram text.
	 *
	 * @return the diagram text
	 */
	public String getDiagramText() {
		return "<<" +
				name +
				">>";
	}

	/**
	 * Checks for tagged values.
	 *
	 * @return true, if successful
	 */
	public boolean hasTaggedValues() {
		return (attributes != null && !attributes.isEmpty());
	}

	/**
	 * Gets the tagged value compartment.
	 *
	 * @return the tagged value compartment
	 */
	public String getTaggedValueCompartment() {
		StringBuilder taggedValueCompartment = new StringBuilder();
		if (attributes != null && !attributes.isEmpty()) {
			for (Entry<String, List<String>> mapentry : attributes.entrySet()) {
				taggedValueCompartment.append(System.lineSeparator());
				taggedValueCompartment.append(" {");
				taggedValueCompartment.append(name);
				String key = mapentry.getKey();
				taggedValueCompartment.append(Character.toUpperCase(key.charAt(0)));
				taggedValueCompartment.append(key.substring(1));
				taggedValueCompartment.append("=");
				List<String> values = mapentry.getValue();
				if (values != null && values.size() == 1) {
					taggedValueCompartment.append(values.get(0));
				} else if (values != null && !values.isEmpty()) {
					for (String value : values) {
						taggedValueCompartment.append("\\n\\t");
						taggedValueCompartment.append(value);
					}
					taggedValueCompartment.append("\\n");
				}
				taggedValueCompartment.append("}");
			}
		}
		return taggedValueCompartment.toString();
	}

}
