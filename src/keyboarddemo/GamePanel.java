package keyboarddemo;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
/**
 *
 * @author RIDDHI DUTTA
 */
public class GamePanel extends JPanel implements ActionListener
{
    private Point ballpt, paddlept;
    private final int RADIUS=10;
    private int dirPaddle; // 2->left 3->right
    private int xincr;
    private int yincr;
    private static final long toleranceMillis=200;
    private boolean paddleMoving,paddleMoved,allDone;
    private Timer t,paddleWatcher,scoreWatcher,credits;
    private final Color brickFillColor= new Color(150,22,11);
    private final Color brickBorderColor= brickFillColor.brighter().brighter().brighter();
    private int brickWidth, brickHeight;
    private final int BRICK_GAP=3;
    private  Image bgimg,image,paddleImage;
    private  InputStream in,in1,in2,in3;
    private   AudioStream as,as1,as2,as3;
    private int score,xmera,ymera,xbharat,ybharat,xmahan,ymahan,cury;
    
    class Brick
    {
        private int x,y;
        private boolean isBrocken;
        
    }
    private Brick bricks[] = new Brick[21];
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        ballpt.x+=xincr;
        if(ballpt.x-RADIUS<=0 || ballpt.x+RADIUS>=getWidth())
        {
            xincr= - xincr;
            ballpt.x+=xincr;
            try
            {
                in3 =  getClass().getResourceAsStream("/keyboarddemo/Sounds/hit.wav");
            }
            catch(Exception es)
            {

            }

            try
            {
                as3 = new AudioStream(in3);
            }
            catch(Exception ess)
            {

            }
            AudioPlayer.player.start(as3);
        }
        ballpt.y+=yincr;
        if(ballpt.y-RADIUS<=0)
        {
            yincr= -  yincr;
            ballpt.y+= yincr;
            try
            {
                in3 =  getClass().getResourceAsStream("/keyboarddemo/Sounds/hit.wav");
            }
            catch(Exception es)
            {

            }

            try
            {
                as3 = new AudioStream(in3);
            }
            catch(Exception ess)
            {

            }
            AudioPlayer.player.start(as3);
        }
        if(ballpt.y+RADIUS>=getHeight()) //if ball touched ground, you are out.
        {
            t.stop(); paddleWatcher.stop(); scoreWatcher.stop();
            ballpt.y= getHeight()+200;
            repaint();
            AudioPlayer.player.start(as2);
            JOptionPane.showMessageDialog(this, " GAME OVER! YOUR SCORE WAS " + score);
      
            allDone=true;
             cury=getHeight();
            credits.start();
        }
        if(checkColiision())
        {
            try
            {
                in3 =  getClass().getResourceAsStream("/keyboarddemo/Sounds/hit.wav");
            }
            catch(Exception es)
            {

            }

            try
            {
                as3 = new AudioStream(in3);
            }
            catch(Exception ess)
            {

            }
            AudioPlayer.player.start(as3);

            yincr=-yincr;
            (ballpt.y)= paddlept.y-2*RADIUS;
            if(paddleMoving)
            {
                if(xincr>0 && dirPaddle==2 || xincr<0 && dirPaddle==3) //if ball and paddle are moving in the same direction decrease xincr(magnitude) by 2.
                {
                    if(xincr>0)
                      xincr-=2;
                    else
                      xincr+=2;
                }
                else
                { 
                    if(xincr>0)
                      xincr+=2;
                    else
                      xincr-=2;
                }
            }
        }
        
        //check if ball collides with brick
        boolean gameOver= true;
        for(int i=0;i<21;i++)
        {
            if(!bricks[i].isBrocken)
            {
                gameOver= false;
                if(ballpt.x+RADIUS>=bricks[i].x && ballpt.x+RADIUS<=bricks[i].x+brickWidth
                  || ballpt.x-RADIUS>=bricks[i].x && ballpt.x-RADIUS<=bricks[i].x+brickWidth)
                    if(ballpt.y+RADIUS>=bricks[i].y && ballpt.y+RADIUS<=bricks[i].y+brickHeight
                  || ballpt.y-RADIUS>=bricks[i].y && ballpt.y-RADIUS<=bricks[i].y+brickHeight)
                    {
                        try
                        {
//                            in =  new FileInputStream("F:\\JAVA\\Netbeans\\Netbeans\\Brick Breaker\\src\\keyboarddemo\\Sounds\\sound.wav");
                             in =  getClass().getResourceAsStream("/keyboarddemo/Sounds/sound.wav");

                        }
                        catch(Exception es)
                        {

                        }
                        try
                        {
                            as = new AudioStream(in);
                        }
                        catch(Exception ess)
                        {

                        }
                         AudioPlayer.player.start(as);
                        yincr=-yincr;
                        bricks[i].isBrocken=true;
                        score+=10;
                    }
            }
        }
        if(gameOver)
        {
            ballpt.y= getHeight()+20;
             t.stop(); paddleWatcher.stop();
             scoreWatcher.stop();
             repaint();
            AudioPlayer.player.start(as2);
            JOptionPane.showMessageDialog(this, "  YOU WON! YOUR SCORE WAS "+ score);
            
            allDone= true;
            cury=getHeight();
            credits.start();
//            repaint();
            
        }    
         
