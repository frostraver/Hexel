package Hexel.things;

import Hexel.math.Vector3d;

public class Movement extends Vector3d {
    public Movement(){

    }

    public Movement(double x, double y, double z){
        super(x, y, z);
    }

    public boolean stoppedX = false;
    public boolean stoppedY = false;
    public boolean stoppedZ = false;
}

