package Hexel.generation.heightMap;

import java.util.Random;
import Hexel.math.Vector2i;

import Hexel.chunk.Chunk;

import Hexel.util.Generator;

import Hexel.math.Vector2i;
import Hexel.math.HexGeometry;

import Hexel.Cleanup;

public class HexBlockHeightMapGenerator implements Generator<HexBlockHeightMap, Vector2i> {

    private BlockHeightMaps bhms;

    public HexBlockHeightMapGenerator(Cleanup cleanup){
        bhms = new BlockHeightMaps(cleanup);
    }

    public HexBlockHeightMap gen(Vector2i cpos){
        HexBlockHeightMap hbhm = new HexBlockHeightMap();

        int cx = cpos.x*Chunk.WIDTH;
        int cy = cpos.y*Chunk.HEIGHT;

        Vector2i tmp = new Vector2i();

        int[][] hmap = hbhm.heights;

        Vector2i hex = new Vector2i();
        Vector2i tri = new Vector2i();
        
        for (int y = 0; y < 16; y++){
            for (int x = 0; x < 32; x ++){
                HexGeometry.tringleToHexel(cx+x, cy+y, hex);
                HexGeometry.hexelToTringle(hex.x, hex.y, tri);
                //int h = tri.x+tri.y;
                int h = bhms.getBlockHeight(tri.x, tri.y, tmp);
                hmap[x][y] = h;
            }
        }

        return hbhm;
    }
}


