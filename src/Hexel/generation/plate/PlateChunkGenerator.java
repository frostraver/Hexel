package Hexel.generation.plate;

import java.util.LinkedList;
import java.util.Queue;
import Hexel.math.Vector2i;

import java.util.Random;

public class PlateChunkGenerator {

    PlateGrids plateGrids;

    public PlateChunkGenerator(){
        plateGrids = new PlateGrids();
    }


    public PlateChunk gen(int px, int py){

        Vector2i[][] basinGrid = new Vector2i[PlateChunk.WIDTH][PlateChunk.HEIGHT];

        PlateChunk pc = new PlateChunk();
        for (int x = 0; x < PlateChunk.WIDTH; x++){
            for (int y = 0; y < PlateChunk.HEIGHT; y++){
                pc.plates[x][y] = new Plate(plateGrids.getBasin(px, py, x, y));
            }
        }
        return pc;
    }
}


