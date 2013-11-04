package Hexel.blocks;

import Hexel.blocks.BlockDelta;

import Hexel.chunk.Chunks;
import Hexel.chunk.Chunk;

import java.util.HashMap;
import java.util.ArrayList;

import Hexel.math.Vector3i;
import Hexel.math.HexGeometry;
import Hexel.math.Vector2i;

import Hexel.util.Pair;
import Hexel.util.Container;

import Hexel.generation.ChunkGenerator;


public class WaterspreadRule implements BlockRule {
    public int stepsUntilConsider(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
        return (5 - (steps % 5))%5;
    }
    public boolean isMatch(final int x, final int y, final int z, Block b, Chunk c, final Chunks chunks, int steps, Vector3i tmp){

        WaterBlock wb = (WaterBlock)b;
        Block below = chunks.getBlock(x, y, z-1, tmp);
        if (wb.getBottom() != 0)
            return false;
        if (below instanceof EmptyBlock)
            return false;
        if (below instanceof WaterBlock){
            WaterBlock wBelow = (WaterBlock)below;
            if (wBelow.getTop() < 8 || wBelow.getBottom() != 0)
                return false;
        }

        final int startBlockH = wb.getTop();

        final Container<Pair<Vector3i, Integer>> target = new Container<Pair<Vector3i, Integer>>();
        target.contents = new Pair<Vector3i, Integer>(new Vector3i(x, y, z), startBlockH);

        BlockTools.pointFloodSearch(target.contents.first, new BlockTools.PointFloodSearchMatcher(){

          private int numSeen = 0;
          private Vector3i tmp = new Vector3i();

          public boolean matches(Vector3i p){
            Block b = chunks.getBlock(p.x, p.y, p.z, tmp);


            Integer blockWaterLevel = null;

            boolean match = false;

            if (b instanceof EmptyBlock && p.z == z){
              Block bl = chunks.getBlock(p.x, p.y, p.z-1, tmp);
              if (bl instanceof EmptyBlock || 
                  bl instanceof WaterBlock){
                b = bl;
                p.z -= 1;
              }
            }

            if (b instanceof EmptyBlock){
              blockWaterLevel = 0;
            }
            else if (b instanceof WaterBlock){
              this.numSeen += 1;
              WaterBlock wb = (WaterBlock)b;
              if (wb.getBottom() != 0)
                blockWaterLevel = wb.getBottom();
              else if (wb.getTop() < 8){
               blockWaterLevel = wb.getTop();
              }
              if (blockWaterLevel != null){
                match = p.z*8 + blockWaterLevel <= z*8 + startBlockH;
              }
            }

            if (blockWaterLevel != null){
              int currZ = target.contents.first.z*8 + target.contents.second;
              int nextZ = p.z*8 + blockWaterLevel;
              if (nextZ+1 < currZ){// (currZ == nextZ && b instanceof WaterBlock && p.x < target.contents.first.x && p.y < target.contents.first.y)){
                target.contents.first = p;
                target.contents.second = blockWaterLevel;
              }
            }

            //System.out.println(("\tMATCH?")+ " " + (x)+ " " + (y)+ " " + (z)+ " " + (startBlockH)+ " " + ("->")+ " " + (p)+ " " + (blockWaterLevel)+ " " + (match));

            if (this.numSeen > 100)
              return false;


            return match;
          }
        });


        if (
            target.contents.first.x == x &&
            target.contents.first.y == y &&
            target.contents.first.z == z
        ){
          //System.out.println(("NOMATCH")+ " " + (x)+ " " + (y)+ " " + (z)+ " " + (startBlockH)+ " " + ("->")+ " " + (target.contents.first)+ " " + (target.contents.second));
          return false;
        }
        else {
          //System.out.println(("MATCH")+ " " + (x)+ " " + (y)+ " " + (z)+ " " + (startBlockH)+ " " + ("->")+ " " + (target.contents.first)+ " " + (target.contents.second));
          wb.target = target.contents.first;
          return true;
        }
        /* TODO figure out a way to cache this */
    }
    public ArrayList<BlockDelta> apply(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){

        WaterBlock wb = (WaterBlock)b;
        int bh = wb.getTop() - wb.getBottom();

        ArrayList<BlockDelta> deltas = new ArrayList<BlockDelta>();

        if (wb.target != null){
          //System.out.println(("APPLY")+ " " + (x)+ " " + (y)+ " " + (z)+ " " + ("->")+ " " + (wb.target));
          Vector3i t = wb.target;
          Block tb = chunks.getBlock(t.x, t.y, t.z, tmp);
          int tgtBH = 0;
          if (tb instanceof WaterBlock){
            WaterBlock twb = (WaterBlock)tb;
            tgtBH = twb.getTop() - twb.getBottom();
          }
          if (bh > 1){
            int newTgtHeight = Math.min(8, tgtBH + bh/2);
            int newSrcHeight = bh+tgtBH - newTgtHeight;
            if (z*8 + newSrcHeight < t.z*8 + newTgtHeight){
              int diff = t.z*8 + newTgtHeight - (z*8 + newSrcHeight);
              newTgtHeight -= diff/2;
              newSrcHeight += diff/2;
            }
            //System.out.println((x)+ " " + (y)+ " " + (z)+ " " + ("->")+ " " + (wb.target)+ " " + (",")+ " " + (bh)+ " " + (tgtBH)+ " " + ("|")+ " " + (newTgtHeight)+ " " + (newSrcHeight));
            if (newSrcHeight == 0)
              deltas.add(new BlockDelta(x, y, z, new EmptyBlock(), true));
            else
              deltas.add(new BlockDelta(x, y, z, WaterBlock.make(0, newSrcHeight), true));
            deltas.add(new BlockDelta(t.x, t.y, t.z, WaterBlock.make(0, newTgtHeight), true));
          }
          else if (bh == 1 && t.z*8 + tgtBH+1 < z*8 + bh){
            deltas.add(new BlockDelta(x, y, z, new EmptyBlock(), true));
            deltas.add(new BlockDelta(t.x, t.y, t.z, WaterBlock.make(0, tgtBH+1), true));
          }
        }

        return deltas;

    }
}

