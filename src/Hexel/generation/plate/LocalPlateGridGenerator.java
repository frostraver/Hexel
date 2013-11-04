package Hexel.generation.plate;

import java.util.LinkedList;
import java.util.Queue;
import Hexel.math.Vector2i;

import java.util.Random;

public class LocalPlateGridGenerator {

    public static int[][] gen(int x, int y){
        Random random = new Random(5 + x << 16 + y);
        int[][] grid = genPlateGrid(random, 2, 4, 2);
        return grid;
    }

    private static int[][] genPlateGrid(Random random, int minPlateDist, double avgPlateDist, double plateDistStdDev){
        int[][] grid = new int[PlateChunk.WIDTH][PlateChunk.HEIGHT];
        for (int x = 0; x < PlateChunk.WIDTH; x++){
            for (int y = 0; y < PlateChunk.HEIGHT; y++){
                grid[x][y] = Integer.MAX_VALUE;
            }
        }

        for (int x = 0; x < PlateChunk.WIDTH; x++)
            grid[x][0] = minPlateDist/2;
        for (int x = 0; x < PlateChunk.WIDTH; x++)
            grid[x][PlateChunk.HEIGHT-1] = minPlateDist/2;
        for (int y = 0; y < PlateChunk.HEIGHT; y++)
            grid[0][y] = minPlateDist/2;
        for (int y = 0; y < PlateChunk.HEIGHT; y++)
            grid[PlateChunk.WIDTH-1][y] = minPlateDist/2;

        PlateGrid.wavefront(grid, 0, 0);

        while(true){
            int plateDist = (int)Math.max(minPlateDist, avgPlateDist + random.nextGaussian()*plateDistStdDev);
            Vector2i p = select(random, grid, plateDist);
            if (p == null)
                break;
            grid[p.x][p.y] = 0;
            PlateGrid.wavefront(grid, p.x, p.y);
        }

        Vector2i p = new Vector2i();
        for (int x = 0; x < PlateChunk.WIDTH; x++){
            for (int y = 0; y < PlateChunk.HEIGHT; y++){
                if (grid[x][y] != 0){
                    grid[x][y] = Integer.MAX_VALUE;
                }
                else {
                    p.x = x;
                    p.y = y;
                }
            }
        }

        PlateGrid.wavefront(grid, p.x, p.y);

        return grid;
    }

    private static Vector2i select(Random random, int[][] grid, int dist){
        Vector2i p = new Vector2i();
        int n = 0;
        for (int x = 0; x < PlateChunk.WIDTH; x++){
            for (int y = 0; y < PlateChunk.HEIGHT; y++){
                int gdist = grid[x][y];
                if (gdist >= dist){
                    n++;
                    if (random.nextDouble()*n < 1){
                        p.x = x;
                        p.y = y;
                    }
                }
            }
        }
        if (n == 0)
            return null;
        return p;
    }


}


