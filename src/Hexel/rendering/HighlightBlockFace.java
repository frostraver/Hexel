package Hexel.rendering;

import javax.media.opengl.GL2;
import javax.media.opengl.GL;

import Hexel.math.Vector3d;
import Hexel.math.HexGeometry;
import Hexel.math.Vector2d;
import Hexel.math.Vector3i;

public class HighlightBlockFace {

    public static Face getBetweenFace(Vector3i a, Vector3i b){
        if (a.z < b.z){
            return Face.TOP;
        }
        else if (a.z > b.z){
            return Face.BOTTOM;
        }
        else if (
            (a.x%2 == 1 && a.y < b.y) ||
            (a.x%2 == 0 && a.x > b.x)){
            return Face.SIDEA;
        }
        else if (
            (a.x%2 == 1 && a.x < b.x) ||
            (a.x%2 == 0 && a.y > b.y)){
            return Face.SIDEB;
        }
        else if (
            (a.x%2 == 1 && a.x > b.x) ||
            (a.x%2 == 0 && a.x < b.x)){
            return Face.SIDEC;
        }
        return null;

    }

    public static void highlight(GL2 gl, Vector3i block, Face face){
        Vector2d a = new Vector2d();
        Vector2d b = new Vector2d();
        Vector2d c = new Vector2d();

        int x = block.x;
        int y = block.y;
        int z = block.z;
        double px = Math.floor(x/2.0);
        double offset = .02;
        if (x % 2 == 0){
            HexGeometry.hexToCartesian(px + 0 - offset/2, y + 0 - offset/2, a);
            HexGeometry.hexToCartesian(px + 0 - offset/2, y + 1 + offset, b);
            HexGeometry.hexToCartesian(px + 1 + offset, y + 0 - offset/2, c);
        }
        else {
            HexGeometry.hexToCartesian(px + 1 + offset/2, y + 1 + offset/2, a);
            HexGeometry.hexToCartesian(px + 0 - offset, y + 1 + offset/2, b);
            HexGeometry.hexToCartesian(px + 1 + offset/2, y + 0 - offset, c);
        }

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(0.5f,0.5f,0.5f,0.5f);

        gl.glBegin(GL.GL_TRIANGLES);
        if (face == Face.TOP){
            gl.glVertex3d(a.x, a.y, (z+1)/2.0 + offset);
            gl.glVertex3d(b.x, b.y, (z+1)/2.0 + offset);
            gl.glVertex3d(c.x, c.y, (z+1)/2.0 + offset);
        }
        else if (face == Face.BOTTOM){
            gl.glVertex3d(a.x, a.y, z/2.0 - offset);
            gl.glVertex3d(b.x, b.y, z/2.0 - offset);
            gl.glVertex3d(c.x, c.y, z/2.0 - offset);
        }
        else if (face == Face.SIDEA){
            gl.glVertex3d(a.x, a.y, (z + 0)/2.0);
            gl.glVertex3d(b.x, b.y, (z + 0)/2.0);
            gl.glVertex3d(a.x, a.y, (z + 1)/2.0);

            gl.glVertex3d(b.x, b.y, (z + 0)/2.0);
            gl.glVertex3d(a.x, a.y, (z + 1)/2.0);
            gl.glVertex3d(b.x, b.y, (z + 1)/2.0);
        }
        else if (face == Face.SIDEB){
            gl.glVertex3d(a.x, a.y, (z + 0)/2.0);
            gl.glVertex3d(c.x, c.y, (z + 0)/2.0);
            gl.glVertex3d(a.x, a.y, (z + 1)/2.0);

            gl.glVertex3d(c.x, c.y, (z + 0)/2.0);
            gl.glVertex3d(a.x, a.y, (z + 1)/2.0);
            gl.glVertex3d(c.x, c.y, (z + 1)/2.0);
        }
        else if (face == Face.SIDEC){
            gl.glVertex3d(b.x, b.y, (z + 0)/2.0);
            gl.glVertex3d(c.x, c.y, (z + 0)/2.0);
            gl.glVertex3d(b.x, b.y, (z + 1)/2.0);

            gl.glVertex3d(c.x, c.y, (z + 0)/2.0);
            gl.glVertex3d(b.x, b.y, (z + 1)/2.0);
            gl.glVertex3d(c.x, c.y, (z + 1)/2.0);
        }
        gl.glEnd();

        gl.glColor4f(1, 1, 1, 1);
    }
}

