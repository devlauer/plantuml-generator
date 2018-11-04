package de.elnarion.test.domain.t0005;

import java.math.BigInteger;
import java.util.logging.Logger;

/**
 * The Class MethodTestClass.
 */
public abstract class MethodTestClass {

	/** The string with field. */
	@SuppressWarnings("unused")
	private String stringWithField;
	
	/**
	 * Test method.
	 */
	@SuppressWarnings("unused")
	private void testMethod()
	{
		
	}
	
	/**
	 * Test method with return.
	 *
	 * @return the string
	 */
	@SuppressWarnings("unused")
	private String testMethodWithReturn()
	{
		return null;
	}
	
	/**
	 * Test method with return and parameter.
	 *
	 * @param paramString the param string
	 * @return the string
	 */
	@SuppressWarnings("unused")
	private String testMethodWithReturnAndParameter(String paramString)
	{
		return null;
	}
	
	/**
	 * Test method with return and multiple parameters.
	 *
	 * @param paramString  the param string
	 * @param paramString2 the param string 2
	 * @return the string
	 */
	@SuppressWarnings("unused")
	private String testMethodWithReturnAndMultipleParameters(String paramString,String paramString2)
	{
		return null;
	}
	
	/**
	 * Test method without return and multiple parameters.
	 *
	 * @param paramString the param string
	 * @param paramInt    the param int
	 */
	@SuppressWarnings("unused")
	private void testMethodWithoutReturnAndMultipleParameters(String paramString,int paramInt)
	{
		
	}
	
	/**
	 * Test overload.
	 *
	 * @param paramTest the param test
	 */
	@SuppressWarnings("unused")
	private void testOverload(String paramTest)
	{
		
	}
	
	/**
	 * Test overload.
	 *
	 * @param paramInt the param int
	 */
	@SuppressWarnings("unused")
	private void testOverload(int paramInt)
	{
		
	}
	
	/**
	 * Test overload.
	 *
	 * @param paramLong the param long
	 */
	@SuppressWarnings("unused")
	private void testOverload(Long paramLong)
	{
		
	}
	
	/**
	 * Test method without visibility.
	 */
	void testMethodWithoutVisibility()
	{
		
	}
	
	/**
	 * Test method protected.
	 */
	protected void testMethodProtected()
	{
		
	}

	/**
	 * Test method protected static.
	 */
	protected static void testMethodProtectedStatic()
	{
		
	}
	
	/**
	 * Test method public.
	 */
	public void testMethodPublic()
	{
		
	}
	
	/**
	 * Test method private static.
	 */
	@SuppressWarnings("unused")
	private static void testMethodPrivateStatic()
	{
		
	}
	
	/**
	 * Test method without visibility static.
	 */
	static void testMethodWithoutVisibilityStatic()
	{
		
	}
	
	/**
	 * Test method public static.
	 */
	public static void testMethodPublicStatic()
	{
		
	}
	
	/**
	 * Gets the string without field.
	 *
	 * @return String - the string without field
	 */
	public String getStringWithoutField()
	{
		return null;
	}
	
	/**
	 * Gets the string with field.
	 *
	 * @return String - the string with field
	 */
	public String getStringWithField()
	{
		return null;
	}

	/**
	 * Sets the string without field.
	 *
	 * @param paramString the param string
	 */
	public void setStringWithoutField(String paramString)
	{
		
	}
	
	/**
	 * Sets the string with field.
	 *
	 * @param paramString the param string
	 */
	public void setStringWithField(String paramString)
	{
		
	}
	
	/**
	 * Do synchronized.
	 */
	public synchronized void doSynchronized()
	{
		
	}
	
	
	/**
	 * Test method abstract.
	 */
	public abstract void testMethodAbstract();
	
	/**
	 * Test method deprecated.
	 */
	@Deprecated
	public abstract void testMethodDeprecated();
	
	/**
	 * Test method with package parameters.
	 *
	 * @param paramLogger the param logger
	 * @param paramInt    the param int
	 */
	public void testMethodWithPackageParameters(Logger paramLogger, BigInteger paramInt)
	{
		
	}
	
	/**
	 * Test method synchronized and deprecated.
	 */
	@Deprecated
	public synchronized void testMethodSynchronizedAndDeprecated() {
	}
}
