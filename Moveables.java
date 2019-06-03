import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.JOptionPane;
public abstract class Moveables
{
    int health;
    int xpos;
    int ypos;
    public void killenemy()
    {
     //if the enemy gets  shot it dies 
    }
    public void setHealth(int hp)
    {
     hp = health;
    }
    public void moveright()
    {
      xpos = xpos + 40;
      //move enemy right a set amount of pixels
    }
    public void moveleft()
    {
      xpos = xpos - 40;
      //move enemy left a set amount of pixels
    }
    public void movedown()
    {
      ypos = ypos + 75;
      //moves ship down
    }
}
    class Enemy extends Moveables
    {
      BufferedImage enemyicon;
      public Enemy(int x, int y)
      {
        try
        {
         enemyicon = ImageIO.read(new File("Galaga Alien.png"));
        }
        catch(IOException e)
        {
          JOptionPane.showMessageDialog(null,"ERROR: FILE INACCESSIBLE OR NOT FOUND","Galaga", JOptionPane.ERROR_MESSAGE);
        }
        xpos = x;
        ypos = y;
      }
      
    }
    class Bullet extends Moveables
    {
      int xbullet;
      int ybullet;
      int ybulletaccel = 20;
      BufferedImage bulleticon;
      public Bullet(int x,int y)
      {
        try
        {
         bulleticon = ImageIO.read(new File("Galaga bullet.png"));
        }
        catch(IOException e)
        {
         JOptionPane.showMessageDialog(null,"ERROR: FILE INACCESSIBLE OR NOT FOUND","Galaga", JOptionPane.ERROR_MESSAGE);
        }
        xbullet = x;
        ybullet = y;
      }
        public void moveup()
      {
       ybullet = ybullet - ybulletaccel; 
      }
    }
      
 

  /*void AllowPlaneToLand(FlyingMachine fm)
{
    fm.LandPlane(); blueprint for passing which enemy with die into the parameters
}
*/
  
  
  
