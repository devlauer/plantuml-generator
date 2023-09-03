package de.elnarion.test.sequence.t0005;

import java.util.Random;

public class SequenceStarterClass {

    Random random = new Random();

    public void startSequence(){
        circularStart();
    }
    public void circularStart(){
        if(random.nextBoolean()){
            new CalledClass().circularSubMethod();
        }
        else {
            new CalledClass().circularSubTwoMethod();
        }
    }

    public void recursiveCall(){
        if(random.nextBoolean()){
            recursiveCall();
        }
    }


}
