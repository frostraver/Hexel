package Hexel;

import Hexel.chunk.Chunks;
import Hexel.chunk.Chunk;
import Hexel.chunk.LoadedChunksGetter;

import Hexel.math.Vector3i;
import Hexel.math.HexGeometry;

import Hexel.blocks.*;

import Hexel.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;

import Hexel.blocks.BlockDelta;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class WorldSimulator {

    private int steps = 0;

    private Chunks chunks;

    private LoadedChunksGetter loadedChunksGetter;

    private BlockRules blockRules;

    public WorldSimulator(Chunks chunks, LoadedChunksGetter loadedChunksGetter){
        this.chunks = chunks;
        this.loadedChunksGetter = loadedChunksGetter;
        this.blockRules = new BlockRules(chunks);
    }

    private ExecutorService ste = Executors.newSingleThreadExecutor();

    public void step(){

        final ArrayList<Vector3i> toUpdate = new ArrayList<Vector3i>();
        HashSet<Vector3i> loadedChunks = loadedChunksGetter.getLoadedChunks();
        Iterator<Vector3i> iter = loadedChunks.iterator();
        while (iter.hasNext()){
            Vector3i pos = iter.next();
            toUpdate.add(pos);
        }
        ste.execute(new Runnable(){
            @Override
            public void run(){
                boolean checkExtras = false;
                boolean goAgain = false;


                do {
                    ArrayList<BlockDelta> deltas = new ArrayList<BlockDelta>();


                    goAgain = false;
                    for (int i = 0; i < toUpdate.size(); i++){
                        Vector3i pos = toUpdate.get(i);

                        if (!checkExtras){
                            stepChunk(pos, deltas, false);
                        }
                        else {
                            Chunk chunk = WorldSimulator.this.chunks.getChunk(pos);
                            if (chunk.stepsToSim > 0){
                                chunk.stepsToSim -= 1;
                                goAgain = true;
                                stepChunk(pos, deltas, true);
                            }
                        }
                    }

                    Vector3i tmp = new Vector3i();
                    for (int i = 0; i < deltas.size(); i++){
                        BlockDelta delta = deltas.get(i);
                        if (!delta.immediate)
                            chunks.setBlock(delta.x, delta.y, delta.z, delta.block, tmp);
                        //if (delta.awakenNeighbors){
                        //  Vector3i[] neighbors;
                        //  if (delta.x%2 == 0){
                        //      neighbors = HexGeometry.evenAllNeighbors;
                        //  }
                        //  else {
                        //      neighbors = HexGeometry.oddAllNeighbors;
                        //  }
                        //  for (int j = 0; j < neighbors.length; j++){
                        //    Vector3i n = neighbors[j];
                        //    int x = delta.x + n.x;
                        //    int y = delta.y + n.y;
                        //    int z = delta.z + n.z;
                        //    chunks.awakenBlock(x, y, z, tmp);
                        //  }
                        //}
                    }

                    if (!checkExtras){
                        checkExtras = true;
                        goAgain = true;
                    }
                    if (!checkExtras || goAgain){
                        steps += 1;
                    }
                } while (!checkExtras || goAgain);
            }
        });


    }

    public void stepChunk(Vector3i p, ArrayList<BlockDelta> deltas, boolean disregardFreq){
        Chunk chunk = this.chunks.getChunk(p);


        if ((!disregardFreq && chunk.stepsToNeedSim != 0) || chunk.stepsToNeedSim == Integer.MAX_VALUE){
            if (chunk.stepsToNeedSim != Integer.MAX_VALUE)
                chunk.stepsToNeedSim -= 1;
            return;
        }
        chunk.stepsToNeedSim = Integer.MAX_VALUE;


        Vector3i tmp = new Vector3i();

        int cx = p.x;
        int cy = p.y;
        int cz = p.z;

        //System.out.println(("+======+"));

        for (int x = 0; x < 32; x++){
            for (int y = 0; y < 16; y++){
                for (int z = 0; z < 32; z++){
                    Block b = chunk.get(x, y, z);
                    int gx = cx*32 + x;
                    int gy = cy*16 + y;
                    int gz = cz*32 + z;


                    Pair<Integer, ArrayList<BlockDelta>> pair = this.blockRules.apply(gx, gy, gz, chunk, b, steps, disregardFreq, tmp);
                    ArrayList<BlockDelta> additionalDeltas = pair.second;
                    chunk.stepsToNeedSim = Math.min(pair.first, chunk.stepsToNeedSim);
                    if (additionalDeltas != null)
                        deltas.addAll(additionalDeltas);
                }
            }
        }
        //System.out.println((chunk.getXYZ())+ " " + ("|")+ " " + (steps)+ " " + ("|")+ " " + (chunk.stepsToNeedSim));
    }
}

