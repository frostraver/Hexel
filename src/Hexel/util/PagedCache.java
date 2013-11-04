package Hexel.util;

import java.util.concurrent.ConcurrentHashMap;

import Hexel.util.Generator;

public abstract class PagedCache<U,T> {

    private ConcurrentHashMap<U,T> inMemoryTs = new ConcurrentHashMap<U,T>();
    private Object[] locks = new Object[100];
    private Generator<T,U> generator;

    public PagedCache(Generator<T,U> generator){
        this.generator = generator;
        for (int i = 0; i < locks.length; i++){
            locks[i] = new Object();
        }
    }

    public T get(U key){
        if (this.hasTInMemory(key)){
            return this.inMemoryTs.get(key);
        }
        maybeGenT(key);
        return this.inMemoryTs.get(key);
    }

    private void maybeGenT(U key){
        int hash = Math.abs(key.hashCode() % locks.length);
        synchronized(locks[hash]){
            if (this.hasTInMemory(key))
                return;
            T val = generator.gen(key);
            inMemoryTs.put(copyKey(key), val);
        }
    }

    private boolean hasTInMemory(U key){
        return inMemoryTs.containsKey(key);
    }

    public void unloadT(U key){
        inMemoryTs.remove(key);
    }

    protected abstract U copyKey(U key);
}


