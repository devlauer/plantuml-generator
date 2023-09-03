package de.elnarion.util.plantuml.generator.sequencediagram.internal;

import java.util.List;

public interface ICallerClass {
    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the parent classes.
     *
     * @return the parent classes
     */
    List<String> getParentClassNames();

    /**
     * Gets the diagram class name.
     *
     * @return the diagram class name
     */
    String getDiagramClassName();
}
