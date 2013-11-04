package Hexel.blocks;

public class EmptyBlock extends Block {
    public double getMaxHealth(){ return 0; };
    public int getTopTextureIndex(){ return 0; }
    public int getBottomTextureIndex(){ return 0; }
    public int getSideTextureIndex(){ return 0; }
    public double getFracBottom(){ return 0; }
    public double getFracTop(){ return 1; }
    public Block clone(){ return this; }
    public boolean isTransparent(){ return true; }
}

