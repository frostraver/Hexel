package Hexel.rendering;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import com.jogamp.common.nio.Buffers;

import java.util.Hashtable;

import java.util.ArrayList;

import Hexel.math.HexGeometry;

import Hexel.blocks.Block;
import Hexel.blocks.*;

import Hexel.math.Vector2d;
import Hexel.math.Vector3i;

import Hexel.chunk.Chunks;
import Hexel.chunk.Chunk;

import Hexel.rendering.GLBuffer;

public class GLChunk {
    public ArrayList<Float> vertexData;
    public Vector3i position;
    public GLBuffer buffer;
    public Chunk chunk;
    public Chunks chunks;


    public GLChunk(Chunk chunk, Chunks chunks){
        this.chunk = chunk;
        this.chunks = chunks;
    }

    public void initBuffer(GL2 gl){
        if (vertexData.size() == 0){
            this.buffer = null;
            return;
        }

        int[] bufferId = new int[]{-1};

        gl.glGenBuffers( 1, bufferId, 0 );
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferId[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER,
                        vertexData.size() * Buffers.SIZEOF_FLOAT,
                        null,
                        GL2.GL_DYNAMIC_DRAW);

        ByteBuffer byteBuffer = gl.glMapBuffer(GL2.GL_ARRAY_BUFFER, GL2.GL_WRITE_ONLY);
        FloatBuffer buffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (int i = 0; i < vertexData.size(); i++){
            buffer.put(vertexData.get(i));
        }
        gl.glUnmapBuffer(GL2.GL_ARRAY_BUFFER);

        this.buffer = new GLBuffer(bufferId, vertexData.size() * Buffers.SIZEOF_FLOAT);

        vertexData = null;
   }

   private boolean display(Block b, Block o, Face face){
        double bfb = b.getFracBottom();
        double bft = b.getFracTop();
        double ofb = o.getFracBottom();
        double oft = o.getFracTop();
        return (o.isTransparent() && o.getClass() != b.getClass()) ||
                (face == Face.TOP && (ofb != 0 || bft != 1)) ||
                (face == Face.BOTTOM && (oft != 1 || bfb != 0)) ||
                (ofb < ofb || bft > oft);
   }

