package Hexel.generation.plate;

import Hexel.math.Vector2i;

import java.util.concurrent.ConcurrentHashMap;

public class PlateChunks {
    private ConcurrentHashMap<Vector2i, PlateChunk> inMemoryPCs = new ConcurrentHashMap<Vector2i, PlateChunk>();
    private Object[] locks = new Object[100];

    PlateChunkGenerator pcg;

    public PlateChunks() {
        pcg = new PlateChunkGenerator();
        for (int i = 0; i < locks.length; i++){
            locks[i] = new Object();
        }
    }

    public PlateChunk get(int x, int y){
        return this.get(new Vector2i(x, y));
    }

    public PlateChunk get(Vector2i ppos){
        if (this.hasPCInMemory(ppos)){
            return inMemoryPCs.get(ppos);
        }
        maybeGenPC(ppos);
        return inMemoryPCs.get(ppos);
    }

    private void maybeGenPC(Vector2i ppos){
        int hash = Math.abs(ppos.hashCode() % locks.length);
        synchronized (locks[hash]){
            if (this.hasPCInMemory(ppos))
                return;

            PlateChunk pc = pcg.gen(ppos.x, ppos.y);
            inMemoryPCs.put(ppos, pc);
        }
    }

    private boolean hasPCInMemory(Vector2i ppos){
        return inMemoryPCs.containsKey(ppos);
    }

    public void unloadPC(Vector2i ppos){
        inMemoryPCs.remove(ppos);
    }

    public Plate getPlate(int x, int y){
        int px = (int)Math.floor(x*1.0 / PlateChunk.WIDTH);
        int py = (int)Math.floor(y*1.0 / PlateChunk.HEIGHT);

        x -= px*PlateChunk.WIDTH;
        y -= py*PlateChunk.HEIGHT;

        return this.get(px, py).plates[x][y];
    }

}


