/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dstealth;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author 732897
 */
public class InputHandler  implements KeyListener,MouseListener{
    
    public InputHandler(Game game){
        game.addKeyListener(this);
        game.addMouseListener(this);
    }

    
    public class Key {
        private int numTimesPressed =0;
        private boolean pressed = false;
        public boolean isPressed(){
            return pressed;
        }
        public int getNumTimesPressed(){
            return numTimesPressed;
        }
        public void toggle(boolean isPressed){
            pressed=isPressed;
            if(pressed) numTimesPressed++;
        }
    }
    public class Mouse {
        public boolean click = false;
        public int x = 0;
        public int y = 0;
    }
    
    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    public Key space = new Key();
    public Mouse mouse = new Mouse();
    public Key num1 = new Key();
    public Key num2 = new Key();
    public Key num3 = new Key();
    
    public void mousePressed(MouseEvent me) {
        mouse.click=true;
        PointerInfo a = MouseInfo.getPointerInfo();
        Point point = new Point(a.getLocation());
        SwingUtilities.convertPointFromScreen(point, me.getComponent());
        mouse.x=(int) point.getX();
        mouse.y=(int) point.getY();
    }
    
    public void mouseClicked(MouseEvent me) {
        
    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }
    
    public void keyTyped(KeyEvent e) {
     
    }

    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(),true);
    }

    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(),false );
    }
    
    public void toggleKey(int keyCode, boolean isPressed){
        if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            up.toggle(isPressed);
        }
        if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            down.toggle(isPressed);
        }
        if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            left.toggle(isPressed);
        }
        if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            right.toggle(isPressed);
        }
        if(keyCode == KeyEvent.VK_SPACE) {
            space.toggle(isPressed);
        }
        if(keyCode == KeyEvent.VK_1){
            num1.toggle(isPressed);
        }
        if(keyCode == KeyEvent.VK_2){
            num2.toggle(isPressed);
        }
        if(keyCode == KeyEvent.VK_3){
            num3.toggle(isPressed);
        }
    }
}
