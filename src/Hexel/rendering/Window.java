package Hexel.rendering;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import javax.swing.JFrame;

public class Window {

    public GLCanvas canvas; public GLCanvas getCanvas(){ return this.canvas; }
    public JFrame frame; public JFrame getFrame(){ return this.frame; }

    public Window() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        caps.setDepthBits(24);
        canvas = new GLCanvas(caps);

        frame = new JFrame("Hexel");
        frame.setSize(1280, 800);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }
}

