package Hexel.blocks;

import Hexel.chunk.Chunks;
import Hexel.chunk.Chunk;

import Hexel.math.Vector3i;

import java.util.ArrayList;

public interface BlockRule {
    public abstract int stepsUntilConsider(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp);
    public abstract boolean isMatch(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp);
    public abstract ArrayList<BlockDelta> apply(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp);
}

