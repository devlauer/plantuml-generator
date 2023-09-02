package de.elnarion.test.sequence.t0005;

import java.util.Random;

public class SequenceStarterClass {

    Random random = new Random();

    public void startSequence(){
        recursiveStart();
    }
    public void recursiveStart(){
        if(random.nextBoolean()){
            recursiveSubMethod();
        }
        else {
            recursiveSubTwoMethod();
        }
    }

    public void recursiveSubMethod(){
        if(random.nextBoolean()){
            recursiveStart();
        }
    }

    public void recursiveSubTwoMethod(){
        if(random.nextBoolean()){
            recursiveStart();
        }
    }
}
