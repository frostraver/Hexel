package Hexel.generation.heightMap;

import Hexel.generation.plate.PlateSumChunks;
import Hexel.generation.plate.PlateSum;

public class HeightMap {

    private PlateSumChunks smallPSCs;
    private PlateSumChunks bigPSCs;

    private static final int SCALE = 2;

    public HeightMap(){
        smallPSCs = new PlateSumChunks();
        bigPSCs = new PlateSumChunks();
    }

    public int getHeight(int px, int py){
        PlateSum small = smallPSCs.getPlateSum(px, py);
        PlateSum big = bigPSCs.getPlateSum(px/SCALE, py/SCALE);
        double h = 0;
        if (big.baseHeight < 0){
            h = big.baseHeight;
            if (h + big.heightVar < 0)
                h += big.heightVar;
        }
        else  {
            h = small.additions + Math.max(small.baseHeight, 0) + small.heightVar;
        }
        return (int)h;
    }

    public double getResistanceToChange(int px, int py){
        PlateSum small = smallPSCs.getPlateSum(px, py);
        return small.resistanceToChange;
    }
}

