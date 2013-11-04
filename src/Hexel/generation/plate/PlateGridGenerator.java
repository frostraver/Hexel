package Hexel.generation.plate;

import Hexel.math.Vector2i;

import java.util.Random;

public class PlateGridGenerator {

    private LocalPlateGrids lpgs;

    public PlateGridGenerator(){
        lpgs = new LocalPlateGrids();
    }

    public int[][] gen(int px, int py){
        int[][] grid = lpgs.get(px, py);
        putGridInContext(grid, px, py);
        return grid;
    }

    private void putGridInContext(int[][] grid, int px, int py){
        for (int x = 0; x < PlateChunk.WIDTH; x++){
            for (int y = 0; y < PlateChunk.HEIGHT; y++){
                if (grid[x][y] != 0)
                    grid[x][y] = Integer.MAX_VALUE;
            }
        }


        int l = px*PlateChunk.WIDTH;
        int r = (px+1)*PlateChunk.WIDTH-1;
        int t = py*PlateChunk.HEIGHT;
        int b = (py+1)*PlateChunk.HEIGHT-1;


        for (int x = 1; x < PlateChunk.WIDTH-1; x++){
            grid[x][0] =                    1 + lpgs.getValue(l+x, t-1);
            grid[x][PlateChunk.HEIGHT-1] =  1 + lpgs.getValue(l+x, b+1);
        }
        for (int y = 1; y < PlateChunk.HEIGHT-1; y++){
            grid[0][y] =                    1 + lpgs.getValue(l-1, t+y);
            grid[PlateChunk.WIDTH-1][y] =   1 + lpgs.getValue(r+1, t+y);
        }

        grid[0][0] = 1 + Math.min(
                        Math.min(   lpgs.getValue(l-1, t-1),
                                    lpgs.getValue(l-1, t)),
                        lpgs.getValue(l, t-1));
        grid[PlateChunk.WIDTH-1][0] = 1 + Math.min(
                        Math.min(   lpgs.getValue(r+1, t-1),
                                    lpgs.getValue(r+1, t)),
                        lpgs.getValue(r, t-1));
        grid[0][PlateChunk.HEIGHT-1] = 1 + Math.min(
                        Math.min(   lpgs.getValue(l-1, b+1),
                                    lpgs.getValue(l-1, b)),
                        lpgs.getValue(l, b+1));
        grid[PlateChunk.WIDTH-1][PlateChunk.HEIGHT-1] = 1 + Math.min(
                        Math.min(   lpgs.getValue(r+1, b+1),
                                    lpgs.getValue(r+1, b)),
                        lpgs.getValue(r, b+1));

        PlateGrid.wavefront(grid, 0, 0);

    }

}
 

