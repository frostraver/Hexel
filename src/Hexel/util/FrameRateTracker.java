package Hexel.util;

public class FrameRateTracker {

    public long[] last;
    public int i;

    public long lastTime = 0;

    public FrameRateTracker(int n){
        last = new long[n];
        i = 0;
    }

    public void hit(){
        last[i] = System.currentTimeMillis() - lastTime;
        i = (i+1) % last.length;

        lastTime = System.currentTimeMillis();
    }

    public double avg(){
        double sum = 0;
        for (int i = 0; i < last.length; i++){
            sum += last[i];
        }
        return 1000.0/(sum / this.last.length);
    }
}

