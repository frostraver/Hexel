package Hexel.rendering;

import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.awt.GLCanvas;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import java.util.LinkedList;
import java.util.Queue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jogamp.common.nio.Buffers;

import java.util.ArrayList;

import Hexel.Stage;

import Hexel.chunk.Chunks;
import Hexel.chunk.Chunk;

import Hexel.math.Vector3i;
import Hexel.math.Vector2d;

import Hexel.things.Thing;

import Hexel.rendering.Camera;
import Hexel.rendering.Renderer;
import Hexel.rendering.GLBuffer;

import Hexel.math.Vector3d;
import Hexel.math.Vector3i;

import Hexel.util.TickHandler;


import Hexel.things.Player;


public class Renderer implements GLEventListener{

    private Camera camera; public void setCamera(Camera camera){ this.camera = camera; }

    public Window window;

	private GLU glu;

    private TickHandler tickHandler;

    private FPSAnimator animator;

    private ChunkRenderer chunkRenderer;
    private ThingRenderer thingRenderer;

    public void loadChunks(ArrayList<Vector3i> points){ chunkRenderer.loadChunks(points); }
    public void addThing(Thing thing){ thingRenderer.addThing(thing); }
    public boolean hasChunk(Vector3i point){ return chunkRenderer.hasChunk(point); }
    public void unloadGLChunk(Vector3i points){ chunkRenderer.unloadGLChunk(points); }
    public HashMap<Vector3i, GLChunk> getGLChunkTable(){ return chunkRenderer.getGLChunkTable(); }

    private HUD hud;
    public void setPlayer(Player player){ hud.setPlayer(player); }
    public void setStage(Stage stage){ hud.setStage(stage); }

    public Renderer(Chunks chunks) {

        this.window = new Window();
        this.chunkRenderer = new ChunkRenderer(chunks);
        this.thingRenderer = new ThingRenderer();
        this.hud = new HUD();

        this.window.getCanvas().addGLEventListener(this);

    }

    public void start(TickHandler tickHandler){
        this.tickHandler = tickHandler;
        animator = new FPSAnimator(this.window.getCanvas(), 60);
        animator.add(this.window.getCanvas());
        animator.start();
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        if (tickHandler != null){
            tickHandler.onTick();
            update(gl);
            render(gl);
            this.hud.display();
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		glu = GLU.createGLU();
		gl.glClearColor(0x00/255f, 0x7d/255f, 0xdf/255f, 0f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        this.chunkRenderer.init(gl);
        this.hud.init(drawable);

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
        this.hud.updateSize(w, h);
		glu.gluPerspective(60.0f, (w*1.0f / h), .01f, 1000.0f); // FOV, AspectRatio, NearClip, FarClip

    }
    
    private void update(GL2 gl) {
        this.chunkRenderer.update(gl);
    }

    private void render(GL2 gl) {


		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glLoadIdentity();
		gl.glRotatef((float)-camera.getCameraXRot(), 1, 0, 0);
		gl.glRotatef((float)-camera.getCameraZRot(), 0, 0, 1);
		gl.glTranslatef((float)-camera.getCameraX(), (float)-camera.getCameraY(), (float)-camera.getCameraZ());

        this.chunkRenderer.render(gl);
        this.thingRenderer.render(gl);

        gl.glEnd();
    }
}

