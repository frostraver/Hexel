package Hexel.generation.heightMap;

import java.util.Random;
import Hexel.math.Vector2i;

import Hexel.chunk.Chunk;

import Hexel.util.Generator;

import Hexel.math.Vector2i;

import Hexel.Cleanup;


public class BlockHeightMapGenerator implements Generator<BlockHeightMap, Vector2i> {

    private SmoothHeightMapChunks shmcs;

    public BlockHeightMapGenerator(Cleanup cleanup){
        shmcs = new SmoothHeightMapChunks(cleanup);
    }

    public BlockHeightMap gen(Vector2i bhmp){
        int bhmx = bhmp.x * 2;
        int bhmy = bhmp.y * 2;
        BlockHeightMap bhm = new BlockHeightMap();

        Vector2i tmp = new Vector2i();

        int[][] heights = new int[3][3];
        for (int x = 0; x <= 2; x++){
            for (int y = 0; y <= 2; y++){
                heights[x][y] = shmcs.getHeight(bhmx+x, bhmy+y, tmp);
            }
        }

        int[][] hmap = new int[33*2][17*2];

        hmap[0][0] = heights[0][0];
        hmap[32][0] = heights[1][0];
        hmap[0][16] = heights[0][1];
        hmap[32][16] = heights[1][1];
        diamondSquare(  genRandom(bhmx, bhmy),
                        genRandom(0, 0), genRandom(0, 0),
                        genRandom(0, 0), genRandom(0, 0),
                        0, 0, 32, 16, hmap);

        hmap[33][0] = heights[1][0];
        hmap[33+32][0] = heights[2][0];
        hmap[33][16] = heights[1][1];
        hmap[33+32][16] = heights[2][1];
        diamondSquare(  genRandom(bhmx+1, bhmy),
                        genRandom(0, 0), genRandom(0, 0),
                        genRandom(0, 0), genRandom(0, 0),
                        33, 0, 32, 16, hmap);

        hmap[0][17] = heights[0][1];
        hmap[32][17] = heights[1][1];
        hmap[0][33] = heights[0][2];
        hmap[32][33] = heights[1][2];
        diamondSquare(  genRandom(bhmx, bhmy+1),
                        genRandom(0, 0), genRandom(0, 0),
                        genRandom(0, 0), genRandom(0, 0),
                        0, 17, 32, 16, hmap);

        hmap[33][17] = heights[1][1];
        hmap[33+32][17] = heights[2][1];
        hmap[33][33] = heights[1][2];
        hmap[33+32][33] = heights[2][2];
        diamondSquare(  genRandom(bhmx+1, bhmy+1),
                        genRandom(0, 0), genRandom(0, 0),
                        genRandom(0, 0), genRandom(0, 0),
                        33, 17, 32, 16, hmap);



        bhm.heights = hmap;
        return bhm;
    }

    public Random genRandom(int x, int y){
        return new Random(x*31 + y*23);
    }

    public void diamondSquare(
        Random random,
        Random rl, Random rt, Random rr, Random rb,
        int l, int t, int w, int h, int[][] hmap
    ){
        int nw = w/2;
        int nh = h/2;

        if (nh == 0 && nw == 0)
            return;

        int r = l + w;
        int b = t + h;

        double maxR = Math.sqrt(Math.abs(w*h))*0;
        int rand = (int)(rt.nextDouble()*maxR - maxR/2);
        hmap[l + nw][t] = (hmap[l][t] + hmap[r][t])/2+rand;
        rand = (int)(rb.nextDouble()*maxR - maxR/2);
        hmap[l + nw][b] = (hmap[l][b] + hmap[r][b])/2+rand;

        rand = (int)(rl.nextDouble()*maxR - maxR/2);
        hmap[l][t + nh] = (hmap[l][t] + hmap[l][b])/2+rand;
        rand = (int)(rr.nextDouble()*maxR - maxR/2);
        hmap[r][t + nh] = (hmap[r][t] + hmap[r][b])/2+rand;



        rand = (int)(random.nextDouble()*maxR - maxR/2);
        hmap[l + nw][t + nh] = (hmap[l][t] + hmap[l][b] + hmap[r][t] + hmap[r][b])/4 + rand;

        diamondSquare(  random, 
                        rl, rt, random, random,
                        l, t, nw, nh, hmap);
        diamondSquare(  random, 
                        rl, random, random, rb,
                        l, t+nh, nw, nh, hmap);
        diamondSquare(  random,
                        random, rt, rr, random,
                        l+nw, t, nw, nh, hmap);
        diamondSquare(  random, 
                        random, random, rr, rb,
                        l+nw, t+nh, nw, nh, hmap);
    }
}


