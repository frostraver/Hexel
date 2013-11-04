package Hexel.blocks;

public abstract class Block implements java.io.Serializable {
    public abstract int getTopTextureIndex();
    public abstract int getBottomTextureIndex();
    public abstract int getSideTextureIndex();

    public abstract double getFracBottom();
    public abstract double getFracTop();

    public abstract Block clone();

    public abstract boolean isTransparent();

    public abstract double getMaxHealth();
    public double health;

}

