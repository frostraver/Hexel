package Hexel.things;

import Hexel.things.Thing;
import Hexel.things.Movement;
import Hexel.things.Movable;
import Hexel.things.Cuboid;

import Hexel.math.Vector2d;
import Hexel.math.Vector3d;
import Hexel.math.Vector3i;
import Hexel.math.HexGeometry;

import Hexel.blocks.Block;
import Hexel.blocks.EmptyBlock;

import Hexel.things.Movable;
import Hexel.things.blockManipulation.BlockManipulator;
import Hexel.things.blockManipulation.BlockAction;
import Hexel.things.blockManipulation.AddBlock;
import Hexel.things.blockManipulation.RemoveBlock;
import Hexel.things.ThingTools;

import Hexel.things.ThingBridge;

import Hexel.chunk.Chunks;

import java.util.HashSet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ThingSimulator {

    private HashSet<Thing> things = new HashSet<Thing>();
    
    private Chunks chunks;

    private ThingBridge thingBridge; public ThingBridge getThingBridge(){ return this.thingBridge; }
    private ThingTools thingTools;

    public ThingSimulator(Chunks chunks){
        this.chunks = chunks;
        this.thingBridge = new ThingBridge(chunks);
        this.thingTools = new ThingTools(chunks);
    }
    
    public void addThing(Thing thing){
        this.things.add(thing);
    }

    public void step(double fps){
        Iterator<Thing> iter = things.iterator();
        Vector3i tmp = new Vector3i();
        while (iter.hasNext()){
            Thing thing = iter.next();

            if (thing instanceof Movable){
                Movable movable = (Movable)thing;

                movable.accelerate(fps, 0, 0, -9.8);
                Vector3d reqMoveVector = movable.getReqMoveVector(fps);
                Movement movement = getMovement((Volumetric)movable, reqMoveVector, tmp);
                if (movement.stoppedZ){
                    movable.stopZ();
                }
                movable.applyMoveVector(movement);
            }
            if (thing instanceof BlockManipulator){
                BlockAction action = ((BlockManipulator)thing).getBlockAction();
                if (action instanceof AddBlock){
                    AddBlock addBlockAction = (AddBlock)action;
                    Block original = chunks.getBlock(addBlockAction.x, addBlockAction.y, addBlockAction.z, tmp);
                    chunks.setBlock(addBlockAction.x, addBlockAction.y, addBlockAction.z, addBlockAction.block, tmp);
                    if (thing instanceof Volumetric){
                        if (thing instanceof Cuboid){
                            if (thingTools.fixOffset((Cuboid)thing, new Vector3d(0, 0, 0), tmp, false) != null)
                                chunks.setBlock(addBlockAction.x, addBlockAction.y, addBlockAction.z, original, tmp);
                            else {
                              if (thing instanceof InventoryOwner){
                                InventoryOwner inventoryOwner = (InventoryOwner)thing;
                                inventoryOwner.rmBlock(addBlockAction.block);
                              }
                            }
                        }

                    }
                }
                else if (action instanceof RemoveBlock){
                    RemoveBlock rmBlockAction = (RemoveBlock)action;
                    Block b = chunks.getBlock(rmBlockAction.x, rmBlockAction.y, rmBlockAction.z, tmp);
                    if (b.health < 0){
                      chunks.setBlock(rmBlockAction.x, rmBlockAction.y, rmBlockAction.z, new EmptyBlock(), tmp);
                      if (thing instanceof InventoryOwner){
                        InventoryOwner inventoryOwner = (InventoryOwner)thing;
                        inventoryOwner.addBlock(b);
                      }
                    }
                    else {
                      b.health -= .025;
                      chunks.setBlock(rmBlockAction.x, rmBlockAction.y, rmBlockAction.z, b, tmp);
                    }
                    
                }
            }

        }
        //System.out.System.out.println(((b-a) + " " + (c-b)));
    }

    public boolean equal(double a, double b, double epsilon){
        return Math.abs(a - b) < epsilon;
    }

    public Movement getMovement(Volumetric v, Vector3d reqMoveVector, Vector3i tmp){
        Movement m = new Movement();

        Vector3d toFix = null;
        if (v instanceof Cuboid){
            toFix = thingTools.fixOffset((Cuboid)v, reqMoveVector, tmp, true);
        }


        if (toFix == null){
            m.x = reqMoveVector.x;
            m.y = reqMoveVector.y;
            m.z = reqMoveVector.z;
        }
        else {
            m.x = reqMoveVector.x + toFix.x;
            m.y = reqMoveVector.y + toFix.y;
            m.z = reqMoveVector.z + toFix.z;

            if (toFix.z != 0){
                m.stoppedZ = true;
            }
        }

        return m;
    }
}


