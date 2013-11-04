package Hexel.generation.heightMap;

import Hexel.math.Vector2i;
import Hexel.generation.plate.PlateChunk;

import Hexel.chunk.Chunk;

import Hexel.util.PagedCache;

import java.util.concurrent.ConcurrentHashMap;

import Hexel.Cleanup;

public class HexBlockHeightMaps extends PagedCache<Vector2i, HexBlockHeightMap> {
    public HexBlockHeightMaps(Cleanup cleanup){
        super(new HexBlockHeightMapGenerator(cleanup));
    }

    public Vector2i copyKey(Vector2i key){
        return new Vector2i(key);
    }

    public int getBlockHeight(int x, int y, Vector2i tmp){


        int cx = (int)Math.floor(x / 32.0);
        int cy = (int)Math.floor(y / 16.0);

        int bx = x - cx*32;
        int by = y - cy*16;

        tmp.x = cx;
        tmp.y = cy;
        HexBlockHeightMap hbhm = this.get(tmp);
        return hbhm.heights[bx][by];
    }

}

