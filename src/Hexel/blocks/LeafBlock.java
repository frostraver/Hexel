package Hexel.blocks;
import Hexel.rendering.TEX;

public class LeafBlock extends Block {
  public double getMaxHealth(){ return .1; };
  public LeafBlock(){
    this.health = .1;
  }

    public int getTopTextureIndex(){
        return 1+TEX.HOR*2; 
    }
    public int getBottomTextureIndex(){
        return 1+TEX.HOR*2; 
    }
    public int getSideTextureIndex(){
        return 1+TEX.HOR*2; 
    }
    public double getFracBottom(){ return 0; }
    public double getFracTop(){ return 1; }
    public Block clone(){ return this; }

    public boolean isTransparent(){ return true; }
}




