package Hexel.things;

import Hexel.things.Cuboid;

import Hexel.math.Vector3d;

public interface Movable {
    public void accelerate(double fps, double dx, double dy, double dz);
    public void stopX();
    public void stopY();
    public void stopZ();
    public Vector3d getReqMoveVector(double fps);
    public void applyMoveVector(Vector3d v);
}

