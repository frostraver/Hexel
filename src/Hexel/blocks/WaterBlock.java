package Hexel.blocks;

import Hexel.math.Vector3i;
import Hexel.rendering.TEX;

public class WaterBlock extends Block {


    public double getMaxHealth(){ return 0; };

    private int top = 0; public int getTop(){ return this.top; }
    private int bottom = 0; public int getBottom(){ return this.bottom; }
    public static int MAX_VOLUME = 8;

    public Vector3i target;

    //public static WaterBlock[][] blocks = new WaterBlock[][]{
    //    { new WaterBlock(0,0),new WaterBlock(0,1),new WaterBlock(0,2),new WaterBlock(0,3),new WaterBlock(0,4),new WaterBlock(0,5),new WaterBlock(0,6),new WaterBlock(0,7),new WaterBlock(0,8) },{ new WaterBlock(1,0),new WaterBlock(1,1),new WaterBlock(1,2),new WaterBlock(1,3),new WaterBlock(1,4),new WaterBlock(1,5),new WaterBlock(1,6),new WaterBlock(1,7),new WaterBlock(1,8) },{ new WaterBlock(2,0),new WaterBlock(2,1),new WaterBlock(2,2),new WaterBlock(2,3),new WaterBlock(2,4),new WaterBlock(2,5),new WaterBlock(2,6),new WaterBlock(2,7),new WaterBlock(2,8) },{ new WaterBlock(3,0),new WaterBlock(3,1),new WaterBlock(3,2),new WaterBlock(3,3),new WaterBlock(3,4),new WaterBlock(3,5),new WaterBlock(3,6),new WaterBlock(3,7),new WaterBlock(3,8) },{ new WaterBlock(4,0),new WaterBlock(4,1),new WaterBlock(4,2),new WaterBlock(4,3),new WaterBlock(4,4),new WaterBlock(4,5),new WaterBlock(4,6),new WaterBlock(4,7),new WaterBlock(4,8) },{ new WaterBlock(5,0),new WaterBlock(5,1),new WaterBlock(5,2),new WaterBlock(5,3),new WaterBlock(5,4),new WaterBlock(5,5),new WaterBlock(5,6),new WaterBlock(5,7),new WaterBlock(5,8) },{ new WaterBlock(6,0),new WaterBlock(6,1),new WaterBlock(6,2),new WaterBlock(6,3),new WaterBlock(6,4),new WaterBlock(6,5),new WaterBlock(6,6),new WaterBlock(6,7),new WaterBlock(6,8) },{ new WaterBlock(7,0),new WaterBlock(7,1),new WaterBlock(7,2),new WaterBlock(7,3),new WaterBlock(7,4),new WaterBlock(7,5),new WaterBlock(7,6),new WaterBlock(7,7),new WaterBlock(7,8) },{ new WaterBlock(8,0),new WaterBlock(8,1),new WaterBlock(8,2),new WaterBlock(8,3),new WaterBlock(8,4),new WaterBlock(8,5),new WaterBlock(8,6),new WaterBlock(8,7),new WaterBlock(8,8) }
    //};

    public WaterBlock(int bottom, int top){
        this.top = top;
        this.bottom = bottom;
    }

    public int getTopTextureIndex(){
        return 2+TEX.HOR*2; 
    }
    public int getBottomTextureIndex(){
        return 2+TEX.HOR*2; 
    }
    public int getSideTextureIndex(){
        return 2+TEX.HOR*2; 
    }

    public double getFracBottom(){ return bottom*1.0/MAX_VOLUME; }
    public double getFracTop(){ return top*1.0/MAX_VOLUME; }
    public Block clone(){
        WaterBlock that = new WaterBlock(this.bottom, this.top);//WaterBlock.blocks[this.bottom][this.top];
        return that;
    }

    public static WaterBlock make(int bottom, int top){
      return new WaterBlock(bottom, top);
    }

    public boolean isTransparent(){ return true; }
}


