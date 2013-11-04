package Hexel.math;

public class Vector2d {
    public double x;
    public double y;

    public Vector2d(){
        this(0, 0);
    }

    public Vector2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d that){
        this.x = that.x;
        this.y = that.y;
    }

    public Vector2d(Vector2i that){
        this.x = that.x;
        this.y = that.y;
    }

    public double distance(Vector2d that){
        double diffX = this.x - that.x;
        double diffY = this.y - that.y;
        return Math.pow(diffX*diffX + diffY*diffY, .5);
    }
    
    public static Vector2d Sub(Vector2d a, Vector2d b){
        Vector2d other = new Vector2d();
        other.x = a.x - b.x;
        other.y = a.y - b.y;
        return other;
    }

    public void unit(){
        double m = mag();
        this.x /= m;
        this.y /= m;
    }

    public void add(Vector2d that){
        this.x += that.x;
        this.y += that.y;
    }

    public void sub(Vector2d that){
        this.x -= that.x;
        this.y -= that.y;
    }

    public void times(double n){
        this.x *= n;
        this.y *= n;
    }

    public double mag(){
        return Math.pow(x*x + y*y, .5);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (o == null)
            return false;
        if (this.getClass() != o.getClass()) 
            return false;

        Vector2d that = (Vector2d)o;

       return this.x == that.x && this.y == that.y;
     }

    @Override
    public int hashCode() {
        double hash = 23;
        hash = hash * 31 + x*100000;
        hash = hash * 31 + y*100000;
        return (int)hash;
    }

    @Override
    public String toString(){
        return this.x + " " + this.y;
    }
}


