package de.elnarion.util.plantuml.generator.classdiagram;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UMLStereotype {

	private String name;
	private Map<String, List<String>> attributes;

	public UMLStereotype(String paramName, Map<String, List<String>> paramAttributes) {
		setName(paramName);
		setAttributes(paramAttributes);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, List<String>> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, List<String>> attributes) {
		this.attributes = attributes;
	}

	public String getDiagramText() {
		StringBuilder builder = new StringBuilder();
		builder.append("<<");
		builder.append(name);
		builder.append(">>");
		return builder.toString();
	}

	public boolean hasTaggedValues() {
		return (attributes != null && !attributes.isEmpty());
	}

	public String getTaggedValueCompartment() {
		StringBuilder taggedValueCompartment = new StringBuilder();
		if (attributes != null && !attributes.isEmpty()) {
			for (Entry<String, List<String>> mapentry : attributes.entrySet()) {
				taggedValueCompartment.append(System.lineSeparator());
				taggedValueCompartment.append(" {");
				taggedValueCompartment.append(name);
				String key = mapentry.getKey();
				taggedValueCompartment.append(Character.toUpperCase(key.charAt(0)) + key.substring(1));
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
