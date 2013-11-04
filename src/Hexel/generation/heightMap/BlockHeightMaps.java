package Hexel.generation.heightMap;

import Hexel.math.Vector2i;
import Hexel.generation.plate.PlateChunk;

import Hexel.chunk.Chunk;

import Hexel.util.PagedCache;

import java.util.concurrent.ConcurrentHashMap;

import Hexel.Cleanup;

public class BlockHeightMaps extends PagedCache<Vector2i, BlockHeightMap> {
    public BlockHeightMaps(Cleanup cleanup){
        super(new BlockHeightMapGenerator(cleanup));
    }

    public Vector2i copyKey(Vector2i key){
        return new Vector2i(key);
    }

    public int getBlockHeight(int x, int y, Vector2i tmp){
        tmp.x = (int)Math.floor(x/(Chunk.WIDTH*2.0));
        tmp.y = (int)Math.floor(y/(Chunk.HEIGHT*2.0));

        int lx = 1 + x - tmp.x*Chunk.WIDTH*2;
        int ly = 1 + y - tmp.y*Chunk.HEIGHT*2;
        if (x%(Chunk.WIDTH*2) > Chunk.WIDTH)
            x += Chunk.WIDTH;
        if (y%(Chunk.HEIGHT*2) > Chunk.HEIGHT)
            y += Chunk.HEIGHT;
        BlockHeightMap bhm = this.get(tmp);
        return bhm.heights[lx][ly];
    }

}