        repaint();
    }
    
    public GamePanel(int val)
    {
        
        xincr= val;
        yincr= val;
        try
        {
//             in =  new FileInputStream("F:\\JAVA\\Netbeans\\Netbeans\\Brick Breaker\\src\\keyboarddemo\\Sounds\\sound.wav");
            in =  getClass().getResourceAsStream("/keyboarddemo/Sounds/sound.wav");
        }
        catch(Exception es)
        {

        }
        try
        {
            as = new AudioStream(in);
        }
        catch(Exception ess)
        {

        }
        
        
        
         try
        {
//             in1 =  new FileInputStream("F:\\JAVA\\Netbeans\\Netbeans\\Brick Breaker\\src\\keyboarddemo\\Sounds\\start.wav");
            in1 =  getClass().getResourceAsStream("/keyboarddemo/Sounds/start.wav");
        }
        catch(Exception es)
        {

        }
        try
        {
            as1 = new AudioStream(in1);
        }
        catch(Exception ess)
        {

        }
        
        AudioPlayer.player.start(as1);
        
         try
        {
//             in2 =  new FileInputStream("F:\\JAVA\\Netbeans\\Netbeans\\Brick Breaker\\src\\keyboarddemo\\Sounds\\end.wav");
            in2 =  getClass().getResourceAsStream("/keyboarddemo/Sounds/end.wav");
        }
        catch(Exception es)
        {

        }
        
        try
        {
            as2 = new AudioStream(in2);
        }
        catch(Exception ess)
        {

        }
        
        try
        {
            in3 =  getClass().getResourceAsStream("/keyboarddemo/Sounds/hit.wav");
        }
        catch(Exception es)
        {

        }
        
        try
        {
            as3 = new AudioStream(in3);
        }
        catch(Exception ess)
        {

        }
        
        
        
//        bgimg = Toolkit.getDefaultToolkit().createImage("F:\\JAVA\\Netbeans\\Netbeans\\Brick Breaker\\src\\keyboarddemo\\Pics\\pic.jpg");
        bgimg = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/keyboarddemo/Pics/pic.jpg"));
         try 
        {                
//                           image = ImageIO.read(new File("F:\\JAVA\\Netbeans\\Netbeans\\Brick Breaker\\src\\keyboarddemo\\Pics\\pic1.jpg"));
           image = ImageIO.read(getClass().getResource("/keyboarddemo/Pics/pic1.jpg"));
        } 
        catch(IOException ex) 
        {
            // handle exception...
        }
        try 
        {                
//               paddleImage = ImageIO.read(new File("F:\\JAVA\\Netbeans\\Netbeans\\Brick Breaker\\src\\keyboarddemo\\Pics\\paddle.jpg"));
            paddleImage = ImageIO.read(getClass().getResource("/keyboarddemo/Pics/paddle.jpg"));
        } 
        catch(IOException ex) 
        {
            // handle exception...
        }
       
        credits= new Timer(100,new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
               repaint();
               cury-=20;
            }
        }
        );
        
        for(int i=0;i<21;i++)
            bricks[i]= new Brick();
        
        t = new Timer(50,this);
        
        scoreWatcher= new Timer(1000,new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
               score--;
               repaint();
            }
        }
        );
        
        paddleWatcher= new Timer(100,new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
               if(paddleMoved)
               {
                   paddleMoving=true;
                   paddleMoved=false;
               }
               else
                   paddleMoving= false;
            }
        }
        );
        addKeyListener(new KeyAdapter() 
        {

           @Override
           public void keyPressed(KeyEvent e) 
           {
               paddleMoved= false;
               int code= e.getKeyCode();
               switch(code)
               {
                   case KeyEvent.VK_LEFT: dirPaddle=2;
                                    paddlept.x-=15;
                                    if(paddlept.x<0)
                                    {
                                        paddlept.x=0;
                                        dirPaddle=3;
                                    }
                                    else
                                        paddleMoved=true;
                                 break;
                   case KeyEvent.VK_RIGHT: dirPaddle=3;
                                 paddlept.x+=15;
                                 if(paddlept.x+getWidth()/10>getWidth())
                                 {
                                     paddlept.x= 9*getWidth()/10;
                                     dirPaddle=2;
                                 }
                                 else
                                    paddleMoved= true;
                                break;
                                
               }
               if(paddleMoved)
                   repaint();
           }
           
       });
        setFocusable(true);
    }
    public boolean checkColiision()
    {
//        return ( (ballpt.x>=paddlept.x && ballpt.x<=paddlept.x+getWidth()/10) && ( ballpt.y+RADIUS>=paddlept.y) );
        
        if(ballpt.x-RADIUS>=paddlept.x && ballpt.x-RADIUS<=paddlept.x+getWidth()/10 || ballpt.x+RADIUS>=paddlept.x && ballpt.x+RADIUS<=paddlept.x+getWidth()/10 )
                    if(ballpt.y+RADIUS>=paddlept.y && ballpt.y+RADIUS<=paddlept.y+20)
                        return true;
        return false;
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(bgimg, 0, 0, null);
        if(allDone)
        {
            if(cury<=ymahan)
                credits.stop();
            Font f1= new Font("Garamond",Font.PLAIN,36);
            Font f2= new Font("Comic Sans MS",Font.PLAIN,60);
            Font f3= new Font("Verdana",Font.PLAIN,48);

            FontMetrics fm1,fm2,fm3;

            fm1= g.getFontMetrics(f1);
            fm2= g.getFontMetrics(f2);
            fm3= g.getFontMetrics(f3);

            String s1= "Developed";
            String s2= " By ";
            String s3= "Riddhi Dutta";

            int middley= getHeight()/2; //y coordinate of middle line.

            int widthMera= fm1.stringWidth(s1);
            int widthBharat= fm2.stringWidth(s2);
            int widthMahan= fm3.stringWidth(s3);

            int xleave = (getWidth() - (widthMera + widthBharat + widthMahan))/2;

            xmera= xleave;
            ymera= middley - fm1.getHeight()/2 + fm1.getAscent();

            xbharat= xleave+widthMera;
            ybharat= middley - fm2.getHeight()/2 + fm2.getAscent();

            xmahan= xbharat+widthBharat;
            ymahan= middley -fm3.getHeight()/2 + fm3.getAscent();

            g.setColor(Color.GREEN);
            g.setFont(f1);
            g.drawString(s1, xmera, cury);

            g.setColor(Color.BLUE);
            g.setFont(f2);
            g.drawString(s2, xbharat, cury);

            g.setFont(f3);
            g.setColor(Color.ORANGE);
            g.drawString(s3, xmahan, cury);

        }
        else
        {
            //setting up bricks
//            brickWidth= getWidth()/9;
//            brickHeight= getHeight()/9;

              brickWidth= 151;
              brickHeight=80;
            Font f2= new Font("Comic Sans MS",Font.PLAIN,30);
            FontMetrics fm1;
            fm1= g.getFontMetrics(f2);
            String s= "SCORE: " + score;
    //        String ts= "TIME: " + time;
            int width= fm1.stringWidth(s);
    //        int widths= fm1.stringWidth(ts);
             g.setColor(Color.GREEN);
            g.setFont(f2);
            g.drawString(s,getWidth()-width-10,bricks[6].y/2);
    //        g.drawString(ts, getWidth()-widths-10, bricks[6].y-10);

            int k=0;
            int left= (getWidth() - 7*brickWidth -6*BRICK_GAP)/2;
            int top=  (getHeight()-7*brickHeight- 6*BRICK_GAP)/2;
            
            for(int i=0;i<3;i++)
            {
                for(int j=0;j<7;j++)
                {
                    Brick b= bricks[k++];
                    b.x= left + j*brickWidth +BRICK_GAP*j;
                    b.y= top + i*brickHeight +BRICK_GAP*i;
                    if(!b.isBrocken)
                    {
//                         BufferedImage image=null;
                            g.drawImage(image, b.x,b.y, this);
                    }
                }
            }
            if(ballpt==null)
            {
                t.start();
                paddleWatcher.start();
                scoreWatcher.start();
                int wd= getWidth();
                int ht= getHeight();
                ballpt= new Point(wd/2,ht/2);
            }
            if(paddlept==null)
            {
                paddlept= new Point(9*getWidth()/20,getHeight()-30);
            }

            paddlept.y=getHeight()-30;
            g.setColor(Color.yellow.brighter().brighter());
            g.fillRect(paddlept.x, paddlept.y , getWidth()/10,20);

//            BufferedImage paddleImage=null;
//            g.drawImage(paddleImage, paddlept.x,paddlept.y, this);

            g.setColor(Color.red);
            g.fillOval(ballpt.x-RADIUS, ballpt.y-RADIUS,2*RADIUS ,2*RADIUS);
        }
    }
}
