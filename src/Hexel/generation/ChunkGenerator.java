package Hexel.generation;

import Hexel.blocks.*;

import Hexel.math.Vector3i;
import Hexel.math.Vector2i;

import Hexel.chunk.Chunk;
import Hexel.chunk.Chunks;

import Hexel.generation.TerrainMap;

import Hexel.Cleanup;

import java.util.Random;

public class ChunkGenerator {

    private TerrainMap tm;
    private Chunks chunks;


    //public static GrassBlock gb = new GrassBlock();
    //public static StoneBlock sb = new StoneBlock();
    //public static EmptyBlock eb = new EmptyBlock();
    //public static WoodBlock wb = new WoodBlock();
    //public static SeedBlock seedb = new SeedBlock();

    public ChunkGenerator(Chunks chunks, Cleanup cleanup){
        tm = new TerrainMap(cleanup);
        this.chunks = chunks;
    }

    public Chunk genChunk(Vector3i pos){
        return this.genChunk(pos.x, pos.y, pos.z);
    }

    public Chunk genChunk(int cx, int cy, int cz){
        Chunk chunk = new Chunk(cx, cy, cz);

        Vector2i tmp = new Vector2i();
        
        boolean debug_mode = false;
        boolean trees = true;

        if (debug_mode){
          for (int x = 0; x < 32; x++){
              for (int y = 0; y < 16; y++){
                  for (int z = 0; z < 32; z++){
                      chunk.set(x, y, z, new EmptyBlock());
                      int gx = cx*32 + x;
                      int gy = cy*16 + y;
                      int gz = cz*32 + z;
                      if (gz < 0)
                          chunk.set(x, y, z, new GrassBlock());

                      //if (x == 2 && y == 1 && gz == 0)
                      //    chunk.set(x, y, z, gb);
                      //if (x == 3 && y == 1 && gz == 0)
                      //    chunk.set(x, y, z, gb);
                      //if (gz == 0 && x%2 == 0)
                      //    chunk.set(x, y, z, gb);
                  }
              }
          }
        }
        else {

          for (int x = 0; x < 32; x++){
              for (int y = 0; y < 16; y++){
                  for (int z = 0; z < 32; z++){
                      int gx = cx*32 + x;
                      int gy = cy*16 + y;
                      int gz = cz*32 + z;

                      int h = tm.getBlockHeight(gx, gy, tmp);

                      if (gz >= h && gz < 0){
                          WaterBlock waterb = WaterBlock.make(0, 8);
                          chunk.set(x, y, z, waterb);
                      }
                      else if (gz+3 < h){
                          chunk.set(x, y, z, new StoneBlock());
                      }
                      else if (gz < h){
                          chunk.set(x, y, z, new GrassBlock());
                      }
                      else {
                          chunk.set(x, y, z, new EmptyBlock());
                      }
                  }
              }
          }
        }

        if (trees){
          for (int x = 0; x < 32; x++){
              for (int y = 0; y < 16; y++){
                  int hash = 3;
                  int gx = cx*32 + x;
                  int gy = cy*16 + y;
                  hash = 97 * hash + gx;
                  hash = 97 * hash + gy;
                  hash = 97 * hash + cx;
                  hash = 97 * hash + cy;
                  hash = 97 * hash + x;
                  hash = 97 * hash + y;
                  Random random = new Random(hash);
                  if (random.nextDouble() < .01){
                      int gz = tm.getBlockHeight(gx, gy, tmp);
                      if (debug_mode)
                        gz = 0;
                      int z =  gz - cz*32;
                      if (z >= 0 && z < 32 && gz >= 0){
                          chunk.set(x, y, z, new SeedBlock());
                      }
                  }
              }
          }
        }

        chunk.stepsToNeedSim = 0;
        chunk.stepsToSim = 20;
        chunk.modified = false;

        return chunk;
    }

}

