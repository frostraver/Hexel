package Hexel.chunk;

import Hexel.generation.ChunkGenerator;

import Hexel.blocks.Block;

import Hexel.chunk.Chunk;
import Hexel.math.Vector3i;

import Hexel.Cleanup;

import java.util.concurrent.ConcurrentHashMap;

import java.util.Iterator;
import java.util.Map;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class Chunks {

    private ChunkGenerator chunkGenerator;
    LoadingCache<Vector3i, Chunk> cache;
    private Object[] locks = new Object[100];

    public Chunks(Cleanup cleanup, int numToBuffer) {
        cache = CacheBuilder.newBuilder()
           .maximumSize(numToBuffer)
           .removalListener(
                new RemovalListener<Vector3i, Chunk>(){
                    public void onRemoval(RemovalNotification<Vector3i, Chunk> notification){
                        Chunk c = notification.getValue();
                        if (c != null && c.modified){
                            ChunkFile.save(c);
                        }
                    }
                })
           .build(
                new CacheLoader<Vector3i, Chunk>() {
                    public Chunk load(Vector3i p) {
                        Chunk chunk;
                        if (ChunkFile.has(p)){
                            chunk = ChunkFile.load(p);
                        }
                        else {
                            chunk = chunkGenerator.genChunk(p);
                        }
                        return chunk;
                    }
                });


        this.chunkGenerator = new ChunkGenerator(this, cleanup);
        for (int i = 0; i < locks.length; i++){
            locks[i] = new Object();
        }

        cleanup.add(new Runnable(){
            public void run(){
                unloadAllChunks();
            }
        });
    }

    public Chunk getChunk(Vector3i cpos){
        try {
            return cache.get(cpos);
        } catch(Exception e){
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public void unloadAllChunks(){
        cache.invalidateAll();
    }

    public Block getBlock(int cx, int cy, int cz, int x, int y, int z, Vector3i p){
        int gx = cx*32 + x;
        int gy = cy*16 + y;
        int gz = cz*32 + z;

        return this.getBlock(gx, gy, gz, p);
    }

    public Block getBlock(int x, int y, int z, Vector3i p){
        int cx = (int)Math.floor(x / 32.0);
        int cy = (int)Math.floor(y / 16.0);
        int cz = (int)Math.floor(z / 32.0);

        int bx = x - cx*32;
        int by = y - cy*16;
        int bz = z - cz*32;

        p.x = cx;
        p.y = cy;
        p.z = cz;
        Chunk chunk = this.getChunk(p);
        return chunk.get(bx, by, bz);
    }

    public void awakenBlock(int x, int y, int z, Vector3i tmp){
        int cx = (int)Math.floor(x / 32.0);
        int cy = (int)Math.floor(y / 16.0);
        int cz = (int)Math.floor(z / 32.0);

        int bx = x - cx*32;
        int by = y - cy*16;
        int bz = z - cz*32;

        tmp.x = cx;
        tmp.y = cy;
        tmp.z = cz;
        Chunk chunk = this.getChunk(tmp);

        chunk.stepsToNeedSim = 0;
    }

    public void setBlock(int x, int y, int z, Block b, Vector3i p){
        int cx = (int)Math.floor(x / 32.0);
        int cy = (int)Math.floor(y / 16.0);
        int cz = (int)Math.floor(z / 32.0);

        int bx = x - cx*32;
        int by = y - cy*16;
        int bz = z - cz*32;

        p.x = cx;
        p.y = cy;
        p.z = cz;
        Chunk chunk = this.getChunk(p);
        chunk.set(bx, by, bz, b);
        chunk.dirty = true;
        chunk.stepsToNeedSim = 0;

        chunk.modified = true;

        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                for (int dz = -1; dz <= 1; dz++){
                    int x2 = x + dx;
                    int y2 = y + dy;
                    int z2 = z + dz;

                    p.x = (int)Math.floor(x2 / 32.0);
                    p.y = (int)Math.floor(y2 / 16.0);
                    p.z = (int)Math.floor(z2 / 32.0);

                    if ((p.x != cx || p.y != cy || p.z != cz)){
                        chunk = this.getChunk(p);
                        chunk.dirty = true;
                        chunk.stepsToNeedSim = 0;
                    }
                }
            }
        }
    }
}

