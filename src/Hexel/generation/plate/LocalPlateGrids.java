package Hexel.generation.plate;

import Hexel.math.Vector2i;

import java.util.concurrent.ConcurrentHashMap;

public class LocalPlateGrids {
    private ConcurrentHashMap<Vector2i, int[][]> inMemoryLPGs = new ConcurrentHashMap<Vector2i, int[][]>();
    private Object[] locks = new Object[100];

    public LocalPlateGrids() {
        for (int i = 0; i < locks.length; i++){
            locks[i] = new Object();
        }
    }

    public int[][] get(int x, int y){
        return this.get(new Vector2i(x, y));
    }

    public int[][] get(Vector2i ppos){
        if (this.hasLPGInMemory(ppos)){
            return inMemoryLPGs.get(ppos);
        }
        maybeGenLPG(ppos);
        return inMemoryLPGs.get(ppos);
    }

    private void maybeGenLPG(Vector2i ppos){
        int hash = Math.abs(ppos.hashCode() % locks.length);
        synchronized (locks[hash]){
            if (this.hasLPGInMemory(ppos))
                return;

            int[][] lpg = LocalPlateGridGenerator.gen(ppos.x, ppos.y);
            inMemoryLPGs.put(ppos, lpg);
        }
    }

    private boolean hasLPGInMemory(Vector2i ppos){
        return inMemoryLPGs.containsKey(ppos);
    }

    public void unloadLPG(Vector2i ppos){
        inMemoryLPGs.remove(ppos);
    }

    public int getValue(int x, int y){
        int px = (int)Math.floor(x*1.0 / PlateChunk.WIDTH);
        int py = (int)Math.floor(y*1.0 / PlateChunk.HEIGHT);

        x -= px*PlateChunk.WIDTH;
        y -= py*PlateChunk.HEIGHT;

        return this.get(px, py)[x][y];
    }

}

