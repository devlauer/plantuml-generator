package de.elnarion.test.domain.t0022;

public class TestPrivateFinalObject {

	private final String PRIVATE_CONSTANT = "asdf";
	private final int PRIVATE_CONSTANT_INT = 2;
	private String nonconstant = "asdfasdfasdf";
	private int nonconstant2 = 22;
	protected final String PROTECTED_CONSTANT = "asdf";
	protected final int PROTECTED_CONSTANT_INT = 22;
	protected String nonconstantProtected = "asdf";
	protected int nonconstant2Protected = 213;
	public final String PUBLIC_CONSTANT = "asdf";
	public final int PUBLIC_CONSTANT_INT = 22;
	public String nonconstantPublic = "asdf";
	public int nonconstant2Public = 234;
	
	public void doSomething()
	{
		@SuppressWarnings("unused")
		String test = PRIVATE_CONSTANT+PRIVATE_CONSTANT_INT+nonconstant+nonconstant2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((PRIVATE_CONSTANT == null) ? 0 : PRIVATE_CONSTANT.hashCode());
		result = prime * result + PRIVATE_CONSTANT_INT;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestPrivateFinalObject other = (TestPrivateFinalObject) obj;
		if (!PRIVATE_CONSTANT.equals(other.PRIVATE_CONSTANT))
			return false;
		if (PRIVATE_CONSTANT_INT != other.PRIVATE_CONSTANT_INT)
			return false;
		return true;
	}
	
}
