package game;

import java.awt.Color;
import javax.swing.JFrame;

/**
 * The JFrame in which the 2048 game is held
 * 
 * @author Cody Moose
 * @Version 0.4.3
 * @since 4/4/2016
 */
public class GameFrame extends JFrame
{
    /**
     * 
     */
    private static final long serialVersionUID = -997412424190795317L;
    GamePanel panel;
    
    /**
     * The JFrame in which 2048 is held
     */
    public GameFrame()
    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setTitle("2048");
        setBackground(Color.WHITE);
        setForeground(Color.WHITE);
        setVisible(true);
        setLocation(0, 0);
        panel = new GamePanel(this);
        add(panel);
        repaint();
    }
    
    public void newGame(){
        panel.setVisible(false);
        panel.setEnabled(false);
        remove(panel);
        panel.reset();
        panel = new GamePanel(this);
        panel.setEnabled(true);
        panel.setVisible(true);
        add(panel);
    }
}