    public void genArrayList(){
        vertexData = new ArrayList<Float>();

        Vector2d a = new Vector2d();
        Vector2d b = new Vector2d();
        Vector2d c = new Vector2d();
        Vector2d d = new Vector2d();

        Vector3i tmp = new Vector3i();

        Rect r = new Rect();

        Color color = new Color();

        for (int x = 0; x < 32; x++){
            for (int y = 0; y < 16; y++){
                for (int z = 0; z < 32; z++){
                    Block block = chunk.get(x, y, z);
                    if (block instanceof EmptyBlock)
                        continue;
                    int px = x/2;
                    if (x % 2 == 0){
                        HexGeometry.hexToCartesian(px + 0, y + 0, a);
                        HexGeometry.hexToCartesian(px + 0, y + 1, b);
                        HexGeometry.hexToCartesian(px + 1, y + 0, c);
                        HexGeometry.hexToCartesian(px + 0.5, y + 0.5, d);
                    }
                    else {
                        HexGeometry.hexToCartesian(px + 1, y + 1, a);
                        HexGeometry.hexToCartesian(px + 0, y + 1, b);
                        HexGeometry.hexToCartesian(px + 1, y + 0, c);
                        HexGeometry.hexToCartesian(px + 0.5, y + 0.5, d);
                    }

                    Block blockZP = chunks.getBlock(
                                                chunk.cx, 
                                                chunk.cy, 
                                                chunk.cz, x, y, z+1, tmp);
                    boolean displayZP = display(block, blockZP, Face.TOP);

                    Block blockZM = chunks.getBlock(
                                                chunk.cx, 
                                                chunk.cy, 
                                                chunk.cz, x, y, z-1, tmp);
                    boolean displayZM = display(block, blockZM, Face.BOTTOM);


                    // NOTE faces wrong but it doesn't matter here, see below for correct
                    Block blockXP = chunks.getBlock(
                                                chunk.cx, 
                                                chunk.cy, 
                                                chunk.cz, x+1, y, z, tmp);
                    boolean displayXP = display(block, blockXP, Face.SIDEA);

                    Block blockXM = chunks.getBlock(
                                                chunk.cx, 
                                                chunk.cy, 
                                                chunk.cz, x-1, y, z, tmp);
                    boolean displayXM = display(block, blockXM, Face.SIDEB);

                    Block blockYP = chunks.getBlock(
                                                chunk.cx, 
                                                chunk.cy, 
                                                chunk.cz, x-1, y+1, z, tmp);
                    boolean displayYP = display(block, blockYP, Face.SIDEC);

                    Block blockYM = chunks.getBlock(
                                                chunk.cx, 
                                                chunk.cy, 
                                                chunk.cz, x+1, y-1, z, tmp);
                    boolean displayYM = display(block, blockYM, Face.SIDEB);

                    double fracBottom = block.getFracBottom();
                    double fracTop = block.getFracTop();

                    double topz = (z + 0)/2.0 + .5*fracTop;
                    double bottomz = (z + 0)/2.0 + .5*fracBottom;

                    int damageOffset = 4*(int)(5*(1-block.health/block.getMaxHealth()));
                    if (damageOffset < 0)
                      damageOffset = 0;
                    else if (damageOffset > 4*4)
                      damageOffset = 4*4;

                    if (displayZM){

                        getColor(block, blockZM, color, Face.BOTTOM, x);

                        getTexRect(damageOffset + block.getBottomTextureIndex(), r);

                        addVertexData(vertexData, a.x, a.y, bottomz,    r.l, r.t, color);
                        addVertexData(vertexData, b.x, b.y, bottomz,    r.l, r.b, color);
                        addVertexData(vertexData, d.x, d.y, bottomz,    r.r, r.b, color);

                        addVertexData(vertexData, a.x, a.y, bottomz,    r.l, r.t, color);
                        addVertexData(vertexData, c.x, c.y, bottomz,    r.r, r.t, color);
                        addVertexData(vertexData, d.x, d.y, bottomz,    r.r, r.b, color);
                    }

                    {
                        getTexRect(damageOffset + block.getSideTextureIndex(), r);

                        if ((x%2 == 1 && displayYP) ||
                            (x%2 == 0 && displayXM)){

                            if (x%2 == 1)
                                getColor(block, blockYP, color, Face.SIDEA, x);
                            else
                                getColor(block, blockXM, color, Face.SIDEA, x);

                            addVertexData(vertexData, a.x, a.y, bottomz,    r.l, r.t, color);
                            addVertexData(vertexData, b.x, b.y, bottomz,    r.r, r.t, color);
                            addVertexData(vertexData, a.x, a.y, topz   ,    r.l, r.b, color);
                            
                            addVertexData(vertexData, b.x, b.y, bottomz,    r.r, r.t, color);
                            addVertexData(vertexData, a.x, a.y, topz   ,    r.l, r.b, color);
                            addVertexData(vertexData, b.x, b.y, topz   ,    r.r, r.b, color);
                        }


                        if ((x%2 == 1 && displayXP) ||
                            (x%2 == 0 && displayYM)){

                            if (x%2 == 1)
                                getColor(block, blockXP, color, Face.SIDEB, x);
                            else
                                getColor(block, blockYM, color, Face.SIDEB, x);

                            addVertexData(vertexData, a.x, a.y, bottomz,    r.l, r.t, color);
                            addVertexData(vertexData, c.x, c.y, bottomz,    r.r, r.t, color);
                            addVertexData(vertexData, a.x, a.y, topz   ,    r.l, r.b, color);
                                                                                   
                            addVertexData(vertexData, c.x, c.y, bottomz,    r.r, r.t, color);
                            addVertexData(vertexData, a.x, a.y, topz   ,    r.l, r.b, color);
                            addVertexData(vertexData, c.x, c.y, topz   ,    r.r, r.b, color);
                        }

                        if ((x%2 == 1 && displayXM) ||
                            (x%2 == 0 && displayXP)){

                            if (x%2 == 1)
                                getColor(block, blockXM, color, Face.SIDEC, x);
                            else
                                getColor(block, blockXP, color, Face.SIDEC, x);

                            addVertexData(vertexData, b.x, b.y, bottomz,    r.l, r.t, color);
                            addVertexData(vertexData, c.x, c.y, bottomz,    r.r, r.t, color);
                            addVertexData(vertexData, b.x, b.y, topz   ,    r.l, r.b, color);
                                                                                   
                            addVertexData(vertexData, c.x, c.y, bottomz,    r.r, r.t, color);
                            addVertexData(vertexData, b.x, b.y, topz   ,    r.l, r.b, color);
                            addVertexData(vertexData, c.x, c.y, topz   ,    r.r, r.b, color);
                        }
                    }


                    if (displayZP){

                        getColor(block, blockZP, color, Face.TOP, x);

                        getTexRect(damageOffset + block.getTopTextureIndex(), r);

                        addVertexData(vertexData, a.x, a.y, topz,    r.l, r.t, color);
                        addVertexData(vertexData, b.x, b.y, topz,    r.l, r.b, color);
                        addVertexData(vertexData, d.x, d.y, topz,    r.r, r.b, color);

                        addVertexData(vertexData, a.x, a.y, topz,    r.l, r.t, color);
                        addVertexData(vertexData, c.x, c.y, topz,    r.r, r.t, color);
                        addVertexData(vertexData, d.x, d.y, topz,    r.r, r.b, color);

                    }
                }
            }
        }
    }

