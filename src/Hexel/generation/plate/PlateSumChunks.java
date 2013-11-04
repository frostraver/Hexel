package Hexel.generation.plate;

import Hexel.math.Vector2i;

import java.util.concurrent.ConcurrentHashMap;

public class PlateSumChunks {
    private ConcurrentHashMap<Vector2i, PlateSumChunk> inMemoryPSCs = new ConcurrentHashMap<Vector2i, PlateSumChunk>();
    private Object[] locks = new Object[100];

    PlateSumChunkGenerator pscg;

    public PlateSumChunks() {
        pscg = new PlateSumChunkGenerator();
        for (int i = 0; i < locks.length; i++){
            locks[i] = new Object();
        }
    }

    public PlateSumChunk get(int x, int y){
        return this.get(new Vector2i(x, y));
    }

    public PlateSumChunk get(Vector2i ppos){
        if (this.hasPSCInMemory(ppos)){
            return inMemoryPSCs.get(ppos);
        }
        maybeGenPSC(ppos);
        return inMemoryPSCs.get(ppos);
    }

    private void maybeGenPSC(Vector2i ppos){
        int hash = Math.abs(ppos.hashCode() % locks.length);
        synchronized (locks[hash]){
            if (this.hasPSCInMemory(ppos))
                return;

            PlateSumChunk psc = pscg.gen(ppos.x, ppos.y);
            inMemoryPSCs.put(ppos, psc);
        }
    }

    private boolean hasPSCInMemory(Vector2i ppos){
        return inMemoryPSCs.containsKey(ppos);
    }

    public void unloadPSC(Vector2i ppos){
        inMemoryPSCs.remove(ppos);
    }

    public PlateSum getPlateSum(int x, int y){
        int px = (int)Math.floor(x*1.0 / PlateChunk.WIDTH);
        int py = (int)Math.floor(y*1.0 / PlateChunk.HEIGHT);

        x -= px*PlateChunk.WIDTH;
        y -= py*PlateChunk.HEIGHT;

        return this.get(px, py).plateSums[x][y];
    }

}


