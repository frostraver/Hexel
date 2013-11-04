package Hexel.generation.plate;

import Hexel.math.Vector2d;
import Hexel.math.Vector2i;

import java.util.Random;

public class PlateSumChunkGenerator {

    PlateChunks plateChunks;

    public PlateSumChunkGenerator(){
        plateChunks = new PlateChunks();
    }

    public PlateSumChunk gen(int px, int py){
        PlateSumChunk psc = new PlateSumChunk();

        for (int x = 0; x < PlateChunk.WIDTH; x++){
            for (int y = 0; y < PlateChunk.HEIGHT; y++){
                psc.plateSums[x][y] = new PlateSum();
            }
        }

        for (int x = -Plate.MAX_OFFSET; x < PlateChunk.WIDTH+Plate.MAX_OFFSET; x++){
            for (int y = -Plate.MAX_OFFSET; y < PlateChunk.HEIGHT+Plate.MAX_OFFSET; y++){
                Plate p = plateChunks.getPlate(px*PlateChunk.WIDTH + x, py*PlateChunk.HEIGHT + y);

                Random random = new Random((x+px) << 16 + (y+py));
                
                if (x >= 0 && x < PlateChunk.WIDTH && y >= 0 && y < PlateChunk.HEIGHT){
                    PlateSum ps = psc.plateSums[x][y];
                    ps.subtractions += 1*p.magnitude;
                    ps.baseHeight = p.heightOffset;
                }

                int ox = x + p.offsetX;
                int oy = y + p.offsetY;
                if (ox >= 0 && ox < PlateChunk.WIDTH && oy >= 0 && oy < PlateChunk.HEIGHT){
                    PlateSum ps = psc.plateSums[ox][oy];
                    ps.additions += 1.0*p.magnitude;
                    ps.heightVar = (ps.heightVar + (int)(p.heightVariation*2*random.nextGaussian() - p.heightVariation));
                    ps.resistanceToChange = (ps.resistanceToChange + p.resistanceToChange)/2;
                }

            }
        }
        return psc;
    }
}

