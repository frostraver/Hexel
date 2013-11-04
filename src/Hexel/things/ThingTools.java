package Hexel.things;

import Hexel.chunk.Chunks;

import Hexel.things.Cuboid;
import Hexel.math.Vector2d;
import Hexel.math.Vector2i;
import Hexel.math.Vector3d;
import Hexel.math.Vector3i;

import Hexel.math.HexGeometry;

import Hexel.blocks.*;

import java.util.HashSet;
import java.util.Iterator;

public class ThingTools {

    private Chunks chunks;

    public ThingTools(Chunks chunks){
        this.chunks = chunks;
    }

    private interface CheckCollidingBlock {
        public boolean isColliding(Block block);
    }

    private CheckCollidingBlock checkCollidesSolid = new CheckCollidingBlock(){
        public boolean isColliding(Block block){
            return !(block instanceof EmptyBlock || block instanceof WaterBlock);
        }
    };

    private CheckCollidingBlock checkCollidesWater = new CheckCollidingBlock(){
        public boolean isColliding(Block block){
            return block instanceof WaterBlock;
        }
    };

    public boolean collidesSolid(Volumetric v, Vector3d offset, Vector3i tmp){
        if (v instanceof Cuboid){
            return this.fixOffset((Cuboid)v, offset, tmp, false, checkCollidesSolid) != null;
        }
        return false;
    }

    public boolean collidesWater(Volumetric v, Vector3d offset, Vector3i tmp){
        if (v instanceof Cuboid){
            return this.fixOffset((Cuboid)v, offset, tmp, false, checkCollidesWater) != null;
        }
        return false;
    }

    public boolean collidesWater(double x, double y, double z, Vector2d tmp2d, Vector3i tmp3i) {
        Vector3i blockPos = new Vector3i();
        HexGeometry.cartesianToBlock(x, y, z, tmp2d, blockPos);
        Block b = chunks.getBlock(blockPos.x, blockPos.y, blockPos.z, tmp3i);
        return b instanceof WaterBlock;
    }

    public Vector3d fixOffset(Cuboid c, Vector3d offset, Vector3i tmp3i, boolean considerNeighbors){
        return fixOffset(c, offset, tmp3i, considerNeighbors, checkCollidesSolid);
    }

    public Vector3d fixOffset(Cuboid c, Vector3d offset, Vector3i tmp3i, boolean considerNeighbors, CheckCollidingBlock checkCollidingBlock){
        double width = c.getWidth();
        double height = c.getHeight();
        double depth = c.getDepth();

        Vector2d tmp2d = new Vector2d();

        HashSet<Vector3i> toCheck = new HashSet<Vector3i>();

        double dx = -.1; while(true){
            double dy = -.1; while(true){
                double dz = -.1; while(true){
                    double x = c.getX() + dx + offset.x;
                    double y = c.getY() + dy + offset.y;
                    double z = c.getZ() + dz + offset.z;
                    Vector3i blockPos = new Vector3i();
                    HexGeometry.cartesianToBlock(x, y, z, tmp2d, blockPos);
                    toCheck.add(blockPos);

                    if (dz == depth+.1) break;
                    dz = Math.min(depth+.1, dz+.05);
                }
                if (dy == height+.1) break;
                dy = Math.min(height+.1, dy+.05);
            }
            if (dx == width+.1) break;
            dx = Math.min(width+.1, dx+.05);

        }

        HashSet<Vector3d> toFixBlockSet = new HashSet<Vector3d>();
        Iterator<Vector3i> points = toCheck.iterator();
        while (points.hasNext()) {
            Vector3i blockPos = points.next();
            Block b = chunks.getBlock(blockPos.x, blockPos.y, blockPos.z, tmp3i);
            if (checkCollidingBlock.isColliding(b)){
                Vector3d partialFix = new Vector3d();
                collides(c, offset, blockPos.x, blockPos.y, blockPos.z, partialFix, considerNeighbors);
                toFixBlockSet.add(partialFix);
            }
        }

        Vector3d toFix = new Vector3d();;
        Iterator<Vector3d> toFixBlocks = toFixBlockSet.iterator();
        while (toFixBlocks.hasNext()) {
            Vector3d toFixBlock = toFixBlocks.next();
            toFix.x += toFixBlock.x;
            toFix.y += toFixBlock.y;
            toFix.z += toFixBlock.z;
        }

        if (toFix.mag() == 0)
            return null;
        else {
            if (considerNeighbors){
                Vector3d attempt = new Vector3d();
                Vector3i tmp = new Vector3i();
                Vector3d result = new Vector3d();

                attempt.x = offset.x + toFix.x;
                attempt.y = offset.y + toFix.y;
                attempt.z = offset.z + 0;
                result = fixOffset(c, attempt, tmp, false, checkCollidingBlock);
                if (result != null && result.mag() > .00001){

                    attempt.z = offset.z + toFix.z;
                    result = fixOffset(c, attempt, tmp, false, checkCollidingBlock);
                    if (result != null && result.mag() > .00001){
                        toFix.z = -offset.x;
                        toFix.y = -offset.y;
                        toFix.z = -offset.z;
                    }
                    return toFix;
                }
                else {
                    toFix.z = 0;
                    return toFix;
                }
            }
            else {
                return toFix;
            }
        }
    }

