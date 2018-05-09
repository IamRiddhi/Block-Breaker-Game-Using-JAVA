package keyboarddemo;
import java.awt.*;
import javax.swing.*;
public class CenteredFrame extends JFrame
{
    public CenteredFrame(String title, double wPer, double hPer)
    {
        Toolkit tk= Toolkit.getDefaultToolkit();
        Dimension d=tk.getScreenSize();
        setTitle(title);
        int w= (int)(wPer*d.width);
        int h= (int)(hPer*d.height);
        setSize(w,h);
        setLocation( (d.width-getWidth())/2, (d.height-getHeight())/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
