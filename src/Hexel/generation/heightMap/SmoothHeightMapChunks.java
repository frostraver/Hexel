package Hexel.generation.heightMap;

import Hexel.math.Vector2i;
import Hexel.generation.plate.PlateChunk;

import Hexel.Cleanup;

import java.util.concurrent.ConcurrentHashMap;

import java.util.Iterator;
import java.util.Map;

public class SmoothHeightMapChunks {
    private ConcurrentHashMap<Vector2i, SmoothHeightMapChunk> inMemorySHMCs = new ConcurrentHashMap<Vector2i, SmoothHeightMapChunk>();
    private Object[] locks = new Object[100];

    SmoothHeightMapChunkGenerator shmcg;

    public SmoothHeightMapChunks(Cleanup cleanup) {
        shmcg = new SmoothHeightMapChunkGenerator();
        for (int i = 0; i < locks.length; i++){
            locks[i] = new Object();
        }
        cleanup.add(new Runnable(){
            public void run(){
                saveAll();
            }
        });
    }

    public SmoothHeightMapChunk get(Vector2i ppos){
        if (this.hasSHMCInMemory(ppos)){
            return inMemorySHMCs.get(ppos);
        }
        maybeGenSHMC(ppos);
        return inMemorySHMCs.get(ppos);
    }


    private void maybeGenSHMC(Vector2i ppos){
        int hash = Math.abs(ppos.hashCode() % locks.length);
        synchronized (locks[hash]){
            if (this.hasSHMCInMemory(ppos))
                return;

            SmoothHeightMapChunk shmc;
            if (SHMCFile.has(ppos)){
                shmc = SHMCFile.load(ppos);
            }
            else {
                shmc = shmcg.gen(ppos.x, ppos.y);
            }
            inMemorySHMCs.put(new Vector2i(ppos), shmc);
        }
    }

    public void saveAll(){
        Iterator<Map.Entry<Vector2i, SmoothHeightMapChunk>> it = inMemorySHMCs.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Vector2i, SmoothHeightMapChunk> entry = it.next();
            if (!SHMCFile.has(entry.getKey())){
                SHMCFile.save(entry.getKey(), entry.getValue());
            }
        }
    }

    private boolean hasSHMCInMemory(Vector2i ppos){
        return inMemorySHMCs.containsKey(ppos);
    }

    public void unloadSHMC(Vector2i ppos){
        inMemorySHMCs.remove(ppos);
    }

    public int getHeight(int x, int y, Vector2i p){
        int px = (int)Math.floor(x*1.0 / SmoothHeightMapChunk.WIDTH);
        int py = (int)Math.floor(y*1.0 / SmoothHeightMapChunk.HEIGHT);

        x -= px*SmoothHeightMapChunk.WIDTH;
        y -= py*SmoothHeightMapChunk.HEIGHT;

        p.x = px;
        p.y = py;
        return this.get(p).heights[x][y];
    }

}