    private class Rect {
        public double l;
        public double r;
        public double t;
        public double b;
    }

    public void getTexRect(int i, Rect r){
        double texW = 1.0/TEX.HOR;
        double texH = 1.0/TEX.VER;
        double texX = (i % TEX.HOR)*texW;
        double texY = 1 - (i / TEX.HOR+1)*texH;

        r.l = texX;
        r.r = texX + texW;
        r.t = texY;
        r.b = texY + texH;
    }

    public void getColor(Block block, Block neighbor, Color color, Face face, int x){
        if ((!(neighbor instanceof EmptyBlock) && neighbor.isTransparent())){
            color.r = 0;
            color.g = 0;
            color.b = 0;
            color.alpha = 0;
        }
        else {
            color.r = 1;
            color.g = 1;
            color.b = 1;

            if (face == Face.TOP)
                color.alpha = .2;
            else if (face == Face.BOTTOM)
                color.alpha = .0;
            else {
                if (face == Face.SIDEA && x%2 == 0)
                    color.alpha = .15;
                else if (face == Face.SIDEA && x%2 == 1)
                    color.alpha = .12;
                else if (face == Face.SIDEC && x%2 == 0){
                    color.alpha = .07;
                }
                else if (face == Face.SIDEB && x%2 == 1){
                    color.alpha = .03;
                }
                else if (face == Face.SIDEB && x%2 == 0){
                    color.alpha = .05;
                }
                else if (face == Face.SIDEC && x%2 == 1)
                    color.alpha = .1;
            }
            //if (block.getMaxHealth() != 0){
            //  double frac = block.health/block.getMaxHealth();
            //  color.r = frac;
            //  color.g = frac;
            //  color.b = frac;
            //  color.alpha += (1 - frac)/2;
            //}
        }
    }

    public void addVertexData(ArrayList<Float> list, double x, double y, double z, double tx, double ty, Color c){
        list.add((float)x);
        list.add((float)y);
        list.add((float)z);
        list.add((float)tx);
        list.add((float)ty);
        list.add((float)c.r);
        list.add((float)c.g);
        list.add((float)c.b);
        list.add((float)c.alpha);
    }
    
}

