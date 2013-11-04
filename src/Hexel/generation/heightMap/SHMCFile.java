package Hexel.generation.heightMap;

import Hexel.math.Vector2i;

import java.io.File;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class SHMCFile {
    public static void save(Vector2i p, SmoothHeightMapChunk shmc){

        String path = getPath(p);

        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(shmc);
            out.close();
            fileOut.close();
        } catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static SmoothHeightMapChunk load(Vector2i p){

        SmoothHeightMapChunk shmc = null;
        try {
            String path = getPath(p);
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            shmc = (SmoothHeightMapChunk) in.readObject();
            in.close();
            fileIn.close();
        } catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        } catch(ClassNotFoundException e){
            System.out.println(("SHMC class not found"));
            e.printStackTrace();
            System.exit(1);
        }
        return shmc;
    }

    public static boolean has(Vector2i p){
        String path = getPath(p);
        File f = new File(path);
        return f.exists();
    }

    private static String getFolder(){
        return "state/shmcs/";
    }

    private static String getFilename(Vector2i p){
        return p + ".ser";
    }

    private static String getPath(Vector2i p){
        return getFolder() + getFilename(p);
    }
}

