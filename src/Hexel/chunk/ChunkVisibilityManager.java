package Hexel.chunk;

import Hexel.rendering.Renderer;
import Hexel.rendering.Camera;

import Hexel.math.Vector2d;
import Hexel.math.Vector3i;
import Hexel.math.HexGeometry;

import Hexel.rendering.GLChunk;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.HashSet;

public class ChunkVisibilityManager implements LoadedChunksGetter {

    private Chunks chunks;
    private Renderer renderer;

    private HashSet<Vector3i> loadedChunks; public HashSet<Vector3i> getLoadedChunks(){ return this.loadedChunks; }

    public ChunkVisibilityManager(Chunks chunks, Renderer renderer){
        this.loadedChunks = new HashSet<Vector3i>();
        this.chunks = chunks;
        this.renderer = renderer;
    }

    public void updateVisibility(Camera camera){
        Vector2d pos = new Vector2d();
        HexGeometry.cartesianToHex(camera.getCameraX(), camera.getCameraY(), pos);

        int cx = (int)Math.floor(pos.x / 16.0);
        int cy = (int)Math.floor(pos.y / 16.0);
        int cz = (int)Math.floor(camera.getCameraZ() / 16.0);

        ArrayList<Vector3i> points = new ArrayList<Vector3i>();
        ArrayList<Vector3i> inRange = new ArrayList<Vector3i>();
        int n = 1;
        for (int x = -n; x <= n; x++){
            for (int y = -n; y <= n; y++){
                for (int z = -n; z <= n; z++){
                    int ncx = cx + x;
                    int ncy = cy + y;
                    int ncz = cz + z;
                    Vector3i cpos = new Vector3i(ncx, ncy, ncz);
                    inRange.add(cpos);
                    if (!loadedChunks.contains(cpos)){
                        points.add(cpos);
                    }
                    else {
                        Chunk chunk = this.chunks.getChunk(cpos);
                        if (chunk.dirty){
                            chunk.dirty = false;
                            points.add(cpos);
                        }

                    }
                    if (points.size() > 10){
                        renderer.loadChunks(points);
                        loadedChunks.addAll(points);
                        points = new ArrayList<Vector3i>();
                    }
                }
            }
        }
        renderer.loadChunks(points);
        loadedChunks.addAll(points);

        Iterator<Vector3i> iter = this.loadedChunks.iterator();
        ArrayList<Vector3i> toRemove = new ArrayList<Vector3i>();
        while (iter.hasNext()){
            Vector3i position = iter.next();

            if (!inRange.contains(position) && !points.contains(position)){
                toRemove.add(position);
            }
        }
        loadedChunks.removeAll(toRemove);

        HashMap<Vector3i, GLChunk> glChunkTable = renderer.getGLChunkTable();
        Iterator<Vector3i> it = new HashSet<Vector3i>(glChunkTable.keySet()).iterator();
        while (it.hasNext()){
            Vector3i p = it.next();
            if (!inRange.contains(p)){
                renderer.unloadGLChunk(p);
            }
        }
    }
}

