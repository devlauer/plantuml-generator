package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The Class MethodAnalyzerUtil.
 */
class MethodAnalyzerUtil {
	
	/**
	 * Instantiates a new method analyzer util.
	 */
	private MethodAnalyzerUtil() {
		// just a static helper
	}

	/**
	 * Checks if a method is a getter or a setter method of a declared field.
	 *
	 * @param paramMethod         - Method - the method to be checked
	 * @param paramDeclaredFields - Field[] - the declared fields used for the check
	 * @return true, if is getter or setter method
	 */
	static boolean isGetterOrSetterMethod(final Method paramMethod, final Field[] paramDeclaredFields) {
		String methodName = paramMethod.getName();
		if (methodName.startsWith("get")) {
			methodName = methodName.substring(3, methodName.length());
		} else if (methodName.startsWith("is")) {
			methodName = methodName.substring(2, methodName.length());
		} else if (methodName.startsWith("set")) {
			methodName = methodName.substring(3, methodName.length());
		}
		for (final Field field : paramDeclaredFields) {
			final String fieldName = field.getName();
			if (fieldName.equalsIgnoreCase(methodName)) {
				return true;
			}
		}
		return false;
	}

}
