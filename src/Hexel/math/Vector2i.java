package Hexel.math;

public class Vector2i {
    public int x;
    public int y;

    public Vector2i(){
        this(0, 0);
    }

    public Vector2i(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2i(Vector2i that){
        this.x = that.x;
        this.y = that.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (o == null)
            return false;
        if (this.getClass() != o.getClass()) 
            return false;

        Vector2i that = (Vector2i)o;

       return this.x == that.x && this.y == that.y;
    }

    public double distance(Vector2i that){
        double diffX = this.x - that.x;
        double diffY = this.y - that.y;
        return Math.pow(diffX*diffX + diffY*diffY, .5);
    }

    @Override
    public int hashCode() {
        int hash = 23;
        hash = hash * 31 + x;
        hash = hash * 31 + y;
        return (int)hash;
    }

    @Override
    public String toString(){
        return this.x + " " + this.y;
    }
}

