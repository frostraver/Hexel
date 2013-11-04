package Hexel.blocks;

public class BlockDelta {
    public int x;
    public int y;
    public int z;
    public Block block;
    public boolean immediate;
    public boolean awakenNeighbors;

    public BlockDelta(int x, int y, int z, Block block){
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.immediate = false;
        this.awakenNeighbors = false;
    }

    public BlockDelta(int x, int y, int z, Block block, boolean immediate){
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.immediate = immediate;
        this.awakenNeighbors = false;
    }

    public BlockDelta(int x, int y, int z, Block block, boolean immediate, boolean awakenNeighbors){
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.immediate = immediate;
        this.awakenNeighbors = awakenNeighbors;
    }
}

