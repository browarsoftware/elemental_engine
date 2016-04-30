/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Forces;

import javax.swing.JFrame;

/**
 *
 * @author Tomus
 */
public class MadDrawerStarter {
    public static void main(String args[])
    {
        JFrame frame = new MapDrawer();
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }
}
