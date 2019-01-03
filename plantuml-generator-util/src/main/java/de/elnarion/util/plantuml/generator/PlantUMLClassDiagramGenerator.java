package de.elnarion.util.plantuml.generator;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import de.elnarion.util.plantuml.generator.classdiagram.ClassType;
import de.elnarion.util.plantuml.generator.classdiagram.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.RelationshipType;
import de.elnarion.util.plantuml.generator.classdiagram.UMLClass;
import de.elnarion.util.plantuml.generator.classdiagram.UMLField;
import de.elnarion.util.plantuml.generator.classdiagram.UMLMethod;
import de.elnarion.util.plantuml.generator.classdiagram.UMLRelationship;
import de.elnarion.util.plantuml.generator.classdiagram.VisibilityType;

/**
 * This class provides the ability to generate a PlantUML class diagram out of a
 * list of package names. Therefore this class scans directories and jars for
 * all classes contained directly in these packages and generates a class
 * diagram out of them via reflection
 * 
 * To be able to get the right classes you have to provide the necessary
 * ClassLoader, which is able to load these classes.
 * 
 * Currently this generator supports the following class types:
 * <ul>
 * <li>class</li>
 * <li>abstract class</li>
 * <li>annotation</li>
 * <li>enum</li>
 * <li>interface</li>
 * </ul>
 * 
 * with some restrictions:
 * 
 * The type annotation does not contain any further information than its name
 * and no Annotation relationships are included in the diagram.
 * 
 * The type enum contains only the constants.
 * 
 * 
 * All other types contain field and method informations.
 * 
 * Relationships are included for
 * <ul>
 * <li>inheritance</li>
 * <li>realization</li>
 * <li>aggregation (only for List and Set types)</li>
 * <li>usage (only direct usage via field)
 * </ul>
 * 
 * You can hide all fields via hideFields parameter or you can hide all methods
 * via hideMethods parameter.
 * 
 * If you want to hide a list of classes you have to provide a String List with
 * full qualified class names.
 */
public class PlantUMLClassDiagramGenerator {

	private static final String CLASS_ENDING = ".class";
	private ClassLoader destinationClassloader;
	private List<String> scanPackages;
	private List<String> hideClasses;
	private boolean hideFields;
	private boolean hideMethods;
	private Map<String, UMLClass> classes;
	Map<UMLClass, List<UMLRelationship>> classesAndRelationships;

	/**
	 * Instantiates a new Plant UML diagram generator.
	 *
	 * @param paramClassloader  - ClassLoader - the ClassLoader used for loading all
	 *                          diagram classes
	 * @param paramScanPackages - List&lt;String&gt; - all the packages which
	 *                          directly contained classes are the base for the
	 *                          class diagram
	 * @param paramHideClasses  - List&lt;String&gt; - the full qualified class
	 *                          names which should be hidden in the resulting
	 *                          diagram (they are not excluded from the diagram,
	 *                          they are just hidden)
	 * @param paramHideFields   - boolean - true, if fields should be hidden, false,
	 *                          if not
	 * @param paramHideMethods  - boolean - true, if methods should be hidden,
	 *                          false, if not
	 */
	public PlantUMLClassDiagramGenerator(ClassLoader paramClassloader, List<String> paramScanPackages,
			List<String> paramHideClasses, boolean paramHideFields, boolean paramHideMethods) {
		destinationClassloader = paramClassloader;
		scanPackages = paramScanPackages;
		hideClasses = paramHideClasses;
		hideFields = paramHideFields;
		hideMethods = paramHideMethods;
		classesAndRelationships = new HashMap<>();
		classes = new HashMap<>();
	}

