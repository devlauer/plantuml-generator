package de.elnarion.util.plantuml.generator.classdiagram.internal;

import de.elnarion.util.plantuml.generator.classdiagram.config.VisibilityType;

/**
 * The Class PlantUMLUtil provides helper methods for generating the class
 * diagram.
 */
public class VisibilityUtil {

	/**
	 * Hide public default constructor because this class has only static methods
	 */
	private VisibilityUtil() {

	}

	/**
	 * Gets the visibility text for a {@link VisibilityType}.
	 *
	 * @param paramVisibility - {@link VisibilityType} - the visibility used for the
	 *                        conversion
	 * @return String - the visibility text
	 */
	public static String getVisibilityText(VisibilityType paramVisibility) {
		if (paramVisibility != null) {
			switch (paramVisibility) {
			case PRIVATE:
				return "-";
			case PACKAGE_PRIVATE:
				return "~";
			case PROTECTED:
				return "#";
			case PUBLIC:
				return "+";
			}
		}
		return "";
	}

}
