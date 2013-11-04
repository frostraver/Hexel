package Hexel.things;

import java.util.Hashtable;

import Hexel.blocks.Block;
import Hexel.blocks.DebugBlock;
import Hexel.blocks.WaterBlock;

public class Inventory {

  private Hashtable<Class, Integer> inventory = new Hashtable<Class, Integer>();

  public Inventory(){
    inventory.put(DebugBlock.class, 1000000);
    inventory.put(WaterBlock.class, 1000000);
  }

  public void addBlock(Block block){
    Class blockClass = block.getClass();
    int v = 0;
    if (inventory.containsKey(blockClass))
      v = inventory.get(blockClass);
    inventory.put(blockClass, v+1);
  }

  public boolean hasBlock(Class blockClass){
    return inventory.containsKey(blockClass) && inventory.get(blockClass) > 0;
  }
  
  public boolean useBlock(Class blockClass){
    if (this.hasBlock(blockClass)){
      inventory.put(blockClass, inventory.get(blockClass)-1);
      return true;
    }
    else
      return false;
  }

  public int numBlock(Class blockClass){
    if (!this.hasBlock(blockClass))
      return 0;
    return inventory.get(blockClass);
  }
}


