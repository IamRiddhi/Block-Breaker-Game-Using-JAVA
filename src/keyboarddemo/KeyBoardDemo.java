package keyboarddemo;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author RIDDHI DUTTA
 */
public class KeyBoardDemo 
{
    public static int Value;
    public static void main(String[] args)
    {
        JFrame f= new CenteredFrame("Paddle Game By Riddhi Dutta", 1, 1);
        f.setResizable(false);
        f.add(new GamePanel(Value));
        f.setVisible(true);
    }
}
