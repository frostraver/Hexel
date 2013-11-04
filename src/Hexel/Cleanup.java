package Hexel;

import Hexel.chunk.Chunks;

import java.util.ArrayList;

public class Cleanup extends Thread {

    public Cleanup(){

    }

    ArrayList<Runnable> runnables = new ArrayList<Runnable>();

    public void add(Runnable runnable){
        runnables.add(runnable);
    }

    public void run() {
        for (int i = 0; i < runnables.size(); i++){
            runnables.get(i).run();
        }
    }
}

