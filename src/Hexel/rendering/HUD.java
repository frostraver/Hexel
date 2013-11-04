package Hexel.rendering;

import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.awt.Overlay;
import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.Stroke;
import java.awt.BasicStroke;

import java.awt.Font;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import Hexel.Stage;

import Hexel.things.Player;
import Hexel.blocks.Block;

public class HUD {

    private Overlay overlay;
    private GLAutoDrawable drawable;

    private Color TRANSPARENT_COLOR = new Color(0.0f, 0.0f, 0.0f, 0.0f);

    private Color UNDERWATER_COLOR = new Color(0.0f, 0.0f, 1.0f, .5f);

    private Color CROSSHAIR_COLOR = new Color(1.0f, 1.0f, 1.0f, .1f);
    private Stroke CROSSHAIR_STROKE = new BasicStroke(3);

    private Color BLOCK_AVAILABILITY_COLOR = new Color(1.0f, 1.0f, 1.0f, .5f);

    private BufferedImage tex;

    private Player player; public void setPlayer(Player player){ this.player = player; }

    private Stage stage; public void setStage(Stage stage){ this.stage = stage; }

    public HUD(){
        try {
            this.tex = ImageIO.read(new File("img/atlas.png"));
        } catch(Exception e){
            System.out.println((e));
            System.exit(1);
        }
    }

    public void init(GLAutoDrawable drawable){
        overlay = new Overlay(drawable);
        this.drawable = drawable;
    }

    private int w;
    private int h;

    public void updateSize(int w, int h){
        this.w = w;
        this.h = h;
    }

    public void display(){
        Graphics2D page = overlay.createGraphics();
        page.setBackground(TRANSPARENT_COLOR);
        page.clearRect(0, 0, w, h);
        if (this.stage == Stage.LOADING){
            drawLoading(page);
        }
        else {
            drawUnderwaterOverlay(page);
            drawCrosshairs(page);
            drawCurrentBlock(page);
        }
        overlay.markDirty(0, 0, w, h);
        overlay.drawAll();
        page.dispose();
    }

    private void drawLoading(Graphics2D page){
        page.setColor(Color.BLACK);
        page.fillRect(0, 0, w, h);
        page.setColor(Color.WHITE);
        page.drawString("loading...", 25, 25);
    }

    private void drawUnderwaterOverlay(Graphics2D page){
        if (!player.isUnderwater())
            return;
        page.setColor(UNDERWATER_COLOR);
        page.fillRect(0, 0, w, h);
    }

    private void drawCrosshairs(Graphics2D page){
        int cs = Math.min(w/24, h/24);
        int cw = cs;
        int ch = cs*3/4;

        page.setColor(CROSSHAIR_COLOR);
        page.setStroke(CROSSHAIR_STROKE);
        page.drawLine(w/2 - cw/2, h/2, w/2 + cw/2, h/2);
        page.drawLine(w/2, h/2 - ch/2, w/2, h/2 + ch/2);
    }

    private void drawCurrentBlock(Graphics2D page){
        Block b = player.getBlockToPlace();
        if (b == null)
            return;
        int availability = player.getBlockAvailability(b.getClass());

        int i = b.getTopTextureIndex();
        int texW = this.tex.getWidth()/TEX.HOR;
        int texH = this.tex.getHeight()/TEX.VER;
        int texX = (i % TEX.HOR)*texW;
        int texY = (i / TEX.HOR)*texH;

        BufferedImage subtex = this.tex.getSubimage(texX, texY, texW, texH);

        AffineTransform at = new AffineTransform();
        int iw = Math.max(200, h/5);
        int ih = Math.max(200, h/5);
        at.translate(0, h-ih/2);
        at.scale(iw*1.0/subtex.getWidth(), ih*1.0/subtex.getHeight());
        at.rotate(Math.PI/4);
        page.drawImage(subtex, at, null);

        Font font = new Font("Arial", Font.PLAIN, 64);
        page.setFont(font);
        page.setColor(BLOCK_AVAILABILITY_COLOR);
        page.drawString("" + availability, texH/2, h-texH/2);
    }

}

