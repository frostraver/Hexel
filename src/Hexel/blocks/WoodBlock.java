package Hexel.blocks;

import Hexel.rendering.TEX;

public class WoodBlock extends Block {

  public double getMaxHealth(){ return 1; };

  public WoodBlock(){
    this.health = 1;
  }

    public int maxHeight;
    public int height;

    public int getTopTextureIndex(){
        return 3+TEX.HOR*1; 
    }
    public int getBottomTextureIndex(){
        return 3+TEX.HOR*1; 
    }
    public int getSideTextureIndex(){
        return 3; 
    }

    public double getFracBottom(){ return 0; }
    public double getFracTop(){ return 1; }
    public Block clone(){ return this; }

    public boolean isTransparent(){ return false; }
}



