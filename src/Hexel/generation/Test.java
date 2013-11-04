package Hexel.generation;

import Hexel.generation.plate.*;

import Hexel.math.Vector2i;

import Hexel.Cleanup;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;

public class Test { public static void main(String[] args){ new Test(); }
    public Test(){
        //testPlateChunks();
        //testPlateSumChunks();
        testTerrainMap();
    }

    public void testTerrainMap(){
        Cleanup cleanup = new Cleanup();
        final TerrainMap tm = new TerrainMap(cleanup);
        draw(new Canvas(){
            @Override
            public void paint(Graphics page){

                Vector2i tmp = new Vector2i();

                page.setColor(Color.white);
                page.fillRect(0, 0, this.getWidth(), this.getHeight());
                int minX = Integer.MAX_VALUE;
                int maxX = Integer.MIN_VALUE;
                for (int x = 0; x < this.getWidth()/4; x++){
                    for (int y = 0; y < this.getHeight()/4; y++){
                        int cx = 32*(x-this.getWidth()/8);
                        int cy = 16*(y-this.getHeight()/8);
                        int h = tm.getBlockHeight(cx, cy, tmp);
                        if (h < minX)
                            minX = h;
                        if (h > maxX)
                            maxX = h;
                    }
                }

                for (int x = 0; x < this.getWidth()/4; x++){
                    for (int y = 0; y < this.getHeight()/4; y++){
                        int cx = 32*(x-this.getWidth()/8);
                        int cy = 16*(y-this.getHeight()/8);
                        int h = tm.getBlockHeight(cx, cy, tmp);
                        if (h < 0){
                            page.setColor(Color.blue);
                        }
                        else {
                            if (maxX - minX != 0)
                                h = 255*(h-minX)/(maxX-minX);
                            Color c = new Color(h, h, h);
                            page.setColor(c);
                        }
                        if (cx/32 == 40 && cy/16 == 60)
                            page.setColor(Color.magenta);
                        page.fillRect(x*4, y*4, 4, 4);
                    }
                }
            }
        });
    }

    public void testPlateSumChunks(){
        final PlateSumChunks pscs = new PlateSumChunks();

        draw(new Canvas(){
            @Override
            public void paint(Graphics page){
                page.setColor(Color.white);
                page.fillRect(0, 0, this.getWidth(), this.getHeight());
                int minX = Integer.MAX_VALUE;
                int maxX = Integer.MIN_VALUE;
                for (int x = 0; x < this.getWidth()/4; x++){
                    for (int y = 0; y < this.getHeight()/4; y++){
                        PlateSum ps = pscs.getPlateSum(x, y);
                        int h = (int)(ps.additions - ps.subtractions) + ps.baseHeight;
                        if (h < minX)
                            minX = h;
                        if (h > maxX)
                            maxX = h;
                    }
                }

                for (int x = 0; x < this.getWidth()/4; x++){
                    for (int y = 0; y < this.getHeight()/4; y++){
                        PlateSum ps = pscs.getPlateSum(x, y);
                        int h = (int)(ps.additions - ps.subtractions) + ps.baseHeight;
                        if (h < 0){
                            page.setColor(Color.blue);
                        }
                        else if (h < 10){
                            page.setColor(Color.green);
                        }
                        else {
                            if (maxX - minX != 0)
                                h = 255*(h-minX)/(maxX-minX);
                            Color c = new Color(h, h, h);
                            page.setColor(c);
                        }
                        page.fillRect(x*4, y*4, 4, 4);
                    }
                }
            }
        });
    }

    public void draw(Canvas canvas){
        JFrame f = new JFrame();
        final int W = 1280;
        final int H = 800;
        f.add(canvas);
        f.setSize(W, H);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static void printPlateGrid(int[][] grid){
        for (int y = 0; y < PlateChunk.HEIGHT; y++){
            for (int x = 0; x < PlateChunk.WIDTH; x++){
                if (grid[x][y] == 0)
                    System.out.print(" ");
                else
                    System.out.print((char)(grid[x][y] + '0'));
            }
            //System.out.println(());
        }
    }
    
}

