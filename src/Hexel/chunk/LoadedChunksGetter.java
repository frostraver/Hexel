package Hexel.chunk;

import Hexel.math.Vector3i;
import java.util.HashSet;

public interface LoadedChunksGetter {
    public HashSet<Vector3i> getLoadedChunks();
}