	/**
	 * Generate the class diagram string for all classes in the configured packages.
	 *
	 * @return String - the text containing all Plant UML class diagram definitions
	 * @throws IOException            - thrown if a class or jar File could not be
	 *                                read
	 * @throws ClassNotFoundException - thrown if a class in a package could not be
	 *                                found or if a package does not contain any
	 *                                class information
	 */
	public String generateDiagramText() throws ClassNotFoundException, IOException {
		List<Class<?>> resolvedClasses = new ArrayList<>();
		// read all classes from directories or jars
		resolvedClasses.addAll(getAllClassesInScanPackages());
		// sort all classes for a reliable sorted result
		Collections.sort(resolvedClasses, new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		// map java classes to UMLClass, UMLField, UMLMethod and UMLRelationship objects
		for (Class<?> clazz : resolvedClasses) {
			mapToDomainClasses(clazz);
		}
		// build the Plant UML String by calling the corresponding method on all
		// PlantUMLDiagramElements
		StringBuilder builder = new StringBuilder();
		builder.append("@startuml");
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());

		List<UMLClass> listToCompare = new ArrayList<>();
		listToCompare.addAll(classes.values());
		// because the ordered list could be changed in between, sort the list
		Collections.sort(listToCompare, new Comparator<UMLClass>() {
			@Override
			public int compare(UMLClass o1, UMLClass o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		Collection<UMLClass> classesList = listToCompare;
		List<UMLRelationship> relationships = new ArrayList<>();
		// add all class information to the diagram (includes field and method
		// information)
		// because of the full qualified class names packages are added automatically
		// and need not be added manually
		for (UMLClass clazz : classesList) {
			relationships.addAll(classesAndRelationships.get(clazz));
			builder.append(clazz.getDiagramText());
			builder.append(System.lineSeparator());
			builder.append(System.lineSeparator());
		}
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		// because the ordered list could be changed in between, sort the list
		Collections.sort(relationships, new Comparator<UMLRelationship>() {
			@Override
			public int compare(UMLRelationship o1, UMLRelationship o2) {
				return o1.getDiagramText().compareTo(o2.getDiagramText());
			}
		});
		// add all class relationships to the diagram
		for (UMLRelationship relationship : relationships) {
			builder.append(relationship.getDiagramText());
			builder.append(System.lineSeparator());
		}
		addHideToggles(builder, classesList, relationships);
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		// add diagram end to text
		builder.append("@enduml");
		return builder.toString();
	}

	/**
	 * Adds the hide toggles to the class diagram string. Toggles are set for hide
	 * methods, hide field and for each full qualified class name in the list
	 * hideClasses. Only if there are classes and relationships in the diagram the
	 * toggles are set.
	 *
	 * @param paramBuilder     - StringBuilder - the builder used to generate the
	 *                         class diagram text
	 * @param paramClassesList - Collection&lt;{@link UMLClass}&gt; - the list of
	 *                         all classes in the class diagram
	 * @param relationships    - List&lt;{@link UMLRelationship}&gt; - the list of
	 *                         all relationships in the class diagram
	 */
	private void addHideToggles(StringBuilder paramBuilder, Collection<UMLClass> paramClassesList,
			List<UMLRelationship> relationships) {
		if ((paramClassesList != null && !paramClassesList.isEmpty()) || (!relationships.isEmpty())) {
			if (hideFields) {
				paramBuilder.append(System.lineSeparator());
				paramBuilder.append("hide fields");
			}
			if (hideMethods) {
				paramBuilder.append(System.lineSeparator());
				paramBuilder.append("hide methods");
			}
			if (hideClasses != null && !hideClasses.isEmpty()) {
				for (String hideClass : hideClasses) {
					paramBuilder.append(System.lineSeparator());
					paramBuilder.append("hide ");
					paramBuilder.append(hideClass);
				}
			}
		}
	}

	/**
	 * Maps the java class to a {@link UMLClass}.
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the java class object to be
	 *                         processed
	 */
	private void mapToDomainClasses(Class<?> paramClassObject) {
		if (!includeClass(paramClassObject)) {
			return;
		}

		int modifiers = paramClassObject.getModifiers();
		VisibilityType visibilityType = VisibilityType.PUBLIC;
		if (Modifier.isPrivate(modifiers)) {
			visibilityType = VisibilityType.PRIVATE;
		} else if (Modifier.isProtected(modifiers)) {
			visibilityType = VisibilityType.PROTECTED;
		}
		ClassType classType = ClassType.CLASS;
		if (paramClassObject.isAnnotation()) {
			classType = ClassType.ANNOTATION;
		} else if (paramClassObject.isEnum()) {
			classType = ClassType.ENUM;
		} else if (paramClassObject.isInterface()) {
			classType = ClassType.INTERFACE;
		} else if (Modifier.isAbstract(modifiers)) {
			classType = ClassType.ABSTRACT_CLASS;
		}

		UMLClass umlClass = new UMLClass(visibilityType, classType, new ArrayList<UMLField>(),
				new ArrayList<de.elnarion.util.plantuml.generator.classdiagram.UMLMethod>(), paramClassObject.getName(),
				new ArrayList<String>());
		List<UMLRelationship> relationships = new ArrayList<>();
		classesAndRelationships.put(umlClass, relationships);
		classes.put(paramClassObject.getName(), umlClass);

		if (classType == ClassType.ENUM) {
			addEnumConstants(paramClassObject, umlClass);
		} else {
			addFields(paramClassObject.getDeclaredFields(), paramClassObject.getDeclaredMethods(), umlClass);
			addMethods(paramClassObject.getDeclaredMethods(), paramClassObject.getDeclaredFields(), umlClass);
		}
		addSuperClassRelationship(paramClassObject, umlClass);
		addInterfaceRelationship(paramClassObject, umlClass);
		addAnnotationRelationship(paramClassObject, umlClass);
	}

	/**
	 * Reads all enum constants from the java enum class object and adds them as
	 * {@link UMLField} objects to the given {@link UMLClass} object. Only constants
	 * are included, values are ignored.
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the class object of the enum to be
	 *                         processed
	 * @param paramUmlClass    - {@link UMLClass} - the UML class object where the
	 *                         field information should be added
	 */
	private void addEnumConstants(Class<?> paramClassObject, UMLClass paramUmlClass) {
		Object[] enumConstants = paramClassObject.getEnumConstants();
		for (Object enumConstant : enumConstants) {
			UMLField field = new UMLField(ClassifierType.NONE, VisibilityType.PUBLIC, enumConstant.toString(), null);
			paramUmlClass.addField(field);
		}
	}

	/**
	 * Adds an association relationship for each Annotation of a class which is also
	 * part of the diagram (needs to be an element of one of the configured
	 * packages).
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the class object which should be
	 *                         checked for annotation relationships
	 * @param umlClass         - {@link UMLClass} - the UML class from which the
	 *                         relationship starts from
	 */
	private void addAnnotationRelationship(Class<?> paramClassObject, UMLClass umlClass) {
		Annotation[] annotations = paramClassObject.getAnnotations();
		if (annotations != null) {
			for (Annotation annotation : annotations) {
				if (includeClass(annotation.getClass())) {
					UMLRelationship relationship = new UMLRelationship(null, null, null, paramClassObject.getName(),
							annotation.getClass().getName(), RelationshipType.ASSOCIATION);
					addRelationship(umlClass, relationship);
				}
			}
		}
	}

	/**
	 * Little helper method which links an {@link UMLRelationship} object to an
	 * {@link UMLClass} in the internal map.
	 *
	 * @param paramUmlClass        - {@link UMLClass} - the class which should be
	 *                             linked to the {@link UMLRelationship}
	 * @param paramUmlRelationship - {@link UMLRelationship} - the relationship
	 *                             which should be linked to the {@link UMLClass}
	 */
	private void addRelationship(UMLClass paramUmlClass, UMLRelationship paramUmlRelationship) {
		List<UMLRelationship> relationshipList = classesAndRelationships.get(paramUmlClass);
		if (relationshipList == null) {
			relationshipList = new ArrayList<>();
			classesAndRelationships.put(paramUmlClass, relationshipList);
		}
		relationshipList.add(paramUmlRelationship);
	}

	/**
	 * Adds a realization relationship for each interface the given java class
	 * object implements and which is also part of the class diagram.
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the class object which should be
	 *                         checked for interface relationships
	 * @param paramUmlClass    - {@link UMLClass} - the UML class from which the
	 *                         relationship starts from
	 */
	private void addInterfaceRelationship(Class<?> paramClassObject, UMLClass paramUmlClass) {
		Class<?>[] interfaces = paramClassObject.getInterfaces();
		if (interfaces != null) {
			for (Class<?> interfaceElement : interfaces) {
				if (includeClass(interfaceElement)) {
					UMLRelationship relationship = new UMLRelationship(null, null, null, paramClassObject.getName(),
							interfaceElement.getName(), RelationshipType.REALIZATION);
					addRelationship(paramUmlClass, relationship);
				}
			}
		}
	}

	/**
	 * Adds a inheritance relationship for the super class of the given java class
	 * object and which needs also to be part of the class diagram.
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the class object which should be
	 *                         checked for super class relationship
	 * @param paramUmlClass    - {@link UMLClass} - the UML class from which the
	 *                         relationship starts from
	 */
	private void addSuperClassRelationship(Class<?> paramClassObject, UMLClass paramUmlClass) {
		Class<?> superClass = paramClassObject.getSuperclass();
		if (superClass != null && includeClass(superClass)) {
			UMLRelationship relationship = new UMLRelationship(null, null, null, paramClassObject.getName(),
					superClass.getName(), RelationshipType.INHERITANCE);
			addRelationship(paramUmlClass, relationship);
		}
	}

	/**
	 * Iterates over an array of declared methods of a java class, creates
	 * {@link UMLMethod} objects and adds them to the given {@link UMLClass} object.
	 * 
	 * If a declared method is a getter or setter method of a declared field it is
	 * ignored.
	 *
	 * @param paramDeclaredMethods - Method[] - the array of declared methods in the
	 *                             java class corresponding to the given
	 *                             {@link UMLClass} object
	 * @param paramDeclaredFields  - Field[] - the array of declared fields in the
	 *                             java class corresponding to the given
	 *                             {@link UMLClass} object
	 * @param paramUmlClass        - {@link UMLClass} - the uml class where the
	 *                             {@link UMLMethod} objects should be added
	 */
	private void addMethods(Method[] paramDeclaredMethods, Field[] paramDeclaredFields, UMLClass paramUmlClass) {
		if (paramDeclaredMethods != null) {
			for (Method method : paramDeclaredMethods) {
				String methodName = method.getName();
				// ignore normal getters and setters
				if ((methodName.startsWith("get") || methodName.startsWith("set") || methodName.startsWith("is"))
						&& paramDeclaredFields != null && isGetterOrSetterMethod(method, paramDeclaredFields)) {
					continue;
				}
				String returnType = method.getReturnType().getName();
				returnType = removeJavaLangPackage(returnType);
				Class<?>[] parameterTypes = method.getParameterTypes();
				Map<String, String> parameters = convertToParameterStringMap(parameterTypes);
				int modifier = method.getModifiers();
				VisibilityType visibilityType = getVisibility(modifier);
				ClassifierType classifierType = getClassifier(modifier);
				List<String> stereotypes = new ArrayList<>();
				if (method.isAnnotationPresent(Deprecated.class)) {
					stereotypes.add("deprecated");
				}
				if (Modifier.isSynchronized(modifier)) {
					stereotypes.add("synchronized");
				}
				de.elnarion.util.plantuml.generator.classdiagram.UMLMethod umlMethod = new de.elnarion.util.plantuml.generator.classdiagram.UMLMethod(
						classifierType, visibilityType, returnType, methodName, parameters, stereotypes);
				paramUmlClass.addMethod(umlMethod);
			}
		}
	}

	/**
	 * Removes the java lang package string from a full qualified class name. This
	 * makes it easier to read the class diagram.
	 *
	 * @param paramTypeName - String - the full qualified type name
	 * @return String - the type name without the java.lang package name
	 */
	private String removeJavaLangPackage(String paramTypeName) {
		if (paramTypeName.startsWith("java.lang.")) {
			paramTypeName = paramTypeName.substring("java.lang.".length(), paramTypeName.length());
		}
		return paramTypeName;
	}

	/**
	 * Gets the classifier type (none, abstract_static, static, abstract) from the
	 * modifier int.
	 *
	 * @param paramModifier - int - the modifier used for determining the classifier
	 *                      type
	 * @return {@link ClassifierType} - the classifier type
	 */
	private ClassifierType getClassifier(int paramModifier) {
		ClassifierType classifierType = ClassifierType.NONE;
		if (Modifier.isStatic(paramModifier)) {
			if (Modifier.isAbstract(paramModifier)) {
				classifierType = ClassifierType.ABSTRACT_STATIC;
			} else {
				classifierType = ClassifierType.STATIC;
			}
		} else if (Modifier.isAbstract(paramModifier)) {
			classifierType = ClassifierType.ABSTRACT;
		}
		return classifierType;
	}

	/**
	 * Gets the visibility type (package_private, public, private, protected) from
	 * the modifier int.
	 *
	 * @param paramModifier - int - the modifier used for determining the visibility
	 *                      type
	 * @return {@link VisibilityType} - the visibility type
	 */
	private VisibilityType getVisibility(int paramModifier) {
		VisibilityType visibilityType = VisibilityType.PACKAGE_PRIVATE;
		if (Modifier.isPublic(paramModifier)) {
			visibilityType = VisibilityType.PUBLIC;
		} else if (Modifier.isPrivate(paramModifier)) {
			visibilityType = VisibilityType.PRIVATE;
		} else if (Modifier.isProtected(paramModifier)) {
			visibilityType = VisibilityType.PROTECTED;
		}
		return visibilityType;
	}

	/**
	 * Converts an array of parameter types to a string map with synthetic parameter
	 * names and their mapped full qualified type name.
	 * 
	 * Parameter names are synthetic because in Java 7 the parameter names cannot be
	 * determined in all cases.
	 *
	 * @param paramParameterTypes - Class&lt;?&gt;[] - the array of all parameter
	 *                            types which should be processed
	 * @return Map&lt;String,String&gt; - the result
	 */
	private Map<String, String> convertToParameterStringMap(Class<?>[] paramParameterTypes) {
		Map<String, String> parameters = new LinkedHashMap<>();
		if (paramParameterTypes != null) {
			int counter = 1;
			for (Class<?> parameter : paramParameterTypes) {
				String parameterName = parameter.getName();
				if (parameterName.lastIndexOf('.') > 0) {
					parameterName = parameterName.substring(parameterName.lastIndexOf('.') + 1, parameterName.length());
				}
				parameterName = "param" + parameterName + counter;
				parameters.put(parameterName, removeJavaLangPackage(parameter.getName()));
				counter++;
			}
		}
		return parameters;
	}

	/**
	 * Checks if a method is a getter or a setter method of a declared field.
	 *
	 * @param paramMethod         - Method - the method to be checked
	 * @param paramDeclaredFields - Field[] - the declared fields used for the check
	 * @return true, if is getter or setter method
	 */
	private boolean isGetterOrSetterMethod(Method paramMethod, Field[] paramDeclaredFields) {
		String methodName = paramMethod.getName();
		if (methodName.startsWith("get")) {
			methodName = methodName.substring(3, methodName.length());
		} else if (methodName.startsWith("is")) {
			methodName = methodName.substring(2, methodName.length());
		} else if (methodName.startsWith("set")) {
			methodName = methodName.substring(3, methodName.length());
		}
		for (Field field : paramDeclaredFields) {
			String fieldName = field.getName();
			if (fieldName.equalsIgnoreCase(methodName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates {@link UMLField} or {@link UMLRelationship} objects for all given
	 * fields. And adds them to the given {@link UMLClass} object.
	 * 
	 * If a field type is part of the class diagram (directly or via List or Set) it
	 * is added as {@link UMLRelationship}. If not it is added as {@link UMLField}
	 * object.
	 * 
	 * If a field has a getter an a setter method its visibility is upgraded to
	 * public.
	 *
	 * @param paramDeclaredFields  - Field[] - the Field objects which are the base
	 *                             for the {@link UMLField} or
	 *                             {@link UMLRelationship} objects.
	 * @param paramDeclaredMethods - Method[] - the method objects of the java class
	 *                             corresponding to the given {@link UMLClass}
	 *                             object
	 * @param paramUmlClass        - {@link UMLClass} - the uml class objects to
	 *                             which the {@link UMLField} or
	 *                             {@link UMLRelationship} objects should be added
	 */
	private void addFields(Field[] paramDeclaredFields, Method[] paramDeclaredMethods, UMLClass paramUmlClass) {
		if (paramDeclaredFields != null) {
			for (java.lang.reflect.Field field : paramDeclaredFields) {
				Class<?> type = field.getType();
				boolean relationshipAdded = addAggregationRelationship(paramUmlClass, field);
				if (relationshipAdded) {
					// do nothing - skip processing
				} else if (includeClass(type)) {
					UMLRelationship relationship = new UMLRelationship(null, null, field.getName(),
							field.getDeclaringClass().getName(), type.getName(), RelationshipType.DIRECTED_ASSOCIATION);
					addRelationship(paramUmlClass, relationship);
				} else {
					int modifier = field.getModifiers();
					ClassifierType classifierType = getClassifier(modifier);
					VisibilityType visibilityType = getVisibility(modifier);
					if (hasGetterAndSetterMethod(field.getName(), paramDeclaredMethods)) {
						visibilityType = VisibilityType.PUBLIC;
					}
					UMLField umlField = new UMLField(classifierType, visibilityType, field.getName(),
							removeJavaLangPackage(type.getName()));
					paramUmlClass.addField(umlField);
				}
			}
		}

	}

	/**
	 * Checks if a field has getter and setter methods.
	 *
	 * @param paramFieldName       - String - the field name
	 * @param paramDeclaredMethods - Method[] - the methods which should be used for
	 *                             the check
	 * @return true, if successful
	 */
	private boolean hasGetterAndSetterMethod(String paramFieldName, Method[] paramDeclaredMethods) {
		String getterMethodName = "get" + paramFieldName;
		String setterMethodName = "set" + paramFieldName;
		String isMethodName = "is" + paramFieldName;
		boolean hasGetterMethod = false;
		boolean hasSetterMethod = false;
		if (paramDeclaredMethods != null) {
			for (Method method : paramDeclaredMethods) {
				String methodName = method.getName();
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

	/**
	 * Adds an aggregation relationship to a given {@link UMLClass} object if the
	 * field is a set or list, which has a generic type argument, which references a
	 * class in the diagram.
	 *
	 * @param paramUmlClass - {@link UMLClass} - the from side of the relationship
	 * @param paramField    - Field - the java reflection field which should be
	 *                      processed
	 * @return true, if an aggregation relationship was created and added
	 */
	private boolean addAggregationRelationship(UMLClass paramUmlClass, Field paramField) {
		Type type = paramField.getType();
		Type genericType = paramField.getGenericType();
		boolean isRelationshipAggregation = false;
		if (Collection.class.isAssignableFrom((Class<?>) type) && genericType instanceof ParameterizedType
				&& (((ParameterizedType) genericType).getRawType().equals(Set.class)
						|| ((ParameterizedType) genericType).getRawType().equals(List.class))) {
			Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
			if (actualTypeArguments != null) {
				for (Type typeArgument : actualTypeArguments) {
					Class<?> typeArgumentClass = getClassForType(typeArgument);
					if (((typeArgumentClass != null) && includeClass(typeArgumentClass))) {
						UMLRelationship relationship = new UMLRelationship("1", "0..*", paramField.getName(),
								paramField.getDeclaringClass().getName(), (typeArgumentClass).getName(),
								RelationshipType.AGGREGATION);
						addRelationship(paramUmlClass, relationship);
						isRelationshipAggregation = true;
					}
				}
			}
		}
		return isRelationshipAggregation;
	}

	/**
	 * Gets the class for type.
	 *
	 * @param paramType the type argument
	 * @return Class - the class for type
	 */
	private Class<?> getClassForType(Type paramType) {
		Class<?> typeArgumentClass = null;
		if (paramType instanceof ParameterizedType) {
			if (((ParameterizedType) paramType).getRawType() instanceof Class<?>) {
				typeArgumentClass = (Class<?>) ((ParameterizedType) paramType).getRawType();
			}
		} else if (paramType instanceof Class<?>) {
			typeArgumentClass = (Class<?>) paramType;
		} else if (paramType instanceof TypeVariable<?>) {
			// Nothing to do ignore type variables
		}
		return typeArgumentClass;
	}

	/**
	 * Gets the all classes which are contained in the scanned packages.
	 *
	 * @return Set&lt;Class&lt;?&gt;&gt; - all classes in scanned packages
	 * @throws ClassNotFoundException - thrown by ClassLoader or if a package does
	 *                                not contain any class
	 * @throws IOException            - signals that an I/O exception has occurred.
	 */
	private Set<Class<?>> getAllClassesInScanPackages() throws ClassNotFoundException, IOException {
		Set<Class<?>> resultSet = new HashSet<>();
		for (String scanpackage : scanPackages) {
			List<Class<?>> classesList = getClasses(scanpackage, destinationClassloader);
			if (classesList.isEmpty()) {
				throw new ClassNotFoundException("No classes found for package " + scanpackage);
			}
			resultSet.addAll(classesList);
		}
		return resultSet;
	}

	/**
	 * Checks if a class is a member of one of the scanned packages.
	 *
	 * @param paramClass - Class&lt;?&gt; - the class to be checked
	 * @return true, if class is a member of the scanned packages
	 */
	private boolean includeClass(Class<?> paramClass) {
		Package packageElement = paramClass.getPackage();
		String packageName = "";
		if (packageElement != null) {
			packageName = packageElement.getName();
		}
		return (scanPackages.contains(packageName)) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * Reads all classes for a package name from the given ClassLoader.
	 *
	 * @param packageName      - String - the package name
	 * @param paramClassLoader - ClassLoader - the ClassLoader
	 * @return List&lt;Class&lt;?&gt;&gt; - the classes of the package
	 * @throws ClassNotFoundException - the class was not found by the class loader
	 * @throws IOException            - signals that an I/O exception has occurred
	 */
	private List<Class<?>> getClasses(String packageName, ClassLoader paramClassLoader)
			throws ClassNotFoundException, IOException {
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = paramClassLoader.getResources(path);
		List<File> dirs = new ArrayList<>();
		List<JarFile> jars = new ArrayList<>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			if (resource.getProtocol().equals("file")) {
				dirs.add(new File(resource.getFile()));
			} else if (resource.getProtocol().equals("jar")) {
				// strip out only the
				// JAR file
				String jarPath = resource.getPath().substring(6, resource.getPath().indexOf('!'));
				JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
				jars.add(jar);
			}
		}
		ArrayList<Class<?>> classesList = new ArrayList<>();
		for (File directory : dirs) {
			classesList.addAll(findClasses(directory, packageName, paramClassLoader));
		}
		for (JarFile jarFile : jars) {
			classesList.addAll(findClassesInJar(jarFile, packageName, paramClassLoader));
		}
		return classesList;
	}

	/**
	 * Find classes of a given package name in a jar file.
	 *
	 * @param paramJarFile     - JarFile - the jar file which is used for the scan
	 * @param paramPackageName - String - the package name
	 * @param classloader      - ClassLoader - the classloader
	 * @return Collection&lt;? extends class&lt;?&gt;&gt; - all classes of a given
	 *         package in the given jar file
	 * @throws ClassNotFoundException - the class not found by the classloader
	 */
	private Collection<? extends Class<?>> findClassesInJar(JarFile paramJarFile, String paramPackageName,
			ClassLoader classloader) throws ClassNotFoundException {
		List<Class<?>> classesList = new ArrayList<>();
		Enumeration<JarEntry> jarEntries = paramJarFile.entries();
		while (jarEntries.hasMoreElements()) {
			JarEntry entry = jarEntries.nextElement();
			String entryPath = entry.getName().replaceAll("/", ".");
			if (entryPath.endsWith(CLASS_ENDING)) {
				String className = entryPath.substring(0, entryPath.length() - CLASS_ENDING.length());
				String entryPackage = className;
				if (entryPackage.contains(".")) {
					entryPackage = entryPackage.substring(0, entryPackage.lastIndexOf('.'));
				}
				if (entryPackage.equals(paramPackageName)) {
					classesList.add(classloader.loadClass(className));
				}
			}
		}
		return classesList;
	}

	/**
	 * Recursive method used to find all classes in a given directory and sub
	 * directories.
	 *
	 * @param directory   - File - the base directory used for the file search of
	 *                    classes in the given package
	 * @param packageName - String - the package name for classes searched inside
	 *                    the base directory
	 * @return List&lt;Class&lt;?&gt;&gt; - the classes of the given package in the
	 *         given base directory
	 * @throws ClassNotFoundException - the class was not found by the classloader
	 */
	private List<Class<?>> findClasses(File directory, String packageName, ClassLoader classloader)
			throws ClassNotFoundException {
		List<Class<?>> classesList = new ArrayList<>();
		if (!directory.exists()) {
			return classesList;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classesList.addAll(findClasses(file, packageName + "." + file.getName(), classloader));
			} else if (file.getName().endsWith(CLASS_ENDING)) {
				classesList.add(classloader
						.loadClass(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classesList;
	}
}
