package Hexel.generation.heightMap;

import Hexel.generation.plate.PlateChunk;

public class SmoothHeightMapChunk implements java.io.Serializable {
    public static final int WIDTH = 17;
    public static final int HEIGHT = 17;
    int[][] heights = new int[WIDTH][HEIGHT];
}

