package de.elnarion.util.plantuml.generator.classdiagram;

import java.util.Map;
import java.util.Map.Entry;

public class UMLStereotype {

	private String name;
	private Map<String, String> attributes;

	public UMLStereotype(String paramName, Map<String, String> paramAttributes) {
		setName(paramName);
		setAttributes(paramAttributes);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public String getDiagramText() {
		StringBuilder builder = new StringBuilder();
		builder.append("<<");
		builder.append(name);
		if (attributes != null && !attributes.isEmpty()) {
			builder.append(" (");
			boolean first = true;
			for (Entry<String, String> mapentry : attributes.entrySet()) {
				if(!first)
				{
					builder.append(",");
				}
				builder.append(mapentry.getKey());
				builder.append("=");
				builder.append(mapentry.getValue());
				first = false;
			}
			builder.append(")");
		}
		builder.append(">>");
		return builder.toString();
	}

}
