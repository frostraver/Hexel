package Hexel.util;

public interface Generator<T,U> {
    public abstract T gen(U key);
}

