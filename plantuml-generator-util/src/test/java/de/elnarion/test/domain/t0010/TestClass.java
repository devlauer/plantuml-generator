package de.elnarion.test.domain.t0010;

import java.util.Set;

/**
 * The Class TestClass.
 *
 * @param <T> the generic type
 */
public class TestClass<T extends BaseInterface<?>> extends AbstractTestClass<T, Integer> {

	/** The test set. */
	private Set<AbstractTestClass<T, Integer>> testSet2;

	/**
	 * Gets the test set 2.
	 *
	 * @return Set - the test set 2
	 */
	public Set<AbstractTestClass<T, Integer>> getTestSet2() {
		return testSet2;
	}

	/**
	 * Sets the test set 2.
	 *
	 * @param testSet2 the test set 2
	 */
	public void setTestSet2(Set<AbstractTestClass<T, Integer>> testSet2) {
		this.testSet2 = testSet2;
	}

	/**
	 * Instantiates a new nomination hibernate DAO.
	 *
	 * @param sessionFactory the session factory
	 */
	public TestClass(String sessionFactory) {
		super(sessionFactory); // LINE 36
	}
}
