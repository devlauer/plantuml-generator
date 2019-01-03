package de.elnarion.test.domain.t0010;

import java.io.Serializable;
import java.util.Set;

/**
 * The Class AbstractTestClass.
 *
 * @param <T> the generic type
 * @param <K> the key type
 */
public abstract class AbstractTestClass<T, K extends Serializable> {

	/** The session factory. */
	private String sessionFactory;

	/**
	 * Gets the session factory.
	 *
	 * @return String - the session factory
	 */
	public String getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Sets the session factory.
	 *
	 * @param sessionFactory the session factory
	 */
	public void setSessionFactory(String sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Gets the persistent class.
	 *
	 * @return Class - the persistent class
	 */
	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	/**
	 * Sets the persistent class.
	 *
	 * @param persistentClass the persistent class
	 */
	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	/**
	 * Gets the test set.
	 *
	 * @return Set - the test set
	 */
	public Set<T> getTestSet() {
		return testSet;
	}

	/**
	 * Sets the test set.
	 *
	 * @param testSet the test set
	 */
	public void setTestSet(Set<T> testSet) {
		this.testSet = testSet;
	}

	/**
	 * Gets the k set.
	 *
	 * @return Set - the k set
	 */
	public Set<K> getkSet() {
		return kSet;
	}

	/**
	 * Sets the k set.
	 *
	 * @param kSet the k set
	 */
	public void setkSet(Set<K> kSet) {
		this.kSet = kSet;
	}

	/** The persistent class. */
	private Class<T> persistentClass;

	/** The test set. */
	private Set<T> testSet;

	/** The k set. */
	private Set<K> kSet;

	/**
	 * Instantiates a new abstract hibernate DAO.
	 *
	 * @param sessionFactory the session factory
	 */
	protected AbstractTestClass(String sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
