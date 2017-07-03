/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collatz;

import java.awt.BasicStroke;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Amadeusz
 */
public class MainPanel extends JPanel {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    
    private int firstX, firstY, angle, max, lineStroke, lastIlePunktow, 
            resizePadding, resizePaddingTop, xMax, xMin, yMax, yMin, 
            boundDiffLimit;
    private double length;
    private boolean isRunning, isInitialized;
    
    private JButton buttonStartStop, buttonReset, buttonNext;
    private TextField tfLength, tfAngle, tfValueLimit, tfStroke;
    private JLabel labelLength, labelAngle, labelValueLimit, labelStroke;
    private JSlider sliderSpeed;
    
    private ArrayList<Punkt> punkty = new ArrayList<>();
    
    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        init();
    }
    
    public void init() {
        initStuff();
        initTextFields();
        initLabels();
        firstPoint();
        initButtons();
        initControlPanel();
    }
    
    public void initStuff() {
        isRunning = false;
        firstX = 600;
        firstY = 400;
        xMax = firstX;
        yMax = firstY;
        xMin = firstX;
        yMin = firstY;
        lastIlePunktow = 0;
        resizePadding = 10;
        resizePaddingTop = 100;
        isInitialized = false;
        boundDiffLimit = 2;
    }
    
    public void initParameters() {
        angle = Integer.parseInt(tfAngle.getText());
        max = Integer.parseInt(tfValueLimit.getText());
        lineStroke = Integer.parseInt(tfStroke.getText());
        length = Integer.parseInt(tfLength.getText());
    }
    
    public void firstPoint() {
        punkty.add(new Punkt(firstX, firstY, 1, 1, null, 0));
    }
    
    public void initButtons() {
        buttonStartStop = new JButton("Start");
        buttonStartStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isInitialized) {
                    initParameters();
                    isInitialized = true;
                }
                if(isRunning) {
                    buttonStartStop.setText("Start");
                    isRunning = false;
                } else {
                    buttonStartStop.setText("Stop");
                    isRunning = true;
                    
                    repaint();
                }
            }
        });
        
        buttonReset = new JButton("Reset");
        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initStuff();
                punkty.clear();
                firstPoint();
                buttonStartStop.setText("Start");
                repaint();
            }
        });
        
        buttonNext = new JButton("Next");
        buttonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isInitialized) {
                    initParameters();
                    isInitialized = true;
                }
                isPointNeeded();
                repaint();
            }
        });
        
        sliderSpeed = new JSlider(1,100,10);
    }
    
    public void initTextFields() {
        tfLength = new TextField("10",2);
        tfAngle = new TextField("10",2);
        tfValueLimit = new TextField("10000",6);
        tfStroke = new TextField("4",1);
    }
    
    public void initLabels() {
        labelLength = new JLabel("Długość linii");
        labelAngle = new JLabel("Kąt odchylenia");
        labelValueLimit = new JLabel("Limit liczbowy");
        labelStroke = new JLabel("Grubość linii");
    }
    
    public void initControlPanel() {
        JPanel controlPanel1 = new JPanel();
        JPanel controlPanel2 = new JPanel();
        controlPanel1.add(labelValueLimit);
        controlPanel1.add(tfValueLimit);
        controlPanel1.add(labelLength);
        controlPanel1.add(tfLength);
        controlPanel1.add(labelAngle);
        controlPanel1.add(tfAngle);
        controlPanel1.add(labelStroke);
        controlPanel1.add(tfStroke);
        controlPanel2.add(buttonStartStop);
        controlPanel2.add(sliderSpeed);
        controlPanel2.add(buttonReset);
        controlPanel2.add(buttonNext);
        add(controlPanel1);
        add(controlPanel2);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        int delay;
        
        BasicStroke stroke = new BasicStroke(lineStroke);
        g2d.setStroke(stroke);
        
        for(int i=1; i<punkty.size(); i++) {
            punkty.get(i).drawLineToPrevious(g2d);
        }
        
        if(isRunning) {
            delay = 1000/sliderSpeed.getValue();
            
            isPointNeeded();
            
            try {
                Thread.sleep(delay);
            }
            catch (Exception ex) {   
            }
            repaint();
        }
    }
    
    public void isPointNeeded() {
        int ilePunktow = punkty.size();
        for(int i=lastIlePunktow; i<ilePunktow; i++) {
            if(punkty.get(i).getNext()==null) {
                nextPoint(punkty.get(i));
            }
        }
        lastIlePunktow = ilePunktow-1;
                
        moveNeeded();
        resizeNeeded();
    }
    
    public void nextPoint(Punkt current) {
        int value = current.getValue();
        int anglePrev = current.getAngle();
        int newX, newY, newAngle;
        double angleRad;
        Punkt temp;
        
        if(value<max) {
            newAngle = anglePrev + angle;
            angleRad = newAngle*3.1415/180;
            newX = (int)current.getX() - (int)(length*Math.cos(angleRad));
            newY = (int)current.getY() - (int)(length*Math.sin(angleRad));
            temp = new Punkt(newX, newY, value*2, current.getOrientation(), current, newAngle);
            punkty.add(temp);
            current.setNext(temp);
            checkBounds(temp);
        
            if((value*2-1)%3==0 && value!=2) {
                newAngle = anglePrev - angle;
                newX = (int)current.getX() - (int)(length*Math.cos(angleRad));
                newY = (int)current.getY() - (int)(length*Math.sin(angleRad));
                temp = new Punkt(newX, newY, (value*2-1)/3, current.getOrientation()+1, current, newAngle);
                punkty.add(temp);
                checkBounds(temp);
            }
        }
    }
    
    public void resizeNeeded() {
        if(xMax > WIDTH - resizePadding || xMin < 0 + resizePadding || 
                yMax > HEIGHT - resizePadding || yMin < 0 + resizePaddingTop) {
            resize();
        }
    }
    
    public void resize() {
        int prevX, prevY;
        double angleRad;
        Punkt temp;
        if(length>2) {
            length=length-0.1;
        }
        if(lineStroke>2) {
            lineStroke = lineStroke - 1;
        }
        xMax = firstX;
        xMin = firstX;
        yMax = firstY;
        yMin = firstY;
        for(int i=1; i<punkty.size(); i++) {
            temp = punkty.get(i);
            prevX = (int)temp.getPrevious().getX();
            prevY = (int)temp.getPrevious().getY();
            angleRad = temp.getAngle()*3.1415/180;
            temp.setLocation(prevX - (int)(length*Math.cos(angleRad)), 
                    prevY - (int)(length*Math.sin(angleRad)));
            checkBounds(temp);
        }
    }
    
    public void checkBounds(Punkt point) {
        int x = (int)point.getX();
        int y = (int)point.getY();
        if(x>xMax)
            xMax = x;
        if(x<xMin)
            xMin = x;
        if(y>yMax)
            yMax = y;
        if(y<yMin)
            yMin = y;
    }
    
    public void moveNeeded() {
        int boundDiffX = (xMin - 0) - (WIDTH - xMax);
        int boundDiffY = (yMin - 0) - (HEIGHT - yMax + 70);
        
        if(Math.abs(boundDiffX)>boundDiffLimit || Math.abs(boundDiffY)>boundDiffLimit) {
            move(boundDiffX, boundDiffY);
        }
    }
    
    public void move(int dX,int dY) {
        int prevX, prevY;
        Punkt temp;
        for(int i=0; i<punkty.size(); i++) {
            temp = punkty.get(i);
            prevX = (int)temp.getX();
            prevY = (int)temp.getY();
            temp.setLocation(prevX - dX/2, prevY - dY/2);
        }
        xMax -= dX/2;
        xMin -= dX/2;
        yMax -= dY/2;
        yMin -= dY/2;
    }
}
