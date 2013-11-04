package Hexel.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ResourcePool<T> {
    private BlockingQueue<T> pool;
    private Class<T> klass;

    public ResourcePool(Class<T> klass){
        this.klass = klass;
        pool = new LinkedBlockingQueue<T>();
    }

    public T aquire(){
        try {
            if (pool.isEmpty()){
                T t = klass.newInstance();
                pool.add(t);
            }
            return pool.take();
        } catch (Exception e){
            System.out.println(("fail aquire"));
            System.exit(1);
            return null;
        }
    }

    public void recycle(T t) throws Exception {
        try {
            pool.put(t);
        } catch (Exception e){
            System.out.println(("fail aquire"));
            System.exit(1);
        }
    }

}

