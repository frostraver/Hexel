package Hexel.blocks;

import Hexel.blocks.BlockDelta;

import Hexel.chunk.Chunks;
import Hexel.chunk.Chunk;

import java.util.HashMap;
import java.util.ArrayList;

import Hexel.math.Vector3i;

import Hexel.util.Pair;

import Hexel.generation.ChunkGenerator;

import java.util.Random;


public class BlockRules {

    private Chunks chunks;

    ArrayList<BlockRule> everywhereRules = new ArrayList<BlockRule>();
    ArrayList<BlockRule> seedRules = new ArrayList<BlockRule>();
    ArrayList<BlockRule> woodRules = new ArrayList<BlockRule>();
    ArrayList<BlockRule> waterRules = new ArrayList<BlockRule>();

    public BlockRules(Chunks chunks){
        this.chunks = chunks;

        everywhereRules.add(new BlockRule(){
            public int stepsUntilConsider(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                return (5 - (steps % 5))%5;
            }
            public boolean isMatch(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
              return b.health < b.getMaxHealth();
            }
            public ArrayList<BlockDelta> apply(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                ArrayList<BlockDelta> deltas = new ArrayList<BlockDelta>();
                b.health += .01;
                deltas.add(new BlockDelta(x, y, z, b));
                return deltas;
            }
        });
        seedRules.addAll(everywhereRules);
        woodRules.addAll(everywhereRules);
        waterRules.addAll(everywhereRules);

        seedRules.add(new BlockRule(){
            public int stepsUntilConsider(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                return (1000 - (steps % 1000))%1000;
            }
            public boolean isMatch(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                return true;
            }
            public ArrayList<BlockDelta> apply(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                ArrayList<BlockDelta> deltas = new ArrayList<BlockDelta>();
                WoodBlock woodBlock = new WoodBlock();
                woodBlock.height = 0;
                woodBlock.maxHeight = 5;
                deltas.add(new BlockDelta(x, y, z, woodBlock));
                return deltas;
            }
        });
        woodRules.add(new BlockRule(){
            public int stepsUntilConsider(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                return (500 - (steps % 500))%500;
            }
            public boolean isMatch(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                if (!(chunks.getBlock(x, y, z+1, tmp) instanceof EmptyBlock ||
                      chunks.getBlock(x, y, z+1, tmp) instanceof LeafBlock))
                    return false;
                WoodBlock wb = (WoodBlock)b;
                return wb.height < wb.maxHeight;
            }
            public ArrayList<BlockDelta> apply(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                ArrayList<BlockDelta> deltas = new ArrayList<BlockDelta>();
                WoodBlock srcwb = (WoodBlock)b;

                int h = x << 16 + y << 8 + z;
                Random random = new Random(h);

                WoodBlock woodBlock = new WoodBlock();
                woodBlock.maxHeight = (int)(5 + random.nextDouble()*7);
                woodBlock.height = srcwb.height+1;
                deltas.add(new BlockDelta(x, y, z+1, woodBlock));
                return deltas;
            }
        });
        for (int x = -1; x <= 1; x++){
            for (int y = -1; y <= 1; y++){
                for (int z = -1; z <= 1; z++){
                    final int xo = x;
                    final int yo = y;
                    final int zo = z;
                    woodRules.add(new BlockRule(){
                        public int stepsUntilConsider(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                            return (500 - (steps % 500))%500;
                        }
                        public boolean isMatch(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                            if (!(chunks.getBlock(x+xo, y+yo, z+zo, tmp) instanceof EmptyBlock))
                                return false;
                            WoodBlock wb = (WoodBlock)b;
                            int h = (x+xo) << 16 + (y+yo) << 8 + (z+zo);
                            Random random = new Random(h);
                            return wb.height > 3 && random.nextDouble() < .8;
                        }
                        public ArrayList<BlockDelta> apply(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
                            ArrayList<BlockDelta> deltas = new ArrayList<BlockDelta>();
                            LeafBlock leafBlock = new LeafBlock();
                            deltas.add(new BlockDelta(x+xo, y+yo, z+zo, leafBlock));
                            return deltas;
                        }
                    });
                }
            }
        }
        waterRules.add(new WaterfallRule());
        waterRules.add(new WaterspreadRule());
    }

    public Pair<Integer, ArrayList<BlockDelta>> apply(int x, int y, int z, Chunk c, Block b, int steps, boolean disregardFreq, Vector3i tmp){
        ArrayList<BlockRule> rules = null;
        if (b instanceof SeedBlock){
            rules = seedRules;
        }
        else if (b instanceof WoodBlock){
            rules = woodRules;
        }
        else if (b instanceof WaterBlock){
            rules = waterRules;
        }
        ArrayList<BlockDelta> deltas = null;
        int minStepsUntilConsider = Integer.MAX_VALUE;
        if (rules == null)
          rules = everywhereRules;
        deltas = new ArrayList<BlockDelta>();
        for (int i = 0; i < rules.size(); i++){
            BlockRule rule = rules.get(i);

            int stepsUntilConsider = rule.stepsUntilConsider(x, y, z, b, c, chunks, steps, tmp);
            if (disregardFreq || stepsUntilConsider == 0){
              boolean isMatch = rule.isMatch(x, y, z, b, c, chunks, steps, tmp);
              if (isMatch){
                ArrayList<BlockDelta> additionalDeltas = rule.apply(x, y, z, b, c, chunks, steps, tmp);
                deltas.addAll(additionalDeltas);
                for (int j = 0; j < deltas.size(); j++){
                    BlockDelta delta = deltas.get(j);
                    if (delta.immediate){
                        chunks.setBlock(delta.x, delta.y, delta.z, delta.block, tmp);
                    }
                }
                if (deltas.size() > 0){
                  stepsUntilConsider = rule.stepsUntilConsider(x, y, z, b, c, chunks, steps+1, tmp);
                  minStepsUntilConsider = Math.min(minStepsUntilConsider, stepsUntilConsider);
                }
              }
            }
            else {
              stepsUntilConsider = rule.stepsUntilConsider(x, y, z, b, c, chunks, steps+1, tmp);
              minStepsUntilConsider = Math.min(minStepsUntilConsider, stepsUntilConsider);
            }
        }
        return new Pair<Integer, ArrayList<BlockDelta>>(minStepsUntilConsider, deltas);
    }
}

