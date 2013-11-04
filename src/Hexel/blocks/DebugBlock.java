package Hexel.blocks;
import Hexel.rendering.TEX;

public class DebugBlock extends Block {

  public double getMaxHealth(){ return 0; };

  public DebugBlock(){
    this.health = 0;
  }


    public int getTopTextureIndex(){
        return TEX.HOR*1; 
    }
    public int getBottomTextureIndex(){
        return 0; 
    }
    public int getSideTextureIndex(){
        return 0; 
    }

    public double getFracBottom(){ return 0; }
    public double getFracTop(){ return 1; }


    public Block clone(){ return this; }

    public boolean isTransparent(){ return false; }

}

