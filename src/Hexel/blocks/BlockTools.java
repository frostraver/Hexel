package Hexel.blocks;

import java.util.Queue;
import java.util.LinkedList;

import Hexel.chunk.Chunks;
import Hexel.math.Vector3i;
import Hexel.math.HexGeometry;
import Hexel.math.Vector2i;

import java.util.HashSet;

public class BlockTools {
  public static void pointFloodSearch(Vector3i start, PointFloodSearchMatcher matcher){
    Queue<Vector3i> toMatch = new LinkedList<Vector3i>();
    toMatch.add(start);
    HashSet<Vector3i> seen = new HashSet<Vector3i>();
    while (!toMatch.isEmpty()){
      Vector3i curr = toMatch.poll();
      if (!seen.contains(curr)){
        seen.add(curr);


        Vector3i[] neighbors;
        if (curr.x%2 == 0){
            neighbors = HexGeometry.evenAllNeighbors;
        }
        else {
            neighbors = HexGeometry.oddAllNeighbors;
        }

        for (int i = 0; i < neighbors.length; i++){
          Vector3i n = neighbors[i];
          Vector3i next = new Vector3i();
          next.x = curr.x + n.x;
          next.y = curr.y + n.y;
          next.z = curr.z + n.z;
          if (!seen.contains(next)){
            if (matcher.matches(next)){
              toMatch.add(next);
            }
            else {
              seen.add(next);
            }
          }
        }
      }
    }
  }

  public interface PointFloodSearchMatcher {
    public boolean matches(Vector3i p);
  }
}

