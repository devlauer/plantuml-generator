package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The type Javax Validation analyzer helper.
 */
class JavaxValidationAnalyzerHelper {

	/**
	 * Extract the cardinality from the supported annotations
	 *
	 * @param field 					the field
	 * @param paramDeclaredMethods		the param declared methods
	 * @param destinationClassloader	the destination classloader
	 * @return the cardinality String
	 */
	String extractCardinality(final Field field, Method[] paramDeclaredMethods,
							  ClassLoader destinationClassloader) {

		Annotation sizeAnnotation = findAnnotation(field, paramDeclaredMethods, destinationClassloader,
				"javax.validation.constraints.Size");
		if(null != sizeAnnotation) {
			Integer min = getAnnotationIntValue(sizeAnnotation, "min");
			Integer max = getAnnotationIntValue(sizeAnnotation, "max");

			StringBuilder cardinality = new StringBuilder();
			if(null != min) {
				cardinality.append(min);
			} else {
				cardinality.append("0");
			}

			cardinality.append("..");

			if(null != max && !max.equals(Integer.MAX_VALUE)) {
				cardinality.append(max);
			} else {
				cardinality.append("*");
			}

			return cardinality.toString();
		}

		Annotation notEmptyAnnotation = findAnnotation(field, paramDeclaredMethods, destinationClassloader,
				"javax.validation.constraints.NotEmpty");

		if(null != notEmptyAnnotation) {
			return "1..*";
		}

		return "0..*";
	}

	/**
	 * Extract the Integer value from a method of an annotation
	 *
	 * @param annotation	the annotation
	 * @param methodName 	the method name
	 * @return the Integer value, or null if not an Integer
	 */
	private Integer getAnnotationIntValue(Annotation annotation, String methodName) {
		try {
			Method nameMethod = annotation.getClass().getMethod(methodName);
			Object object = nameMethod.invoke(annotation);
			if(object instanceof Integer) {
				return (Integer) object;
			}
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
				 | InvocationTargetException e) {
			// ignore, because method is not found or can not be called or has illegal
			// arguments
		}
		return null;
	}

	/**
	 * Find a specific annotation on a field or getter/setter methods
	 *
	 * @param field						the field
	 * @param paramDeclaredMethods		the param declared methods
	 * @param destinationClassloader	the destination classloader
	 * @param paramAnnotationClassname	the param annotation classname
	 * @return the annotation if found; null otherwise
	 */
	private Annotation findAnnotation(final Field field, Method[] paramDeclaredMethods, // NOSONAR
									  ClassLoader destinationClassloader, String paramAnnotationClassname) {
		try {
			Class<?> columnAnnotation = destinationClassloader.loadClass(paramAnnotationClassname);
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (columnAnnotation.isAssignableFrom(annotation.getClass())) {
					return annotation;
				}
			}

			for (Method declaredMethod : paramDeclaredMethods) {
				if (MethodAnalyzerUtil.isGetterOrSetterMethod(declaredMethod, new Field[]{field})) {
					annotations = declaredMethod.getAnnotations();
					for (Annotation annotation : annotations) {
						if (columnAnnotation.isAssignableFrom(annotation.getClass())) {
							return annotation;
						}
					}
				}
			}
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			// ignore all exceptions
		}
		return null;
	}
}
