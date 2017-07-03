/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collatz;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Amadeusz
 */
public class Punkt extends Point {
    private int orientation, value, angle;
    private Punkt previous = null;
    private Punkt next = null;
    
    public Punkt(int x, int y, int value, int orientation, Punkt previous, int angle) {
        super(x, y);
        this.value = value;
        this.orientation = orientation;
        this.previous = previous;
        this.angle = angle;
    }
    
    public int getOrientation() {
        return orientation;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setAngle(int angle) {
        this.angle = angle;
    }
    
    public int getAngle() {
        return angle;
    }
    
    public void setNext(Punkt punkt) {
        this.next = punkt;
    }
    
    public Punkt getNext() {
        return next;
    }
    
    public Punkt getPrevious() {
        return previous;
    }
    
    public void drawLineToPrevious(Graphics2D g2d) {
        if(previous != null) {
            g2d.setColor(new Color(0, (3*orientation)%256, (4*orientation+20)%256));
            g2d.drawLine(x, y, (int)previous.getX(), (int)previous.getY());
        }
    }
    
    public void draw(Graphics2D g2d, int radius) {
        g2d.fillOval(x - radius/2, y - radius/2, radius, radius);
    }
}
