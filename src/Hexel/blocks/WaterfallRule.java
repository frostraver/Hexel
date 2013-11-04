package Hexel.blocks;

import Hexel.blocks.BlockDelta;

import Hexel.chunk.Chunks;
import Hexel.chunk.Chunk;

import java.util.HashMap;
import java.util.ArrayList;

import Hexel.math.Vector3i;

import Hexel.util.Pair;

import Hexel.generation.ChunkGenerator;


public class WaterfallRule implements BlockRule {
    public int stepsUntilConsider(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
        return (3 - (steps % 3))%3;
    }
    public boolean isMatch(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
        WaterBlock wb = (WaterBlock)b;
        Block below = chunks.getBlock(x, y, z-1, tmp);
        if (wb.getBottom() != 0)
            return true;
        if (below instanceof EmptyBlock)
            return true;
        if (below instanceof WaterBlock){
            WaterBlock wBelow = (WaterBlock)below;
            if (wBelow.getTop() < 8)
                return true;
        }
        return false;
    }
    public ArrayList<BlockDelta> apply(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){

        ArrayList<BlockDelta> deltas = new ArrayList<BlockDelta>();
        WaterBlock wb = (WaterBlock)b;

        int wbBottom = wb.getBottom() - 4;
        int wbTop = wb.getTop() - 4;

        if (wbBottom >= 0){
            deltas.add(new BlockDelta(x, y, z, WaterBlock.make(wbBottom, wbTop), true));
        }
        else {
            Block below = chunks.getBlock(x, y, z-1, tmp);
            if (below instanceof EmptyBlock){
                int top = Math.min(8, 8+wbTop);
                int bottom = 8+wbBottom;
                WaterBlock replacement = WaterBlock.make(bottom, top);                
                deltas.add(new BlockDelta(x, y, z-1, replacement, true));
            }
            else if (below instanceof WaterBlock){
                WaterBlock belowWB = (WaterBlock)below;
                int h = Math.min(wbTop, 0) - wbBottom;
                int top = Math.min(8, belowWB.getTop() + h);
                WaterBlock replacement = WaterBlock.make(belowWB.getBottom(), top);
                deltas.add(new BlockDelta(x, y, z-1, replacement, true));
            }

            if (wbTop <= 0){
                deltas.add(new BlockDelta(x, y, z, new EmptyBlock(), true));
            }
            else {
                wbBottom = 0;
                deltas.add(new BlockDelta(x, y, z, WaterBlock.make(wbBottom, wbTop), true));
            }
        }
        return deltas;

    }
}


