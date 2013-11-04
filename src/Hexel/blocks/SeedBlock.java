package Hexel.blocks;
import Hexel.rendering.TEX;

public class SeedBlock extends Block {

  public double getMaxHealth(){ return .1; };
  public SeedBlock(){
    this.health = .1;
  }

    public int getTopTextureIndex(){
        return TEX.HOR*2; 
    }
    public int getBottomTextureIndex(){
        return TEX.HOR*2; 
    }
    public int getSideTextureIndex(){
        return TEX.HOR*2; 
    }

    public double getFracBottom(){ return 0; }
    public double getFracTop(){ return 1; }
    public Block clone(){ return this; }

    public boolean isTransparent(){ return false; }
}




