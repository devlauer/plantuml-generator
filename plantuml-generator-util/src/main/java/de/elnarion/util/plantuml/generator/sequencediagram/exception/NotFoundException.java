package de.elnarion.util.plantuml.generator.sequencediagram.exception;

/**
 * This exception is thrown if a method or class is not found on the classpath.
 */
public class NotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}


}
