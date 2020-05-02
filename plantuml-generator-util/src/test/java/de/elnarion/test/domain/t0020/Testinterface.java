package de.elnarion.test.domain.t0020;

public interface Testinterface {
    public void doSomething();
    void doSomething2();
    default void doSomething3()
    {
        System.out.println("");
    }
}