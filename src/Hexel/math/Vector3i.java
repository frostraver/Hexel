package Hexel.math;

public class Vector3i implements java.io.Serializable {
    public int x;
    public int y;
    public int z;

    public Vector3i(){
        this(0, 0, 0);
    }

    public Vector3i(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i(Vector3i that){
        this.x = that.x;
        this.y = that.y;
        this.z = that.z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (o == null)
            return false;
        if (this.getClass() != o.getClass()) 
            return false;

        Vector3i that = (Vector3i)o;

        return this.x == that.x && this.y == that.y && this.z == that.z;
    }

    public double distance(Vector3i that){
        double diffX = this.x - that.x;
        double diffY = this.y - that.y;
        double diffZ = this.z - that.z;
        return Math.pow(diffX*diffX + diffY*diffY + diffZ*diffZ, .5);
    }

    @Override
    public int hashCode() {
        double hash = 23;
        hash = hash * 31 + x;
        hash = hash * 31 + y;
        hash = hash * 31 + z;
        return (int)hash;
    }

    @Override
    public String toString(){
        return this.x + " " + this.y + " " + this.z;
    }
}

