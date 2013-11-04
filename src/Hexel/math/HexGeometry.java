package Hexel.math;

import Hexel.math.Vector2d;

public class HexGeometry {
    public static void hexToCartesian(double x, double y, Vector2d hp){
        hp.x = x + y*Math.cos(60*Math.PI/180);
        hp.y = y*Math.sin(60*Math.PI/180);
    }
    public static void cartesianToHex(double hx, double hy, Vector2d p){
        p.y = hy/Math.sin(60*Math.PI/180);
        p.x = hx - p.y*Math.cos(60*Math.PI/180);
    }

    public static void cartesianToBlock(double x, double y, double z, Vector2d tmp, Vector3i p){
        HexGeometry.cartesianToHex(x, y, tmp);
        p.x = 2*(int)Math.floor(tmp.x);
        p.y = (int)Math.floor(tmp.y);
        p.z = (int)Math.floor(z*2);

        if (Math.abs((Math.floor(tmp.x) + Math.floor(tmp.y))%2) != Math.abs(Math.floor(tmp.x+tmp.y)%2))
            p.x += 1;
    }

    public static void tringleToHexel(int x, int y, Vector2i h){
        h.y = (int)Math.floor(y/2.0);
        h.x = (int)Math.floor((x-2+y)/3.0);
        if (h.x%2 == 0 && Math.abs(y%2) == 1){
            h.y++;
        }
    }

    public static void hexelToTringle(int hx, int hy, Vector2i t){
        t.y = hy*2;
        t.x = hx*3+2-t.y;
    }

    public static final Vector2i[] evenNeighbors = new Vector2i[]{
        new Vector2i(1, 0),
        new Vector2i(-1, 0),
        new Vector2i(1, -1)
    };

    public static final Vector2i[] oddNeighbors = new Vector2i[]{
        new Vector2i(-1, 1),
        new Vector2i(-1, 0),
        new Vector2i(1, 0)
    };

    public static final Vector3i[] evenAllNeighbors = new Vector3i[]{
        new Vector3i(1, 0, 0),
        new Vector3i(-1, 0, 0),
        new Vector3i(1, -1, 0),
        new Vector3i(0, 0, -1),
        new Vector3i(0, 0, 1),
    };

    public static final Vector3i[] oddAllNeighbors = new Vector3i[]{
        new Vector3i(-1, 1, 0),
        new Vector3i(-1, 0, 0),
        new Vector3i(1, 0, 0),
        new Vector3i(0, 0, -1),
        new Vector3i(0, 0, 1),
    };

    public static final Vector2i[] evenTringlePoints = new Vector2i[]{
        new Vector2i(0, 0),
        new Vector2i(0, 1),
        new Vector2i(1, 0)
    };

    public static final Vector2i[] oddTringlePoints = new Vector2i[]{
        new Vector2i(1, 1),
        new Vector2i(0, 1),
        new Vector2i(1, 0)
    };
}


