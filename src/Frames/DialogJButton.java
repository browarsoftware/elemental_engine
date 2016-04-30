/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import javax.swing.JButton;

/**
 *
 * @author Tomus
 */
public class DialogJButton extends JButton{
    private int value = -1;
    public DialogJButton(String text, int value)
    {
        super(text);
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
}
