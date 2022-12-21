package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.lang.reflect.Method;

class FieldAnalyzerUtil {

	private FieldAnalyzerUtil() {
		// just a static utility
	}
	
	/**
	 * Checks if a field has getter and setter methods.
	 *
	 * @param paramFieldName       - String - the field name
	 * @param paramDeclaredMethods - Method[] - the methods which should be used for
	 *                             the check
	 * @return true, if successful
	 */
	static boolean hasGetterAndSetterMethod(final String paramFieldName, final Method[] paramDeclaredMethods) {
		final String getterMethodName = "get" + paramFieldName;
		final String setterMethodName = "set" + paramFieldName;
		final String isMethodName = "is" + paramFieldName;
		boolean hasGetterMethod = false;
		boolean hasSetterMethod = false;
		if (paramDeclaredMethods != null) {
			for (final Method method : paramDeclaredMethods) {
				final String methodName = method.getName();
				if (methodName.equalsIgnoreCase(getterMethodName) || methodName.equalsIgnoreCase(isMethodName)) {
					hasGetterMethod = true;
				} else if (methodName.equalsIgnoreCase(setterMethodName)) {
					hasSetterMethod = true;
				}
				if (hasSetterMethod && hasGetterMethod) {
					return true;
				}
			}
		}
		return false;
	}

}
