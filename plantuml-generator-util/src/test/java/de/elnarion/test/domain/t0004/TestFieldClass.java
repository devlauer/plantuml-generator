package de.elnarion.test.domain.t0004;

import java.util.logging.Logger;

/**
 * The Class TestFieldClass.
 */
public class TestFieldClass {

	private String testStringWithGetterAndSetter;
	private String testStringWithGetter;
	@SuppressWarnings("unused")
	private String testStringWithSetter;
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger("test");
	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger("test2");
	String testStringWithoutVisibility;
	protected String testStringWithVisibilityProtected;

	@SuppressWarnings("unused")
	private static String testStringStaticPrivate;

	static String testStringStaticWithoutVisibility;

	protected static String testStringStaticProtected;
	
	@SuppressWarnings("unused")
	private TestReference testReference;

	/**
	 * 
	 */
	public static String testStringStaticPublic;

	/** The test string with visibility public. */
	public String testStringWithVisibilityPublic;

	/**
	 * Gets the test string with getter and setter.
	 *
	 * @return String - the test string with getter and setter
	 */
	public String getTestStringWithGetterAndSetter() {
		return testStringWithGetterAndSetter;
	}

	/**
	 * Sets the test string with getter and setter.
	 *
	 * @param testStringWithGetterAndSetter the test string with getter and setter
	 */
	public void setTestStringWithGetterAndSetter(String testStringWithGetterAndSetter) {
		this.testStringWithGetterAndSetter = testStringWithGetterAndSetter;
	}

	/**
	 * Gets the test string with getter.
	 *
	 * @return String - the test string with getter
	 */
	public String getTestStringWithGetter() {
		return testStringWithGetter;
	}

	/**
	 * Sets the test string with setter.
	 *
	 * @param testStringWithSetter the test string with setter
	 */
	public void setTestStringWithSetter(String testStringWithSetter) {
		this.testStringWithSetter = testStringWithSetter;
	}

}
