package Hexel.generation.plate;

import Hexel.math.Vector2i;

import java.util.Random;

public class Plate {

    public static final int MAX_OFFSET = 30;
    public static final int AVG_OFFSET = 12;
    public static final int AVG_MAGNITUDE = 64;

    public double magnitude;

    public int heightOffset;

    public int heightVariation;

    public int offsetX;
    public int offsetY;
    
    public double resistanceToChange;

    public Plate(Vector2i p){
        Random random = new Random(p.hashCode());
        offsetX = (int)(random.nextGaussian()*AVG_OFFSET);
        offsetY = (int)(random.nextGaussian()*AVG_OFFSET);
        if (Math.abs(offsetY) > MAX_OFFSET)
            offsetY = (int)(MAX_OFFSET*Math.signum(offsetY));
        magnitude = random.nextDouble()*AVG_MAGNITUDE;
        heightOffset = (int)(magnitude + random.nextGaussian()*magnitude);

        heightVariation = (int)(Math.abs(random.nextGaussian())*magnitude/4);

        resistanceToChange = (int)(random.nextDouble()*heightVariation/2);
    }
}

