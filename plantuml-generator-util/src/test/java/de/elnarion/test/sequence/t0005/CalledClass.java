package de.elnarion.test.sequence.t0005;

import java.util.Random;

public class CalledClass {

    Random random = new Random();

    public void circularSubMethod(){
        if(random.nextBoolean()){
            new SequenceStarterClass().circularStart();
        }
    }

    public void circularSubTwoMethod(){
        if(random.nextBoolean()){
            new SequenceStarterClass().circularStart();
        }
    }
}
