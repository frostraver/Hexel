package Hexel.blocks;
import Hexel.rendering.TEX;

public class GrassBlock extends Block {
  public double getMaxHealth(){ return .5; };
  public GrassBlock(){
    this.health = .5;
  }

    public int getTopTextureIndex(){
        return 1;
    }
    public int getBottomTextureIndex(){
        return 2 + TEX.HOR*1; 
    }
    public int getSideTextureIndex(){
        return 1 + TEX.HOR*1; 
    }
    public double getFracBottom(){ return 0; }
    public double getFracTop(){ return 1; }
    public Block clone(){ return this; }

    public boolean isTransparent(){ return false; }

}

