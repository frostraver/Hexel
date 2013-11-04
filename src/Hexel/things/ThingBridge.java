package Hexel.things;

import Hexel.chunk.Chunks;
import Hexel.math.Vector3d;
import Hexel.math.Vector3i;
import Hexel.math.Vector2d;
import Hexel.math.HexGeometry;

import Hexel.blocks.*;

public class ThingBridge {
    private Chunks chunks;

    private ThingTools thingTools;

    public ThingBridge(Chunks chunks){
        this.chunks = chunks;
        this.thingTools = new ThingTools(chunks);
    }

    private static final Vector3d onGroundOffset = new Vector3d(0, 0, -.1);
    private static final Vector3d zeroOffset = new Vector3d(0, 0, 0);

    public boolean onGround(Volumetric v){
        Vector3i tmp = new Vector3i();
        return thingTools.collidesSolid(v, onGroundOffset, tmp);
    }

    public boolean inWater(Volumetric v){
        Vector3i tmp = new Vector3i();
        return thingTools.collidesWater(v, zeroOffset, tmp);
    }

    public boolean isUnderwater(double x, double y, double z){
        Vector3i tmp3i = new Vector3i();
        Vector2d tmp2d = new Vector2d();
        return thingTools.collidesWater(x, y, z, tmp2d, tmp3i);
    }

    public Vector3i getClosestNonEmptyBlockOnVector(Vector3d offset, Vector3d t){
        double STEP_SIZE = .01;

        Vector3d v = new Vector3d();
        Vector3d step = Vector3d.Sub(t, v);
        step.unit();
        step.times(STEP_SIZE);

        Vector2d tmp2d = new Vector2d();
        Vector3i tmp3i = new Vector3i();

        Vector3i blockPos = new Vector3i();

        while(v.mag() < t.mag()){
            v.add(step);

            HexGeometry.cartesianToBlock(v.x + offset.x, v.y + offset.y, v.z + offset.z, tmp2d, blockPos);

            Block b = chunks.getBlock(blockPos.x, blockPos.y, blockPos.z, tmp3i);
            if (!(b instanceof EmptyBlock || b instanceof WaterBlock)){
                return blockPos;
            }
        }
        return null;
    }

    public Vector3i getClosestEmptyBlockOnVector(Vector3d offset, Vector3d t){
        double STEP_SIZE = .01;

        Vector3d v = new Vector3d();
        Vector3d step = Vector3d.Sub(t, v);
        step.unit();
        step.times(STEP_SIZE);

        Vector3i farthestEmpty = new Vector3i();
        Vector3i blockPos = new Vector3i();

        Vector2d tmp2d = new Vector2d();
        Vector3i tmp3i = new Vector3i();

        while(v.mag() < t.mag()){
            v.add(step);
            HexGeometry.cartesianToBlock(v.x + offset.x, v.y + offset.y, v.z + offset.z, tmp2d, blockPos);

            Block b = chunks.getBlock(blockPos.x, blockPos.y, blockPos.z, tmp3i);
            if (!(b instanceof EmptyBlock || b instanceof WaterBlock)){
                return farthestEmpty;
            }
            else {
                farthestEmpty.x = blockPos.x;
                farthestEmpty.y = blockPos.y;
                farthestEmpty.z = blockPos.z;
            }
        }
        return null;
    }
}

