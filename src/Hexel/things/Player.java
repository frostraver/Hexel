package Hexel.things;

import javax.media.opengl.GL2;
import javax.media.opengl.GL;

import java.lang.InstantiationException;

import Hexel.math.Vector3d;
import Hexel.rendering.Camera;

import Hexel.things.Movable;
import Hexel.things.blockManipulation.BlockManipulator;
import Hexel.things.blockManipulation.BlockAction;
import Hexel.things.blockManipulation.AddBlock;
import Hexel.things.blockManipulation.RemoveBlock;

import Hexel.blocks.Block;
import Hexel.blocks.*;

import Hexel.rendering.Renderable;
import Hexel.rendering.HighlightBlockFace;
import Hexel.rendering.Face;

import Hexel.math.HexGeometry;
import Hexel.math.Vector2d;
import Hexel.math.Vector3i;

public class Player implements Movable, Cuboid, Volumetric, Camera, Thing, BlockManipulator, Renderable, InventoryOwner {

    public double x;
    public double y;
    public double z;

    public double xlen = .3;
    public double ylen = .3;
    public double zlen = 1.4;

    private double side_dx = 0;
    private double side_dy = 0;

    private double forward_dx = 0;
    private double forward_dy = 0;

    public double dz = 0;

    private double headFractionXlen = .5;
    private double headFractionYlen = .5;
    private double headFractionZlen = .75;

    public double headXRot = 0; 
    public double headZRot = 0;

    public double getCameraX(){ return x + xlen*headFractionXlen; }
    public double getCameraY(){ return y + ylen*headFractionYlen; }
    public double getCameraZ(){ return z + zlen*headFractionZlen; }

    public double getCameraXRot(){ return headXRot+90; }
    public double getCameraYRot(){ return 0; }
    public double getCameraZRot(){ return headZRot; }


    public double getX(){ return x; };
    public double getY(){ return y; };
    public double getZ(){ return z; };

    public double getWidth(){ return xlen; };
    public double getHeight(){ return ylen; };
    public double getDepth(){ return zlen; };

    private ThingBridge thingBridge;

    private Inventory inventory = new Inventory();

    public Player(
        double x, double y, double z,
        double xRot, double zRot,
        ThingBridge thingBridge
    ){
        this.x = x;
        this.y = y;
        this.z = z;
        this.headXRot = xRot;
        this.headZRot = zRot;
        this.thingBridge = thingBridge;
    }

    public Vector3d getReqMoveVector(double fps){
        Vector3d v = new Vector3d();
        double dragZ = 1;
        double dragXY = 1;
        if (thingBridge.inWater(this)){
            dragZ = .5;
            dragXY = .75;
        }
        v.x = dragXY*(this.side_dx + this.forward_dx)/fps;
        v.y = dragXY*(this.side_dy + this.forward_dy)/fps; 
        v.z = dragZ*this.dz/fps;
        return v;
    }

    public void applyMoveVector(Vector3d v){
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }

    public void lookVertical(double dx){
        this.headXRot += dx;
        if (this.headXRot < -90) 
            this.headXRot = -90;
        if (this.headXRot > 90) 
            this.headXRot = 90;
    }

    public void lookHorizontal(double dx){
        this.headZRot += dx;
    }

    public void setForwardSpeed(double speed){
        this.side_dx = speed*Math.cos(Math.toRadians(90 + this.headZRot));
        this.side_dy = speed*Math.sin(Math.toRadians(90 + this.headZRot));
    }

    public void setSidewaysSpeed(double speed){
        this.forward_dx = speed*Math.cos(Math.toRadians(this.headZRot));
        this.forward_dy = speed*Math.sin(Math.toRadians(this.headZRot));
    }

    public void jump(double mag){
        if (thingBridge.onGround(this) || thingBridge.inWater(this))
            this.dz = mag;
    }

    public void accelerate(double fps, double dx, double dy, double dz){
        this.dz += dz/fps;
        if (thingBridge.inWater(this) && Math.abs(this.dz) > 5)
            this.dz = Math.signum(this.dz)*5;
    }


    public void stopX(){

    }

    public void stopY(){

    }

    public void stopZ(){
        this.dz = 0;
    }

    public void addBlock(Block b){
      inventory.addBlock(b);
    }

    public void rmBlock(Block b){
      inventory.useBlock(b.getClass());
    }

    public Vector3d getDirectionLooking(){
        double x = Math.cos(Math.toRadians(90 + this.headZRot))*Math.cos(Math.toRadians(this.headXRot));
        double y = Math.sin(Math.toRadians(90 + this.headZRot))*Math.cos(Math.toRadians(this.headXRot));
        double z = Math.sin(Math.toRadians(this.headXRot));
        return new Vector3d(x, y, z);
    }


    public boolean isUnderwater(){
        return this.thingBridge.isUnderwater(this.getCameraX(), this.getCameraY(), this.getCameraZ());
    }

    public void render(GL2 gl){

        Vector3d dir = this.getDirectionLooking();
        dir.times(10);
        
        Vector3i closestBlock = this.thingBridge.getClosestNonEmptyBlockOnVector(
                                            new Vector3d(this.getCameraX(),
                                                        this.getCameraY(),
                                                        this.getCameraZ()),
                                            dir);

        Vector3i closestEmptyBlock = this.thingBridge.getClosestEmptyBlockOnVector(
                                            new Vector3d(this.getCameraX(),
                                                        this.getCameraY(),
                                                        this.getCameraZ()),
                                            dir);

        if (closestBlock != null){
            Face f = HighlightBlockFace.getBetweenFace(closestBlock, closestEmptyBlock);
            HighlightBlockFace.highlight(gl, closestBlock, f);
        }
    }
        
    public boolean createBlock = false;
    public boolean deleteBlock = false;
    public Class blockToPlace = null;

    public Block getBlockToPlace(){
      if (blockToPlace == WaterBlock.class){
        return new WaterBlock(0, 8);
      }
      else {
        try {
          return (Block)this.blockToPlace.newInstance();
        } catch (Exception e){
          e.printStackTrace();
          System.exit(1);
        }
      }
      return null;
    }

    public int getBlockAvailability(Class c){
      return this.inventory.numBlock(c);
    }

    public BlockAction getBlockAction(){
        Vector3d dir = this.getDirectionLooking();
        dir.times(10);
        
        if (createBlock){
            createBlock = false;
            if (!inventory.hasBlock(blockToPlace))
              return null;
            Vector3i closestBlock = this.thingBridge.getClosestEmptyBlockOnVector(
                                                new Vector3d(this.getCameraX(),
                                                            this.getCameraY(),
                                                            this.getCameraZ()),
                                                dir);
            if (closestBlock != null){
                AddBlock ab = new AddBlock();
                ab.x = closestBlock.x;
                ab.y = closestBlock.y;
                ab.z = closestBlock.z;
                ab.block = this.getBlockToPlace();
                return ab;
            }
            else {
                return null;
            }
        }
        else if (deleteBlock){
            Vector3i closestBlock = this.thingBridge.getClosestNonEmptyBlockOnVector(
                                                new Vector3d(this.getCameraX(),
                                                            this.getCameraY(),
                                                            this.getCameraZ()),
                                                dir);
            deleteBlock = false;
            if (closestBlock != null){
                RemoveBlock ab = new RemoveBlock();
                ab.x = closestBlock.x;
                ab.y = closestBlock.y;
                ab.z = closestBlock.z;
                return ab;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }
}

