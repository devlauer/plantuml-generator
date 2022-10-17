package de.elnarion.util.plantuml.generator.sequencediagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.elnarion.util.plantuml.generator.config.PlantUMLSequenceDiagramConfig;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * The Class CallerMethod.
 */
public class CallerMethod {

	/** The class name. */
	private CallerClass callerClass;
	
	/** The ct method. */
	private CtMethod ctMethod;
	
	/** The config. */
	private PlantUMLSequenceDiagramConfig config;
	
	/** The callees. */
	private List<CallerMethod> callees = new ArrayList<>();
	

	/**
	 * Instantiates a new caller method.
	 *
	 * @param paramMethod the param method
	 * @param paramCallerClass the param caller class
	 * @param paramConfig the param config
	 */
	public CallerMethod(CtMethod paramMethod, CallerClass paramCallerClass, PlantUMLSequenceDiagramConfig paramConfig) {
		ctMethod = paramMethod;
		config = paramConfig;
		callerClass = paramCallerClass;
	}
	
	
	/**
	 * Gets the method name.
	 *
	 * @return the method name
	 */
	public String getMethodName() {
		return ctMethod.getName();
	}
	
	/**
	 * Gets the method parameters.
	 *
	 * @return the method parameters
	 */
	public String getMethodParameters() {
		return ctMethod.getSignature();
	}
	
	/**
	 * Gets the callees.
	 *
	 * @return the callees
	 */
	public List<CallerMethod> getCallees() {
		return callees;
	}
	
	/**
	 * Gets the caller class.
	 *
	 * @return the caller class
	 */
	public CallerClass getCallerClass() {
		return callerClass;
	}

	/**
	 * Sets the caller class.
	 *
	 * @param callerClass the new caller class
	 */
	public void setCallerClass(CallerClass callerClass) {
		this.callerClass = callerClass;
	}

	/**
	 * Gets the diagram participants.
	 *
	 * @return the diagram participants
	 */
	public Set<String> getDiagramParticipants() {
		Set<String> diagramParticipants = new HashSet<>();
		diagramParticipants.add(callerClass.getDiagramClassName());
		diagramParticipants.addAll(callees.stream().map(CallerMethod::getDiagramParticipants).flatMap(Set::stream).collect(Collectors.toSet()));
		return diagramParticipants;
	}
	
	/**
	 * Gets the diagram text.
	 *
	 * @return the diagram text
	 */
	public Object getDiagramText() {
		String participantsString = generateParticipantsText();
		StringBuilder callerMethodDiagramTextBuilder = new StringBuilder();
		callerMethodDiagramTextBuilder.append(System.lineSeparator());
		callerMethodDiagramTextBuilder.append(participantsString);
		callerMethodDiagramTextBuilder.append(System.lineSeparator());
		callerMethodDiagramTextBuilder.append(System.lineSeparator());
		callerMethodDiagramTextBuilder.append("activate ");
		callerMethodDiagramTextBuilder.append(this.getCallerClass().getDiagramClassName());
		callerMethodDiagramTextBuilder.append(System.lineSeparator());
		String callSequenceDiagramText = generateCallSequenceDiagramText("\t");
		callerMethodDiagramTextBuilder.append(callSequenceDiagramText);
		callerMethodDiagramTextBuilder.append("deactivate ");
		callerMethodDiagramTextBuilder.append(this.getCallerClass().getDiagramClassName());
		callerMethodDiagramTextBuilder.append(System.lineSeparator());
		callerMethodDiagramTextBuilder.append(System.lineSeparator());
		return callerMethodDiagramTextBuilder.toString();
	}

	/**
	 * Generate call sequence diagram text.
	 *
	 * @param paramIndent the param indent
	 * @return the string
	 */
	private String generateCallSequenceDiagramText(String paramIndent) {
		StringBuilder callSequenceBuilder = new StringBuilder();
		for(CallerMethod calleeMethod:callees) {
			String callerClassName = this.getCallerClass().getDiagramClassName();
			String calleeClassName = calleeMethod.getCallerClass().getDiagramClassName();
			callSequenceBuilder.append(paramIndent);
			callSequenceBuilder.append(callerClassName);
			callSequenceBuilder.append(" -> ");
			callSequenceBuilder.append(calleeClassName);
			callSequenceBuilder.append(" : ");
			callSequenceBuilder.append(calleeMethod.getMethodName());
			callSequenceBuilder.append(System.lineSeparator());

			callSequenceBuilder.append(paramIndent);
			callSequenceBuilder.append("activate ");
			callSequenceBuilder.append(calleeClassName);
			callSequenceBuilder.append(System.lineSeparator());

			callSequenceBuilder.append(calleeMethod.generateCallSequenceDiagramText(paramIndent+"\t"));

			callSequenceBuilder.append(paramIndent+"\t");
			callSequenceBuilder.append(calleeClassName);
			callSequenceBuilder.append(" --> ");
			callSequenceBuilder.append(callerClassName);
			if(config.isShowReturnTypes())
			{
				callSequenceBuilder.append(" : ");
				callSequenceBuilder.append(calleeMethod.getReturnType());
			}
			callSequenceBuilder.append(System.lineSeparator());
			
			callSequenceBuilder.append(paramIndent);
			callSequenceBuilder.append("deactivate ");
			callSequenceBuilder.append(calleeClassName);
			callSequenceBuilder.append(System.lineSeparator());
		}
		return callSequenceBuilder.toString();
	}

	/**
	 * Gets the return type.
	 *
	 * @return the return type
	 * @throws NotFoundException 
	 */
	private String getReturnType() {
		try {
			if(config.isUseShortClassNames())
				return ctMethod.getReturnType().getSimpleName();
			return ctMethod.getReturnType().getName();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}


	/**
	 * Generate participants text.
	 *
	 * @return the string
	 */
	private String generateParticipantsText() {
		Set<String> participants = getDiagramParticipants();
		List<String> diagramParticipants = participants.stream().map(e -> "participant "+e).collect(Collectors.toList());
		return String.join(System.lineSeparator(), diagramParticipants);
	}

}
