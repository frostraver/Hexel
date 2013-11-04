//public class WaterRule implements BlockRule {
//    public int stepsUntilConsider(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
//        return (5 - (steps % 5))%5;
//    }
//    public boolean isMatch(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
//        WaterBlock wb = (WaterBlock)b;
//        if (wb.recalc == null ||
//            wb.recalc){
//            return true;
//        }
//        if (wb.lowestTarget == null){
//            return true;
//        }
//        { // CHANGE ME TO LOOK OVER EVERY NEIGHBOR
//            Block below = chunks.getBlock(x, y, z-1, tmp);
//
//            boolean couldFall = true;
//            if (below instanceof EmptyBlock){
//                couldFall = true;
//            }
//            if (below instanceof WaterBlock){
//                WaterBlock wBelow = (WaterBlock)below;
//                if (wBelow.top < 8)
//                    couldFall = true;
//            }
//            if (couldFall){
//                wb.lowestTarget.z = Math.min(z-1, wb.lowestTarget.z);
//            }
//        }
//        if (wb.lowestTarget.z <= z){
//            return true;
//        }
//        return false;
//    }
//    public ArrayList<BlockDelta> apply(int x, int y, int z, Block b, Chunk c, Chunks chunks, int steps, Vector3i tmp){
//        ArrayList<BlockDelta> deltas = new ArrayList<BlockDelta>();
//
//        //move until find, if no find, set, spread
//
//        return deltas;
//    }
//}

