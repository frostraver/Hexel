package Hexel;

import Hexel.util.FrameRateTracker;
import Hexel.util.TickHandler;
import Hexel.util.StageChangeListener;

import Hexel.things.ThingSimulator;
import Hexel.things.ThingBridge;

import Hexel.chunk.Chunks;
import Hexel.chunk.ChunkVisibilityManager;

import Hexel.things.Thing;

import Hexel.rendering.Renderer;
import Hexel.rendering.Camera;

import Hexel.things.Player;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;


public class Engine{

    private Chunks chunks;
    private Renderer renderer;
    private Camera camera;

    private Stage stage;

    private FrameRateTracker fps;
    private ThingSimulator thingSimulator;
    private WorldSimulator worldSimulator;
    private ChunkVisibilityManager chunkVisibilityManager;

    public JFrame getFrame(){ return renderer.window.getFrame(); }
    public GLCanvas getCanvas(){ return renderer.window.getCanvas(); }
    public void start(TickHandler handler){ renderer.start(handler); }

    public ThingBridge getThingBridge(){ return thingSimulator.getThingBridge(); }

    public void setPlayer(Player player){ renderer.setPlayer(player); }

    private StageChangeListener scl;

    public void setStage(Stage stage){
        this.stage = stage;
        this.renderer.setStage(stage);
        this.scl.stageChanged(stage);
    }

    public void addThing(Thing thing){
        thingSimulator.addThing(thing);
        renderer.addThing(thing);
    }

    public Engine(StageChangeListener scl){
        this.scl = scl;
        Cleanup cleanup = new Cleanup();
        chunks = new Chunks(cleanup, 10*10*10);
        renderer = new Renderer(chunks);
        fps = new FrameRateTracker(5);
        chunkVisibilityManager = new ChunkVisibilityManager(chunks, renderer);
        thingSimulator = new ThingSimulator(chunks);
        worldSimulator = new WorldSimulator(chunks, chunkVisibilityManager);
        this.setStage(Stage.LOADING);

        Runtime.getRuntime().addShutdownHook(cleanup);

    }

    public void setCamera(Camera camera){
        this.camera = camera;
        renderer.setCamera(camera);
    }

    private int i = 0;

    public void step(){
        fps.hit();
        if (stage == Stage.INGAME){
            chunkVisibilityManager.updateVisibility(this.camera);
            if (fps.avg() >= 1){
                thingSimulator.step(fps.avg());

                worldSimulator.step();
            }
        }
        else {
            i++;
            if (i>10)
                this.setStage(Stage.INGAME);
        }
    }

}

