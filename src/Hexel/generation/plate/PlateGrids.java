package Hexel.generation.plate;

import Hexel.math.Vector2i;

import java.util.concurrent.ConcurrentHashMap;

public class PlateGrids {
    private ConcurrentHashMap<Vector2i, int[][]> inMemoryPGs = new ConcurrentHashMap<Vector2i, int[][]>();
    private Object[] locks = new Object[100];

    PlateGridGenerator pgg;

    public PlateGrids() {
        pgg = new PlateGridGenerator();
        for (int i = 0; i < locks.length; i++){
            locks[i] = new Object();
        }
    }

    public int[][] get(int x, int y){
        return this.get(new Vector2i(x, y));
    }

    public int[][] get(Vector2i ppos){
        if (this.hasPGInMemory(ppos)){
            return inMemoryPGs.get(ppos);
        }
        maybeGenPG(ppos);
        return inMemoryPGs.get(ppos);
    }

    private void maybeGenPG(Vector2i ppos){
        int hash = Math.abs(ppos.hashCode() % locks.length);
        synchronized (locks[hash]){
            if (this.hasPGInMemory(ppos))
                return;

            int[][] pg = pgg.gen(ppos.x, ppos.y);
            inMemoryPGs.put(ppos, pg);
        }
    }

    private boolean hasPGInMemory(Vector2i ppos){
        return inMemoryPGs.containsKey(ppos);
    }

    public void unloadPG(Vector2i ppos){
        inMemoryPGs.remove(ppos);
    }

    public int getValue(int x, int y){
        int px = (int)Math.floor(x*1.0 / PlateChunk.WIDTH);
        int py = (int)Math.floor(y*1.0 / PlateChunk.HEIGHT);

        x -= px*PlateChunk.WIDTH;
        y -= py*PlateChunk.HEIGHT;

        return this.get(px, py)[x][y];
    }

    public Vector2i getBasin(int px, int py, int x, int y){
        int tx = px*PlateChunk.WIDTH + x;
        int ty = py*PlateChunk.HEIGHT + y;
        int i = 0;
        while (this.getValue(tx, ty) != 0 && i < 1000){
            i++;
            int minx = tx;
            int miny = ty;
            int min = this.getValue(tx, ty);
            for (int dx = -1; dx <= 1; dx++){
                for (int dy = -1; dy <= 1; dy++){
                    int lx = tx + dx;
                    int ly = ty + dy;
                    int v = this.getValue(lx, ly);
                    if (v < min){
                        min = v;
                        minx = lx;
                        miny = ly;
                    }
                }
            }
            tx = minx;
            ty = miny;
        }

        return new Vector2i(tx, ty);
    }

}


