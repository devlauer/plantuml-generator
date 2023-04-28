package de.elnarion.test.domain.t0026;

import java.util.*;

public class TestA {
    private List<TestB> testBList;
    private Vector<TestC> testCVector;
    private Queue<TestD> testDQueue;
    private Deque<TestE> testEDeque;
    private TestF directTestF;

    private Map<TestG, TestH> testATestBMap;

    public TestF getDirectTestF() {
        return directTestF;
    }

    public void setDirectTestF(TestF directTestF) {
        this.directTestF = directTestF;
    }


    public List<TestB> getTestBList() {
        return testBList;
    }

    public void setTestBList(List<TestB> testBList) {
        this.testBList = testBList;
    }

    public Vector<TestC> getTestCVector() {
        return testCVector;
    }

    public void setTestCVector(Vector<TestC> testCVector) {
        this.testCVector = testCVector;
    }

    public Queue<TestD> getTestDQueue() {
        return testDQueue;
    }

    public void setTestDQueue(Queue<TestD> testDQueue) {
        this.testDQueue = testDQueue;
    }

    public Deque<TestE> getTestEDeque() {
        return testEDeque;
    }

    public void setTestEDeque(Deque<TestE> testEDeque) {
        this.testEDeque = testEDeque;
    }

}
