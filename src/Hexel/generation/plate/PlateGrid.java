package Hexel.generation.plate;

import java.util.LinkedList;
import java.util.Queue;
import Hexel.math.Vector2i;

public class PlateGrid {

    public static void wavefront(int[][] grid, int x, int y){
        Queue<Vector2i> toUpdate = new LinkedList<Vector2i>();

        toUpdate.add(new Vector2i(x, y));

        while (!toUpdate.isEmpty()){
            Vector2i p = toUpdate.poll();
            int val = grid[p.x][p.y];
            for (int dx = -1; dx <= 1; dx++){
                for (int dy = -1; dy <= 1; dy++){
                    int lx = p.x + dx;
                    int ly = p.y + dy;
                    if (lx < 0 || ly < 0 || lx >= PlateChunk.WIDTH || ly >= PlateChunk.HEIGHT)
                        continue;
                    if (val+1 < grid[lx][ly]){
                        grid[lx][ly] = 1+minNeighbor(grid, lx, ly);
                        toUpdate.add(new Vector2i(lx, ly));
                    }

                }
            }
        }
    }

    public static int minNeighbor(int[][] grid, int x, int y){
        int min = grid[x][y];
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                int lx = x + dx;
                int ly = y + dy;
                if (lx < 0 || ly < 0 || lx >= PlateChunk.WIDTH || ly >= PlateChunk.HEIGHT)
                    continue;
                if (grid[lx][ly] < min)
                    min = grid[lx][ly];
            }
        }
        return min;
    }

}

