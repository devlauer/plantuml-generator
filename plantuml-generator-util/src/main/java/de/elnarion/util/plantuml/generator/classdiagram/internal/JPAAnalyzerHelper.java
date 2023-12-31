package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Collections.sort;

/**
 * The type Jpa analyzer helper.
 */
class JPAAnalyzerHelper {

	/**
	 * Adds the JPA field annotations to list.
	 *
	 * @param field                  the field
	 * @param paramDeclaredMethods   the param declared methods
	 * @param destinationClassloader the destination classloader
	 * @return the list
	 */
	List<String> addJPAFieldAnnotationsToList(final java.lang.reflect.Field field, Method[] paramDeclaredMethods,
											  ClassLoader destinationClassloader) {
		List<String> annotationStringList = new LinkedList<>();
		if (destinationClassloader != null) {
			// javax.persistance
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.Column");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.Id");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.Transient");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.OneToOne");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.OneToMany");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.ManyToMany");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.ManyToOne");

			// jakarta.persistance
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"jakarta.persistence.Column");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"jakarta.persistence.Id");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"jakarta.persistence.Transient");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"jakarta.persistence.OneToOne");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"jakarta.persistence.OneToMany");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"jakarta.persistence.ManyToMany");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"jakarta.persistence.ManyToOne");
		}
		return annotationStringList;
	}

	/**
	 * Adds the JPA field annotation class to list.
	 *
	 * @param field                    the field
	 * @param paramDeclaredMethods     the param declared methods
	 * @param annotationStringList     the annotation string list
	 * @param destinationClassloader   the destination classloader
	 * @param paramAnnotationClassname the param annotation classname
	 */
	private void addJPAFieldAnnotationClassToList(final java.lang.reflect.Field field, Method[] paramDeclaredMethods, // NOSONAR
												  List<String> annotationStringList, ClassLoader destinationClassloader, String paramAnnotationClassname) {
		try {
			boolean foundAnnotation = false;
			Class<?> columnAnnotation = destinationClassloader.loadClass(paramAnnotationClassname);
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (columnAnnotation.isAssignableFrom(annotation.getClass())) {
					annotationStringList.add(getColumnAnnotationString(annotation, columnAnnotation.getSimpleName()));
					foundAnnotation = true;
				}
			}
			if (!foundAnnotation) {
				for (Method declaredMethod : paramDeclaredMethods) {
					if (MethodAnalyzerUtil.isGetterOrSetterMethod(declaredMethod, new Field[]{field})) {
						annotations = declaredMethod.getAnnotations();
						for (Annotation annotation : annotations) {
							if (columnAnnotation.isAssignableFrom(annotation.getClass())) {
								annotationStringList
										.add(getColumnAnnotationString(annotation, columnAnnotation.getSimpleName()));
							}
						}
					}
				}
			}
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			// ignore all exceptions
		}
	}

	/**
	 * Gets the column annotation string.
	 *
	 * @param annotation     the annotation
	 * @param annotationName the annotation name
	 * @return the column annotation string
	 */
	private String getColumnAnnotationString(Annotation annotation, String annotationName) {
		StringBuilder builder = new StringBuilder();
		builder.append("@");
		builder.append(annotationName);
		try {
			Method nameMethod = annotation.getClass().getMethod("name");
			builder.append("(\"");
			Object nameObject = nameMethod.invoke(annotation);
			if (nameObject instanceof String) {
				builder.append(nameObject);
			}
			builder.append("\")");
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
				 | InvocationTargetException e) {
			// ignore, method not found or not callable
		}
		return builder.toString();
	}

	/**
	 * Adds the JPA stereotype.
	 *
	 * @param paramClassObject       the param class object
	 * @param stereotypes            the stereotypes
	 * @param destinationClassloader the destination classloader
	 */
	protected void addJPAStereotype(final Class<?> paramClassObject, List<UMLStereotype> stereotypes, ClassLoader destinationClassloader) {
		if (destinationClassloader != null) {
			try {
				// javax.persistance
				addStereoTypesForAnnotationClass(paramClassObject, stereotypes, destinationClassloader,
						"javax.persistence.Entity", "Entity");
				addStereoTypesForAnnotationClass(paramClassObject, stereotypes, destinationClassloader,
						"javax.persistence.Table", "Table");
				addStereoTypesForAnnotationClass(paramClassObject, stereotypes, destinationClassloader,
						"javax.persistence.MappedSuperclass", "MappedSuperclass");

				// jakarta.persistance
				addStereoTypesForAnnotationClass(paramClassObject, stereotypes, destinationClassloader,
						"jakarta.persistence.Entity", "Entity");
				addStereoTypesForAnnotationClass(paramClassObject, stereotypes, destinationClassloader,
						"jakarta.persistence.Table", "Table");
				addStereoTypesForAnnotationClass(paramClassObject, stereotypes, destinationClassloader,
						"jakarta.persistence.MappedSuperclass", "MappedSuperclass");
			} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
				// ignore all exceptions
			}
		}
	}

	/**
	 * Adds the stereo types for annotation class.
	 *
	 * @param paramClassObject       the param class object
	 * @param stereotypes            the stereotypes
	 * @param destinationClassloader the destination classloader
	 * @param annotationClassName    the annotation class name
	 * @param annotationName         the annotation name
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void addStereoTypesForAnnotationClass(final Class<?> paramClassObject, List<UMLStereotype> stereotypes,
												  ClassLoader destinationClassloader, String annotationClassName, String annotationName)
			throws ClassNotFoundException {
		Class<?> tableAnnotation = destinationClassloader.loadClass(annotationClassName);
		Annotation[] annotations = paramClassObject.getAnnotations();
		for (Annotation annotation : annotations) {
			if (tableAnnotation.isAssignableFrom(annotation.getClass())) {
				addAnnotationStereotype(stereotypes, annotation, annotationName, destinationClassloader);
			}
		}
	}

	/**
	 * Adds the annotation stereotype.
	 *
	 * @param stereotypes            the stereotypes
	 * @param annotation             the annotation
	 * @param annotationName         the annotation name
	 * @param destinationClassloader the destination classloader
	 */
	private void addAnnotationStereotype(List<UMLStereotype> stereotypes, Annotation annotation, String annotationName,
										 ClassLoader destinationClassloader) {
		Map<String, List<String>> attributes = new TreeMap<>();
		// only for @Table
		if ("Table".equals(annotationName)) {
			addAttributeIfExists(annotation, attributes, "name");
			addAttributeIfExists(annotation, attributes, "schema");

			// javax.persistance
			addAttributeObjectListIfExists(annotation, attributes, "javax.persistence.Index", "indexes",
					destinationClassloader);
			addAttributeObjectListIfExists(annotation, attributes, "javax.persistence.UniqueConstraint",
					"uniqueConstraints", destinationClassloader);

			// jakarta.persistance
			addAttributeObjectListIfExists(annotation, attributes, "jakarta.persistence.Index", "indexes",
					destinationClassloader);
			addAttributeObjectListIfExists(annotation, attributes, "jakarta.persistence.UniqueConstraint",
					"uniqueConstraints", destinationClassloader);
		}
		UMLStereotype stereotype = new UMLStereotype(annotationName, attributes);
		stereotypes.add(stereotype);
	}

	/**
	 * Adds the attribute if exists.
	 *
	 * @param annotation the annotation
	 * @param attributes the attributes
	 * @param methodname the methodname
	 */
	private void addAttributeIfExists(Annotation annotation, Map<String, List<String>> attributes, String methodname) {
		try {
			Method nameMethod = annotation.getClass().getMethod(methodname);
			Object nameObject = nameMethod.invoke(annotation);
			if (nameObject instanceof String && !((String) nameObject).isEmpty()) {
				List<String> values = new ArrayList<>();
				values.add((String) nameObject);
				attributes.put(methodname, values);
			}
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
				 | InvocationTargetException e) {
			// ignore, because method is not found or can not be called or has illegal
			// arguments
		}
	}


	/**
	 * Adds the attribute object list if exists.
	 *
	 * @param annotation             the annotation
	 * @param attributes             the attributes
	 * @param annotationClassName    the annotation class name
	 * @param methodName             the method name
	 * @param destinationClassloader the destination classloader
	 */
	private void addAttributeObjectListIfExists(Annotation annotation, Map<String, List<String>> attributes,
												String annotationClassName, String methodName, ClassLoader destinationClassloader) {
		try {
			Method nameMethod = annotation.getClass().getMethod(methodName);
			Class<?> valueAnnotation = destinationClassloader.loadClass(annotationClassName);
			Object nameObject = nameMethod.invoke(annotation);
			if (nameObject != null && nameObject.getClass().isArray()
					&& nameObject.getClass().getComponentType().equals(valueAnnotation)) {
				List<String> values = addAnnotationArrayToAttributeList(valueAnnotation, nameObject);
				if (!values.isEmpty()) {
					sort(values);
					attributes.put(methodName, values);
				}
			}
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				 | ClassNotFoundException e) {
			// ignore, because method is not found or can not be called or has illegal
			// arguments
		}
	}

	/**
	 * Adds the annotation array to attribute list.
	 *
	 * @param valueAnnotation the value annotation
	 * @param nameObject      the name object
	 * @return the list
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private List<String> addAnnotationArrayToAttributeList(Class<?> valueAnnotation, Object nameObject)
			throws IllegalAccessException, InvocationTargetException {
		List<String> values = new ArrayList<>();
		int length = Array.getLength(nameObject);
		for (int i = 0; i < length; i++) {
			Object arrayElement = Array.get(nameObject, i);
			StringBuilder elementStringBuilder = new StringBuilder();
			Method[] methods = valueAnnotation.getDeclaredMethods();
			if (methods.length > 0) {
				addMethodArrayValueString(valueAnnotation, arrayElement, elementStringBuilder, methods);
			}
			values.add(elementStringBuilder.toString());
		}
		return values;
	}

	/**
	 * Adds the method array value string.
	 *
	 * @param valueAnnotation      the value annotation
	 * @param arrayElement         the array element
	 * @param elementStringBuilder the element string builder
	 * @param methods              the methods
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private void addMethodArrayValueString(Class<?> valueAnnotation, Object arrayElement,
										   StringBuilder elementStringBuilder, Method[] methods)
			throws IllegalAccessException, InvocationTargetException {
		elementStringBuilder.append(valueAnnotation.getSimpleName());
		elementStringBuilder.append(" (");
		List<String> methodAttributesString = new ArrayList<>();
		for (Method method : methods) {
			String name = method.getName();
			if (method.getParameterCount() == 0) {
				String methodValueString = createMethodValueString(arrayElement, method, name);
				if (!methodValueString.isEmpty()) {
					methodAttributesString.add(methodValueString);
				}
			}
		}
		sort(methodAttributesString);
		elementStringBuilder.append(String.join(",", methodAttributesString));
		elementStringBuilder.append(" )");
	}


	/**
	 * Creates the method value string.
	 *
	 * @param arrayElement the array element
	 * @param method       the method
	 * @param name         the name
	 * @return the string
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private String createMethodValueString(Object arrayElement, Method method, String name)
			throws IllegalAccessException, InvocationTargetException {
		Object result = method.invoke(arrayElement);
		StringBuilder elementStringBuilder = new StringBuilder();
		if ((result instanceof String && !((String) result).isEmpty())
				|| (!(result instanceof String) && result != null)) {
			elementStringBuilder.append(name);
			elementStringBuilder.append("=[");
			if (result.getClass().isArray()) {
				handleMethodArrayResultString(elementStringBuilder, result);
			} else {
				elementStringBuilder.append(result);
			}
			elementStringBuilder.append("]");

		}
		return elementStringBuilder.toString();
	}


	/**
	 * Handle method array result string.
	 *
	 * @param elementStringBuilder the element string builder
	 * @param result               the result
	 */
	private void handleMethodArrayResultString(StringBuilder elementStringBuilder, Object result) {
		int resultLength = Array.getLength(result);
		for (int j = 0; j < resultLength; j++) {
			if (j > 0)
				elementStringBuilder.append(",");
			elementStringBuilder.append(Array.get(result, j));
		}
	}
}
