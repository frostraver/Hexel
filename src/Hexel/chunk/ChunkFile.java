package Hexel.chunk;

import Hexel.chunk.Chunk;
import Hexel.math.Vector3i;

import java.io.File;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ChunkFile {

    private static Object lock = new Object();
    public static void save(Chunk chunk){
        synchronized (lock){
            Vector3i p = chunk.getXYZ();

            String path = getPath(p);

            try {
                FileOutputStream fileOut = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(chunk);
                out.close();
                fileOut.close();
            } catch(IOException e){
                e.printStackTrace();
                System.exit(1);
            }
            chunk.modified = false;
        }

    }

    public static Chunk load(Vector3i p){

        synchronized (lock){
            Chunk c = null;
            try {
                String path = getPath(p);
                FileInputStream fileIn = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                c = (Chunk) in.readObject();
                in.close();
                fileIn.close();
            } catch(IOException e){
                e.printStackTrace();
                System.exit(1);
            } catch(ClassNotFoundException e){
                System.out.println(("Chunk class not found"));
                e.printStackTrace();
                System.exit(1);
            }
            c.modified = false;
            return c;
        }
    }

    public static boolean has(Vector3i p){
        synchronized (lock){
            String path = getPath(p);
            File f = new File(path);
            return f.exists();
        }
    }

    private static String getFolder(){
        return "state/chunks/";
    }

    private static String getFilename(Vector3i p){
        return p + ".ser";
    }

    private static String getPath(Vector3i p){
        return getFolder() + getFilename(p);
    }
}

