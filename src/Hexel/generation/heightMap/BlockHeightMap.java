package Hexel.generation.heightMap;

import Hexel.chunk.Chunk;

public class BlockHeightMap {

    
    public static int WIDTH = (Chunk.WIDTH+1)*2;
    public static int HEIGHT = (Chunk.HEIGHT+1)*2;

    int[][] heights = new int[WIDTH][HEIGHT];
}


