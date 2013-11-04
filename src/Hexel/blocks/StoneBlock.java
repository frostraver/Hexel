package Hexel.blocks;
import Hexel.rendering.TEX;

public class StoneBlock extends Block {

  public double getMaxHealth(){ return 2; };
  public StoneBlock(){
    this.health = 2;
  }

    public int getTopTextureIndex(){
        return 2; 
    }
    public int getBottomTextureIndex(){
        return 2; 
    }
    public int getSideTextureIndex(){
        return 2; 
    }

    public double getFracBottom(){ return 0; }
    public double getFracTop(){ return 1; }
    public Block clone(){ return this; }

    public boolean isTransparent(){ return false; }
}


