package Hexel.chunk;

import Hexel.math.Vector3i;

import Hexel.blocks.Block;

import java.util.concurrent.atomic.AtomicBoolean;

public class Chunk implements java.io.Serializable{
    public static int WIDTH = 32;
    public static int HEIGHT = 16;
    public static int DEPTH = 32;

    private Vector3i pos;

    public boolean dirty;

    public int stepsToNeedSim;

    public int stepsToSim;

    public boolean modified;

    private Block[][][] blocks = new Block[32][16][32];

    public int cx;
    public int cy;
    public int cz;

    public Chunk(int cx, int cy, int cz){
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        pos = new Vector3i(cx, cy, cz);
    }
    
    public Block get(int x, int y, int z){
        return this.blocks[x][y][z];
    }

    public void set(int x, int y, int z, Block b){
        this.blocks[x][y][z] = b;
    }

    public Vector3i getXYZ(){
        return pos;
    }
}

