/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import Engine.Lua.PlayerLua;
import Engine.Lua.Sentence;
import Engine.PlayerStats;
import Engine.Screen;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Tomus
 */
public class DialogJFrame extends JFrame implements ActionListener{
    //Sentence[]sentences = null;
    JPanel DialogPanel = null;
    private PlayerLua playerLua = null;
    PlayerStats[] ps = null;
    private void ConstructConversation(Sentence[]sentences)
    {
        JLabel label = new JLabel(sentences[0].getSentence());
        DialogPanel.add(label);
        
        if (sentences.length > 1)
        {
            //JButton buttons[] = new JButton[sentences.length];
            for (int i = 1; i < sentences.length; i++)
            {
                DialogJButton btn = new DialogJButton(sentences[i].getSentence(), sentences[i].getValue());
                btn.addActionListener(this);
                DialogPanel.add(btn);
                //buttons[i] = btn;
            }
        }
        else
        {
            DialogJButton button = new DialogJButton("End", Sentence.EndConversationConst);
            button.addActionListener(this);
            DialogPanel.add(button);
        }
        //Container contentPane = getContentPane();
        //contentPane.add(listPane, BorderLayout.CENTER);
        pack();
    }
    
    public void ClearDialogPanel()
    {
        for (int a = 0; a < DialogPanel.getComponentCount(); a++)
        {
            Component c = DialogPanel.getComponent(a);
            if (c instanceof JButton)
            {
                ((JButton)c).removeActionListener(this);
            }
        }
        DialogPanel.removeAll();
    }
    
    Screen parent = null;
    public DialogJFrame(PlayerLua playerLua, Screen parent, String title, boolean isDialog,  PlayerStats[] ps)
    {
        super(title);
        setUndecorated(true);
        this.parent = parent;
        this.playerLua = playerLua;
        this.ps = ps;
        Container contentPane = getContentPane();
        DialogPanel = new JPanel();
        DialogPanel.setLayout(new BoxLayout(DialogPanel, BoxLayout.PAGE_AXIS));
        contentPane.add(DialogPanel, BorderLayout.CENTER);
        ClearDialogPanel();

        Sentence[] sentences = playerLua.DoConversation(Sentence.BeginConversationConst, ps);
        ConstructConversation(sentences);

	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    /*
    public DialogJDialog(Sentence[]sentences, JFrame parent, String title, boolean isDialog)
    {
        super(parent, title, isDialog);
        this.sentences = sentences;
        
        Container contentPane = getContentPane();
        listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        
        JLabel label = new JLabel(sentences[0].getSentence());
        listPane.add(label);
        
        if (sentences.length > 1)
        {
            JButton buttons[] = new JButton[sentences.length];
            for (int i = 1; i < buttons.length; ++i)
            {
                DialogJButton btn = new DialogJButton(sentences[i].getSentence(), sentences[i].getValue());
                btn.addActionListener(this);
                listPane.add(btn);
                buttons[i] = btn;
            }
        }
        contentPane.add(listPane, BorderLayout.CENTER);
        pack();
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }*/

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() instanceof DialogJButton)
        {
            DialogJButton djb = (DialogJButton)ae.getSource();
            int returnValue = djb.getValue();
            if (returnValue == Sentence.EndConversationConst)
            {
                setVisible(false);
                parent.DisposeDialog();
            }
            else
            {
                ClearDialogPanel();
                Sentence[] sentences = playerLua.DoConversation(returnValue, ps);
                ConstructConversation(sentences);
            }
        }
    }


}
