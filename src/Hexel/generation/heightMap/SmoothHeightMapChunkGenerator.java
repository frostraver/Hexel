package Hexel.generation.heightMap;

import Hexel.generation.plate.PlateSumChunks;
import Hexel.generation.plate.PlateSum;

import Hexel.generation.plate.PlateChunk;

public class SmoothHeightMapChunkGenerator {

    private HeightMap hm;

    public SmoothHeightMapChunkGenerator(){
        hm = new HeightMap();
    }

    public SmoothHeightMapChunk gen(int px, int py){
        px *= SmoothHeightMapChunk.WIDTH;
        py *= SmoothHeightMapChunk.HEIGHT;
        SmoothHeightMapChunk shmc = new SmoothHeightMapChunk();
        for (int x = 0; x < SmoothHeightMapChunk.WIDTH; x++){
            for (int y = 0; y < SmoothHeightMapChunk.HEIGHT; y++){

                double hsum = 0;
                double div = 0;
                for (int dx = -1; dx <= 1; dx++){
                    for (int dy = -1; dy <= 1; dy++){
                        int h = hm.getHeight(px + x + dx, py + y + dy);
                        double w = hm.getResistanceToChange(px + x + dx, py + y + dy);
                        if (dx == 0 && dy == 0)
                            w = 1/(1+w);
                        hsum += h*w;
                        div += w;
                    }
                }

                shmc.heights[x][y] = (int)(hsum/div);
            }
        }
        return shmc;
    }
}


