/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collatz;

import javax.swing.JFrame;

/**
 *
 * @author Amadeusz
 */
public class MainFrame extends JFrame {
    public MainFrame() {
        super("Collatz Conjecture");
        
        add(new MainPanel());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
