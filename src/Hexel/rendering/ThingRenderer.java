package Hexel.rendering;

import javax.media.opengl.GL2;
import java.util.HashSet;

import Hexel.things.Thing;

import Hexel.rendering.Renderable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThingRenderer {

    private HashSet<Thing> things = new HashSet<Thing>();

    public void addThing(Thing thing){
        this.things.add(thing);
    }
    
    public void render(GL2 gl){
        Iterator<Thing> iter = things.iterator();
        while (iter.hasNext()){
            Thing thing = iter.next();
            if (thing instanceof Renderable){
                Renderable r = (Renderable)thing;
                r.render(gl);
            }
        }
    }
}