    public void collides(Cuboid c, Vector3d offset, int blockx, int blocky, int blockz, Vector3d toFix, boolean considerNeighbors){
        Vector2d p = new Vector2d();

        Vector2i[] points;
        if (blockx%2 == 0){
            points = HexGeometry.evenTringlePoints;
        }
        else {
            points = HexGeometry.oddTringlePoints;
        }

        Vector3d[] axis = new Vector3d[]{
            new Vector3d(1, 0, 0),
            new Vector3d(0, 1, 0),
            new Vector3d(0, 0, 1),
            null, null, null
        };
        Vector3d up = new Vector3d(0, 0, 1);
        for (int i = 0; i < points.length; i++){
            Vector2i v = points[i];
            Vector2i u = points[(i+1)%points.length];
            Vector2d vd = new Vector2d();
            Vector2d ud = new Vector2d();
            HexGeometry.hexToCartesian(v.x, v.y, vd);
            HexGeometry.hexToCartesian(u.x, u.y, ud);

            Vector3d plane = new Vector3d(vd.x - ud.x, vd.y - ud.y, 0);
            plane.cross(up);
            plane.unit();
            axis[3+i] = plane;
            if (plane.y == -1)
                plane.y = 1;
        };
        double[] magnitudes = new double[axis.length];
        for (int i = 0; i < axis.length; i++){
            Double m = calcOverlapMagnitude(c, offset, blockx, blocky, blockz, axis[i], considerNeighbors);

            if (m == null)
                continue;
            if (m == 0)
                return;

            magnitudes[i] = m;

        }
        Vector3d attempt = new Vector3d();
        Vector3i tmp = new Vector3i();
        for (int i = 0; i < axis.length; i++){
            Double overlapMagnitude = magnitudes[i];
            if (overlapMagnitude != null && overlapMagnitude != 0 && (toFix.mag() == 0 || Math.abs(overlapMagnitude) < toFix.mag())){
                    toFix.x = overlapMagnitude*axis[i].x;
                    toFix.y = overlapMagnitude*axis[i].y;
                    toFix.z = overlapMagnitude*axis[i].z;
            }
        }
    }

    public Double calcOverlapMagnitude(Cuboid c, Vector3d offset, int blockx, int blocky, int blockz, Vector3d axis, boolean considerNeighbors){
        double minC = Integer.MAX_VALUE;
        double maxC = Integer.MIN_VALUE;
        double cx = c.getX() + offset.x;
        double cy = c.getY() + offset.y;
        double cz = c.getZ() + offset.z;
        minC = Math.min(minC, axis.dot(cx, cy, cz));
        minC = Math.min(minC, axis.dot(cx+c.getWidth(), cy, cz));
        minC = Math.min(minC, axis.dot(cx, cy+c.getHeight(), cz));
        minC = Math.min(minC, axis.dot(cx+c.getWidth(), cy+c.getHeight(), cz));
        minC = Math.min(minC, axis.dot(cx, cy, cz + c.getDepth()));
        minC = Math.min(minC, axis.dot(cx+c.getWidth(), cy, cz + c.getDepth()));
        minC = Math.min(minC, axis.dot(cx, cy+c.getHeight(), cz + c.getDepth()));
        minC = Math.min(minC, axis.dot(cx+c.getWidth(), cy+c.getHeight(), cz + c.getDepth()));

        maxC = Math.max(maxC, axis.dot(cx, cy, cz));
        maxC = Math.max(maxC, axis.dot(cx+c.getWidth(), cy, cz));
        maxC = Math.max(maxC, axis.dot(cx, cy+c.getHeight(), cz));
        maxC = Math.max(maxC, axis.dot(cx+c.getWidth(), cy+c.getHeight(), cz));
        maxC = Math.max(maxC, axis.dot(cx, cy, cz + c.getDepth()));
        maxC = Math.max(maxC, axis.dot(cx+c.getWidth(), cy, cz + c.getDepth()));
        maxC = Math.max(maxC, axis.dot(cx, cy+c.getHeight(), cz + c.getDepth()));
        maxC = Math.max(maxC, axis.dot(cx+c.getWidth(), cy+c.getHeight(), cz + c.getDepth()));

        double minB = Integer.MAX_VALUE;
        double maxB = Integer.MIN_VALUE;

        Vector2d p = new Vector2d();

        Vector2i[] points;
        if (blockx%2 == 0){
            points = HexGeometry.evenTringlePoints;
        }
        else {
            points = HexGeometry.oddTringlePoints;
        }

        for (int i = 0; i < points.length; i++){
            HexGeometry.hexToCartesian(Math.floor(blockx/2.0) + points[i].x, blocky + points[i].y, p);

            minB = Math.min(minB, axis.dot(p.x, p.y, blockz/2.0));
            maxB = Math.max(maxB, axis.dot(p.x, p.y, blockz/2.0));

            minB = Math.min(minB, axis.dot(p.x, p.y, blockz/2.0 + .5));
            maxB = Math.max(maxB, axis.dot(p.x, p.y, blockz/2.0 + .5));
        }


        if (maxC <= minB || minC >= maxB){
            return 0.0;
        }
        else {
            double a = minB - maxC;
            double b = maxB - minC;

            if (Math.abs(a) > Math.abs(b))
                return b;
            else
                return a;
        }
    }
}

