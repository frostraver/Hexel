package Hexel;

import Hexel.things.Player;

import Hexel.Engine;
import Hexel.rendering.Camera;
import Hexel.PCInput;

import Hexel.math.Vector2d;
import Hexel.math.HexGeometry;

import Hexel.util.FrameRateTracker;
import Hexel.util.TickHandler;
import Hexel.util.StageChangeListener;

import Hexel.blocks.*;

public class Hexel implements TickHandler, StageChangeListener  { public static void main(String[] args){ new Hexel(); }

    private PCInput input;

    private Player player;

    private Engine engine;

    private Camera camera;

    public void setCamera(Camera camera){
        engine.setCamera(camera);
        this.camera = camera;
    }

    private FrameRateTracker fps;

    private Config config;

    public Hexel(){

        config = new Config();

        engine = new Engine((StageChangeListener)this);
        int cx = 0;//61;
        int cy = 0;//73;
        int cz = 3;

        Vector2d pos = new Vector2d();
        HexGeometry.hexToCartesian(cx*16, cy*16, pos);

        double x = pos.x + 0;
        double y = pos.y + 0;
        double z = cz*16 + 0;

        //START 1.9087976974108996 1.2740837857172846 0.0 | 3 1 0

        player = new Player(x, y, z, -45, 0, engine.getThingBridge());
        engine.setPlayer(player);
        player.blockToPlace = DebugBlock.class;
        input = new PCInput(engine.getFrame(), engine.getCanvas());
        this.setCamera(player);
        engine.start((TickHandler)this);
        fps = new FrameRateTracker(5);
    }

    @Override
    public void onTick(){
        fps.hit();
        this.updateControls();
        engine.step();
    }

    @Override
    public void stageChanged(Stage s){
        if (s == Stage.INGAME){
            engine.addThing(player);
        }
    }


    public void updateControls(){

        while (!input.keysTapped.isEmpty()){
            int keyTapped = input.keysTapped.poll();
            if (keyTapped == config.controls.get("toggleFocus")){
                this.input.toggleCursorObject();
            }
            else if (keyTapped == config.controls.get("gotoDebugBlock")){
                player.blockToPlace = DebugBlock.class;
            }
            else if (keyTapped == config.controls.get("gotoGrassBlock")){
                player.blockToPlace = GrassBlock.class;
            }
            else if (keyTapped == config.controls.get("gotoStoneBlock")){
                player.blockToPlace = StoneBlock.class;
            }
            else if (keyTapped == config.controls.get("gotoWaterBlock")){
                player.blockToPlace = WaterBlock.class;//WaterBlock.blocks[0][8];
            }
            else if (keyTapped == config.controls.get("gotoWoodBlock")){
                player.blockToPlace = WoodBlock.class;//WaterBlock.blocks[0][8];
            }
            else if (keyTapped == config.controls.get("gotoLeafBlock")){
                player.blockToPlace = LeafBlock.class;//WaterBlock.blocks[0][8];
            }
            else if (keyTapped == config.controls.get("createBlock")){
              player.createBlock = true;
            }
        }

        double speed = 4;
        if (input.keysPressed.contains(config.controls.get("sprint"))){
            speed *= 4.5;
        }

        if (input.keysPressed.contains(config.controls.get("forward"))){
            player.setForwardSpeed(1*speed);
        }
        else if (input.keysPressed.contains(config.controls.get("backward"))){
            player.setForwardSpeed(-1*speed);
        }
        else {
            player.setForwardSpeed(0);
        }

        if (input.keysPressed.contains(config.controls.get("left"))){
            player.setSidewaysSpeed(-1*speed);
        }
        else if (input.keysPressed.contains(config.controls.get("right"))){
            player.setSidewaysSpeed(1*speed);
        }
        else {
            player.setSidewaysSpeed(0);
        }

        player.deleteBlock = input.keysPressed.contains(config.controls.get("removeBlock"));

        if (input.keysPressed.contains(config.controls.get("jump"))){
            player.jump(4);
        }

        double dx = input.getDX()/5.0;
        double dy = input.getDY()/5.0;

        player.lookVertical(dy);
        player.lookHorizontal(dx);
    }

}

