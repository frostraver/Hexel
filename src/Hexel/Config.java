package Hexel;

import java.util.Scanner;
import java.io.FileReader;

import java.util.Hashtable;

import Hexel.util.CharToKeyEvent;

public class Config {

    public Hashtable<String, Integer> controls = new Hashtable<String, Integer>();

    public Config(){
        try {
            Scanner in = new Scanner(new FileReader("hexel.config"));

            while (in.hasNextLine()){
                String s = in.nextLine();
                String[] parts = s.split("\\s+");
                String control = parts[0];
                String key = parts[1];
                setControl(control, key);
            }
        } catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setControl(String control, String key_descriptor){
        char key;
        if (key_descriptor.equals("SPACE")){
            key = ' ';
        }
        else {
            key = key_descriptor.charAt(0);
        }
        controls.put(control, CharToKeyEvent.convert(key));
    }

    
}

