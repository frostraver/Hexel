package Hexel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.LinkedList;
import java.util.Queue;

import java.awt.event.MouseMotionAdapter;

import java.awt.AWTException;
import java.awt.Robot;

import java.util.HashSet;

import java.awt.Toolkit;
import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.image.MemoryImageSource;

import javax.media.opengl.awt.GLCanvas;

public class PCInput {

    public double frontDir;
    public double sideDir;

	public HashSet<Integer> keysPressed = new HashSet<Integer>();
    public Queue<Integer> keysTapped = new LinkedList<Integer>();

	private JFrame frame;
    private GLCanvas canvas;

    public double dx;
    public double dy;

	private int[] cx = new int[2];
	private int[] cy = new int[2];

	private Robot robot;

	private boolean cursorObject = false;
	private boolean cursorHidden = false;

    public PCInput(JFrame frame, GLCanvas canvas){
        this.frame = frame;
        this.canvas = canvas;

        try {
		    robot = new Robot();
        } catch(AWTException e){
            e.printStackTrace();
        }

        dx = 0;
        dy = 0;
        
        frontDir = 0;
        sideDir = 0;

		canvas.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				handleKeyPressed(e);
			}
			@Override
			public void keyReleased(KeyEvent e){
				handleKeyReleased(e);
			}
		});
		canvas.addMouseListener(new MouseAdapter(){
			public void mouseExited(MouseEvent e) {
				handleMouseExited(e);
			}
			public void mousePressed(MouseEvent e){
				handleMousePressed(e);
			}
		});
		canvas.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				handleMouseMoved(e);
			}
		});
    }

    public double getDX(){
        double tmp = this.dx;
        this.dx = 0;
        return tmp;
    }

    public double getDY(){
        double tmp = this.dy;
        this.dy = 0;
        return tmp;
    }

	public void handleKeyPressed(KeyEvent e){
		keysPressed.add(e.getKeyCode());
        keysTapped.add(e.getKeyCode());
	}

	public void handleKeyReleased(KeyEvent e){
		keysPressed.remove(e.getKeyCode());
	}


	public void handleMouseExited(MouseEvent e){
		if (frame.isActive() && cursorObject){
			robot.mouseMove(frame.getX() + canvas.getWidth()/2 + frame.getInsets().left, frame.getY() + canvas.getHeight()/2 + frame.getInsets().top);
			cx[0] = canvas.getWidth()/2;
			cy[0] = canvas.getHeight()/2;
			cx[1] = cx[0];
			cy[1] = cy[0];
		}
	}

	public void handleMouseMoved(MouseEvent e){
		if (cursorObject){
			if (
                e.getX() < 30 || 
                e.getX() > canvas.getWidth() - 30 || 
                e.getY() < 30 || 
                e.getY() > canvas.getHeight() - 30
            ){
				robot.mouseMove(frame.getX() + canvas.getWidth()/2 + frame.getInsets().left, frame.getY() + canvas.getHeight()/2 + frame.getInsets().top);
				cx[0] = canvas.getWidth()/2;
				cy[0] = canvas.getHeight()/2;
				cx[1] = cx[0];
				cy[1] = cy[0];
			}
            else {
				cx[1] = cx[0];
				cy[1] = cy[0];
				cx[0] = e.getX();
				cy[0] = e.getY();

                this.dx += cx[1] - cx[0];
                this.dy += cy[1] - cy[0];
            }
		}
	}

	public void handleMousePressed(MouseEvent e){

	}

	private void hideCursor(){
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(
				new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor =
			Toolkit.getDefaultToolkit().createCustomCursor
			(image, new Point(0, 0), "invisibleCursor");
		frame.setCursor(transparentCursor);
		cursorHidden = true;
	}

	private void showCursor(){
		frame.setCursor(null);
		cursorHidden = false;
	}

    public void toggleCursorObject(){
		cursorObject = !cursorObject;
		if (cursorHidden)
			showCursor();
		else {
			hideCursor();
		}
    }
}

