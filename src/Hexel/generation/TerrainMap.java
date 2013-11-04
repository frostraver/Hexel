package Hexel.generation;

import Hexel.generation.heightMap.HexBlockHeightMaps;

import Hexel.blocks.Block;
import Hexel.blocks.EmptyBlock;
import Hexel.blocks.GrassBlock;
import Hexel.blocks.StoneBlock;
import Hexel.blocks.WoodBlock;

import Hexel.math.Vector2i;

import java.util.Random;

import Hexel.Cleanup;

public class TerrainMap {

    private HexBlockHeightMaps hbhms;

    private static final int SCALE = 2;

    private static final EmptyBlock emptyBlock = new EmptyBlock();
    private static final GrassBlock grassBlock = new GrassBlock();
    private static final StoneBlock stoneBlock = new StoneBlock();
    private static final WoodBlock woodBlock = new WoodBlock();

    public TerrainMap(Cleanup cleanup){
        hbhms = new HexBlockHeightMaps(cleanup);
    }

    public int getBlockHeight(int x, int y, Vector2i tmp){
        return hbhms.getBlockHeight(x, y, tmp);
    }
}

