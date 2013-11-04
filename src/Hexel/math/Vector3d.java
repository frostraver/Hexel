package Hexel.math;

import Hexel.math.Vector3i;

public class Vector3d {
    public double x;
    public double y;
    public double z;

    public Vector3d(){
        this(0, 0, 0);
    }

    public Vector3d(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector3d that){
        this.x = that.x;
        this.y = that.y;
        this.z = that.z;
    }

    public Vector3d(Vector3i that){
        this.x = that.x;
        this.y = that.y;
        this.z = that.z;
    }

    public double distance(Vector3d that){
        double diffX = this.x - that.x;
        double diffY = this.y - that.y;
        double diffZ = this.z - that.z;
        return Math.pow(diffX*diffX + diffY*diffY + diffZ*diffZ, .5);
    }
    
    public static Vector3d Sub(Vector3d a, Vector3d b){
        Vector3d other = new Vector3d();
        other.x = a.x - b.x;
        other.y = a.y - b.y;
        other.z = a.z - b.z;
        return other;
    }

    public void unit(){
        double m = mag();
        this.x /= m;
        this.y /= m;
        this.z /= m;
    }

    public void add(Vector3d that){
        this.x += that.x;
        this.y += that.y;
        this.z += that.z;
    }

    public void cross(Vector3d that){
        double x = this.y*that.z - this.z*that.y;
        double y = this.z*that.x - this.x*that.z;
        double z = this.x*that.y - this.y*that.x;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void sub(Vector3d that){
        this.x -= that.x;
        this.y -= that.y;
        this.z -= that.z;
    }

    public double dot(double x, double y, double z){
        return this.x*x + this.y*y + this.z*z;
    }

    public void times(double n){
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }

    public double mag(){
        return Math.pow(x*x + y*y + z*z, .5);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (o == null)
            return false;
        if (this.getClass() != o.getClass()) 
            return false;

        Vector3d that = (Vector3d)o;

       return this.x == that.x && this.y == that.y && this.z == that.z;
     }

    @Override
    public int hashCode() {
        double hash = 23;
        hash = hash * 31 + x*100000;
        hash = hash * 31 + y*100000;
        hash = hash * 31 + z*100000;
        return (int)hash;
    }

    @Override
    public String toString(){
        return this.x + " " + this.y + " " + this.z;
    }
}

