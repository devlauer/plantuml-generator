package de.elnarion.util.plantuml.generator.sequencediagram.internal;

import javassist.NotFoundException;

import java.util.List;

public interface ICallerMethod {
    /**
     * Gets the method name.
     *
     * @return the method name
     */
    String getMethodName();

    /**
     * Gets the callees.
     *
     * @return the callees
     */
    List<ICallerMethod> getCallees();

    /**
     * Gets the caller class.
     *
     * @return the caller class
     */
    ICallerClass getCallerClass();

    /**
     * Gets the diagram participants.
     *
     * @return the diagram participants
     */
    List<String> getDiagramParticipants();

    /**
     * Gets the diagram text.
     *
     * @return the diagram text
     * @throws ClassNotFoundException the class not found exception
     * @throws NotFoundException      the not found exception
     */
    Object getDiagramText() throws ClassNotFoundException, NotFoundException;

    /**
     * Generate call sequence diagram text.
     *
     * @param paramIndent the param indent
     * @return the string
     * @throws NotFoundException the not found exception
     */
    String generateCallSequenceDiagramText(String paramIndent) throws NotFoundException;

    /**
     * Gets the return type.
     *
     * @return the return type
     * @throws NotFoundException if type is not found
     */
    String getReturnType() throws NotFoundException;
}
