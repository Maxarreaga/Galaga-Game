import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.text.NumberFormat;
import javax.swing.filechooser.*;
//imports required libraries

public class galagun extends JFrame implements ActionListener, KeyListener//extends classes used to display and control program
{
  Timer time = new Timer(45, this);//swing timer for controlling pulse events (set to 25 times per second)
  JPanel bigpanel = new JPanel();//panel which is switched from using cardlasyout (the panel which all other panels are stored on
  JPanel mainmenu = new JPanel();//mainmenu panel that displays on boot
  JPanel gamepanel = new JPanel();//gamepanel that displays the main gameplay
  JPanel highscoremenu = new JPanel();//panel that the highscores are displayed on
  JTextArea highscorelist = new JTextArea("");//text area where highscores are displayed to on the highscore panel
  JButton playgame = new JButton("Play game");
  JButton highscorebutton = new JButton("High scores");
  JButton quitgame = new JButton("Quit Game");
  //Main menu buttons that take you to the other panels or quits game
  JButton clearscores = new JButton("Clear Highscores");
  JButton backtomenu  = new JButton("Back to menu");
  //buttons to clear scores and go back to menu on high score panel ^
  Font font = new Font("Bauhaus 93", Font.BOLD, 40);//font used to display HUD on gamepanel
  
  File f;//file to store highscore file
  private ArrayList <Highscorer> champs = new ArrayList <Highscorer>();//array list containing the highscore objects for display
  public ArrayList <Enemy> enemylist = new ArrayList <Enemy>();//arraylist containing the alien enemy objects that contain their coordinates for comparison
  public LinkedList <Bullet> bullets = new LinkedList <Bullet>();//linkedlist containing the bullet objects that contain their coordinates for comparison and images
  String data[];// 1D array to split delimited text file
  String line;//String to contain the input from text file
  int currentmenu = 0;//int that represents the different menus (1 for main menu, 2 for game panel, 3 for highscores)

  int drawprompt = 0;//int that changes when something needs to be drawn by the paint function
  int enemyprompt = 0;//int that changes when an enemy needs to be draw by the pain function
  int windowsizex = 1200;//window size for gameplay panel (x component)
  int windowsizey = 800;// window size for gameplay panel (y component)
  int shipx;// int containing the player controlled ship x coordinate
  int shipy = windowsizey - 80;//the static y coordinates for the player controlled ship
  int shipxvel;//ship x acceleration (speed side to side)
  int shipyvel;// ship y acceleration (speed up and down, unused at the moment)
  int levelcount = 1;//counter that keeps track of current level
  int enemypulse = 0;//counter that keeps track of how long the current level has lasted and when to draw new aliens
  int bulletpulse = 0;//counter that limits how often the player can shoot
  int xaliensize = 65;//dimensions(x)of the image of the alien for use on their hitboxes(comparison
  int yaliensize = 50;//dimensions*(y) of the image of the alien for use on their hitboxes(comparison)
  int enemyspeed = 20;//determines how fast the enemies move
  boolean movingleft = false;//boolean for which direction the enemies are moving
  boolean movingdown = false;//boolean for if the enemies will move down this tick
  boolean lose = false;//boolean to determine if the player has lsot to prevent infinite loops
  int hasmoveddown = 1;//int that determines if the aliens have moved down this turn to prevent constant moving down
  int levelprompt = 1;//int prompt to draw new level if needed
  int score = 0;// keeps track of players score
  String champdisplay = "";//high score string displayed on the high score screen
  
  Dimension d = new Dimension(1200,800);//dimensions of the gamepanel
  Dimension dship = new Dimension(80,80);//dimensions of the player controlled ship
  BufferedImage galagaship,bullet,alien;//buffered images of the sprites used in the game
  Bullet bul;//bullet object initialized
  
  CardLayout cl = new CardLayout();//initializing cardlayout
  
  public galagun()
  {
    time.start();//starts swing timer to start actionevent listening on game
    try
    {
    galagaship = ImageIO.read(new File("Galaga ship.png"));
    bullet = ImageIO.read(new File("Galaga bullet.png"));
    alien = ImageIO.read(new File("Galaga Alien.png"));
    //imports the necessary images and saves them as files
    }
    catch(IOException e)
    {
       JOptionPane.showMessageDialog(null,"ERROR: FILE INACCESSIBLE OR NOT FOUND","Galaga", JOptionPane.ERROR_MESSAGE);//tells user if the files arent found
    }
    bigpanel.setLayout(cl);
    bigpanel.add(mainmenu, "1");
    bigpanel.add(gamepanel, "2");
    bigpanel.add(highscoremenu, "3");
    //sets cardlayout and defines the numbers for the panels stored on the bigpanel ^^^^
    shipxvel = 0;//sets ship veloctiy to zero
    shipx = windowsizex/2 - 40;//sets ship coordinates in middle of screen
    setPreferredSize(d);//sets window preferredsize
    addKeyListener(this);//adds keylisetener
    setFocusTraversalKeysEnabled(false); //disables traversaleys as they arent neeeded
    loadmenu();//loads main menu
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//jframe closes on close
    setSize(600,400);//sets size of menu screen
    setTitle("Galaga");//sets tite in corner of frame
    setResizable(true);
    setLocationRelativeTo (null);
    //jframe settings
    setContentPane(bigpanel);
    //sets content panel to the cardlayout panel
    openscores();//prompts the user to select a file to read from adn save scores to
  }
  public void loadmenu()//loads the main menu
  {
    wipe();//clears any objects left over from other panels in the case of swtiching
    cl.show(bigpanel,"1");//changes panel eing displayed to the main menu
    ImageIcon galagalogo = new ImageIcon("Galaga Logo.png");//initialized the galaga logo on main menu
    JLabel galagalogolabel = new JLabel();
    galagalogolabel.setIcon(galagalogo);
    //applies the galaga logo image to the main menu on a jlabel
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = 0;
    gc.gridy = 0;
    gc.gridwidth = 3;
    gc.gridheight = 1;
    gc.weightx = 50;
    gc.weighty = 50;
    gc.insets = new Insets(5, 5, 5, 5);
    gc.anchor = GridBagConstraints.NORTHWEST;
    gc.fill = GridBagConstraints.BOTH;
    mainmenu.setLayout(new GridBagLayout());
    //gridbag settings
    playgame.addActionListener(this);
    highscorebutton.addActionListener(this);
    quitgame.addActionListener(this);
    mainmenu.add(galagalogolabel,gc);
    gc.gridwidth = 1;
    gc.gridx = 1;
    gc.gridy = 2;
    mainmenu.add(playgame,gc);
    gc.gridy = 3;
    mainmenu.add(highscorebutton,gc);
    gc.gridy = 4;
    mainmenu.add(quitgame,gc);
    mainmenu.setBackground(Color.BLACK);
    //configuring physical layout of main menu with grid back and changing background
    currentmenu = 0;//int so that nothing is printed to the wrong screen (see paint method)
    setTitle("Galaga");
    setSize(600,400);
    setVisible(true);
    setResizable(true);
    setLocationRelativeTo (null);
    //jframe settings for main menu
    repaint();
    //update display
  }
  public void loadgame()//loads the main gamepanel of the game and its corresponding settings
  {
    setVisible(true);
    setSize(windowsizex,windowsizey);
    setResizable(false);
    setLocationRelativeTo(null);
    mainmenu.setVisible(false);
    gamepanel.setVisible(true);
    // new static jframe settings
    wipe();//wipes away extra drawings
    cl.show(bigpanel,"2");//changes panel being displayed 
    currentmenu = 1;//sets check that the correct menu gets the correct paint functions (see paint method)
    
    gamepanel.setLayout(new BorderLayout());
    gamepanel.setPreferredSize(d);
    //loads gamepanel panel settings
    ImageIcon backimage = new ImageIcon("Galaga background.png"); 
    JLabel background = new JLabel();
    background.setOpaque(false);
    background.setIcon(backimage);
    //sets stars as background to the GUI
    gamepanel.add(background);
    gamepanel.addKeyListener(this);
    gamepanel.setPreferredSize(d);
    gamepanel.setFocusable(true);
    gamepanel.requestFocus();
    //gets focus for use in keylisteners and adds background + keylisteners
    setLocationRelativeTo(null);//puts the window in the middle of the screen
    setSize(windowsizex,windowsizey);//sets size
    gamepanel.getPreferredSize();
    revalidate();
    gamepanel.repaint();
    //updates display
    newlevel();
    //spawns a new level (see new level method)
  }
  
  @Override//ensures the right methods/variables are overwritten
  public void paint(Graphics g)//draws images and moving objects for main game
  {
    super.paint(g);//overwrites method
    if(currentmenu == 1)
    {
      g.drawImage(galagaship,shipx,shipy,80,80,this);//constantly draws ship according to user controls
      g.setFont(font);
      g.setColor(Color.YELLOW);
     g.drawString("SCORE: " + score, windowsizex/2 - 125,75);
     //sets text settings and score display
      if(drawprompt == 1)//when bullets are shot
      {
        try{
        g.drawImage(bullet,bullets.get(0).xbullet,bullets.get(0).ybullet,15,40,this);//draw a new bullet originating from the ship 
        }
        catch(IndexOutOfBoundsException e)
        {
          System.out.println("Bullet Fired Early");//if pressed to quick the comparisons may get mixed with the constant updating
        }
        drawprompt = 0;//resets prompt
      }
      else if(drawprompt == 0 && bullets.peek() != null)//if there are bullets on the screen
      {
        for(int count = 0;count < bullets.size();count++)
        {
        g.drawImage(bullet,bullets.get(count).xbullet,bullets.get(count).ybullet,15,40,this);//redraw bullets as they accelerate
        }
        //redraw bullets
      }
      
      if(levelprompt == 1)//if a new level is set to be drawn
      {
        g.drawString("Level " + levelcount,windowsizex/2 - 100,windowsizey/2);//print what is the current level
      }
      
      if(enemyprompt == 1)//prompted when a new level starts (see newlevel method then draw new enmies
      {
       enemyprompt = 0;//reset prompt
       for(int i = 0;i<enemylist.size();i++)
       {
        g.drawImage(alien,enemylist.get(i).xpos,enemylist.get(i).ypos,xaliensize,yaliensize,this);//take the default coordinates of the enemies and draw them there
       }
      }
      else if(enemyprompt == 0 && enemylist.size() != 0)//if enemies ae being redrawn
      {
       for(int a = 0;a < enemylist.size();a++)
       {
         g.drawImage(alien,enemylist.get(a).xpos,enemylist.get(a).ypos,xaliensize,yaliensize,this);//draw enemies at the new coordinates
       }
      }
    }
    else if(currentmenu != 1)//else do nothing
    {
      
    }
    validate();
  }
  //////////////////////////////////////////////////////////////////////////END OF GUI SECTION
  public void openscores()//open scores file
  {
    JOptionPane.showMessageDialog(null,"Welcome To Galaga\nPlease select a Save File\n(Text document to save and read highscores)","Galaga", JOptionPane.PLAIN_MESSAGE);//tells user that file wasnt found 
   JFileChooser fc = new JFileChooser(); //Declares JFie chooser
   FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files","txt");//Changes filter type to detect text files only
   fc.setFileFilter(filter);//sets file fitler
   int choice = fc.showOpenDialog(this);//returns if user picked a file
   
   if(choice == JFileChooser.APPROVE_OPTION);//if a file was chosen
   {
     f = fc.getSelectedFile();//save the file
     try
     {
       BufferedReader in = new BufferedReader(new FileReader(f));
       line = in.readLine();
       //try to read first line of document
       while(line != null && line != "")//if document isnt empty and there are still lines to read
       {
         data = line.split(", ");
         Highscorer hs = new Highscorer(data[0], Integer.parseInt(data[1]));
         hs.setname(data[0]);
         hs.sethighscore(Integer.parseInt(data[1]));
         //take info from comma delimited text file and store them in an array
         champs.add(hs);
         line = in.readLine(); //read next line
       }
       in.close();//prevents leak
     }
     catch(IOException e)
     {
      JOptionPane.showMessageDialog(null,"ERROR: FILE INACCESSIBLE OR NOT FOUND","Galaga", JOptionPane.ERROR_MESSAGE);//tells user that file wasnt found 
     }
   }
   
  }
  public void recordScore(int sc,String playername)//method to record the winner/loser's socre in the array/textfile
  {
    Highscorer h = new Highscorer(playername,sc);
    h.sethighscore(sc);
    h.setname(playername);
    champs.add(h);
    //adds score to array w/ name
    try
    {
      BufferedWriter out = new BufferedWriter(new FileWriter(f,false));//initializes the text writer
      {
       for(int i = 0; i< champs.size();i++)//rewrites all highscores
       {
         out.write(champs.get(i).getname() + ", " + champs.get(i).gethighscore());//writes score adn name delimited by a comma
         out.newLine();//writes a new line
       }
       out.close();//preventss memory leak
      }
    }
      catch(IOException e)
      {
       JOptionPane.showMessageDialog(null,"ERROR: FILE INACCESSIBLE OR NOT FOUND","Galaga", JOptionPane.ERROR_MESSAGE);//if error tells user the file was inaccessible
      }
    
  }
  public void outputScores()//outputs scores to the text field in the highscore menu
  {
   highscorelist.setText("");
   champdisplay = "";
   Collections.sort(champs, Collections.reverseOrder());
   for(int i = 0; i < champs.size();i++)
   {
    champdisplay = champdisplay + (Integer.toString(i + 1) + "\t\t" + champs.get(i).getname() + "\t\t" + champs.get(i).gethighscore() + "\n");//adds all high scores to the saem string to print out
   }
   highscorelist.setText(champdisplay);//sets string as text for that text area
  }
    
    
  public void loadhighscores()//loads high score menu
  {
    wipe();//clears any extra drawings/objects
    currentmenu = 2;//sets the change in menu
    setSize(600,800);
    setLocationRelativeTo(null);
    //jframe settings
     GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = 0;
    gc.gridy = 0;
    gc.gridwidth = 1;
    gc.gridheight = 1;
    gc.weightx = 50;
    gc.weighty = 10;
    gc.insets = new Insets(5, 5, 5, 5);
    gc.anchor = GridBagConstraints.NORTHWEST;
    gc.fill = GridBagConstraints.NONE;
    highscoremenu.setLayout(new GridBagLayout());
    //gridbag settings
    backtomenu.addActionListener(this);
    JLabel highscoretitle = new JLabel("HIGHSCORES");
    highscoretitle.setForeground(Color.GREEN); 
    highscoretitle.setFont(font);
    //GUI title label settings
    JLabel emptyholder = new JLabel("");
    //empty holder to center all other aspects of the fields
 
   outputScores();//outputs scores to the screen
   highscorelist.setText(champdisplay);
   highscorelist.setBackground (Color.BLACK);
   highscorelist.setForeground (Color.CYAN);
   //sets textfield settings
   JPanel highscorearea = new JPanel();
   JLabel positionlabel = new JLabel("POS");
   JLabel namelabel = new JLabel("NAME");   
   JLabel highscorelabel = new JLabel("HIGHSCORE");
   //declaring local jlabels for headings and jpanel to store text area
   highscoremenu.add(backtomenu,gc);
   gc.gridx = 1;
   gc.gridy = 0;
   highscoremenu.add(highscoretitle,gc);
   gc.gridx = 2;
   highscoremenu.add(emptyholder,gc);
   gc.gridx = 0;
   gc.gridy = 1;
   gc.weighty = 10;
   gc.gridwidth = 1;
   gc.gridheight = 1;
   gc.fill = GridBagConstraints.BOTH;
   //adding components to the main panel with gridbag
   positionlabel.setPreferredSize(new Dimension(150,50));
   positionlabel.setForeground(Color.GREEN);
   //custom jlable settings to contrast background
   highscoremenu.add(positionlabel,gc);
   //adding jlabel to main panel
   gc.gridx = 1;
   namelabel.setPreferredSize(new Dimension(150,50));
   namelabel.setForeground(Color.GREEN);
   highscoremenu.add(namelabel,gc);
   gc.gridx = 2;
   highscorelabel.setPreferredSize(new Dimension(150,50));
   highscorelabel.setForeground(Color.GREEN);
   highscoremenu.add(highscorelabel,gc);
   //adding all jlabel headings to the main panel
   gc.gridx = 0;
   gc.gridy = 2;
   highscorelist.setPreferredSize(new Dimension(500,400));
   highscorelist.setText(champdisplay);
   highscorelist.setFont(new Font("Times New Roman", Font.PLAIN, 15));
   //settings for the main highscore text area
   gc.gridwidth = 3;
   gc.gridheight = 3;
   gc.weighty = 40;
   highscoremenu.add(highscorelist,gc);
   gc.weighty = 10;
   gc.gridy = 4;
   gc.gridx = 0;
   gc.gridwidth = 3;
   gc.gridheight = 1;
   gc.weighty = 10;
   gc.fill = GridBagConstraints.BOTH;
   //configuring layout
   highscoremenu.add(clearscores,gc);
   highscoremenu.setBackground(Color.BLACK);//setting background
   validate();
   cl.show(bigpanel,"3");//chaning panel to high score panel
   repaint();
  }
  public void wipe()//clears all panels from left over objects to ensure no duplication
  {
   gamepanel.removeAll();
   mainmenu.removeAll();
   highscoremenu.removeAll();
   gamepanel.revalidate();
   mainmenu.revalidate();
   highscoremenu.revalidate();
  }
  
  public void quitgame()//if quit button is clicked the user will be prompted with a confirmation message
  {
     int exit = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?","Galaga", JOptionPane.YES_NO_OPTION);

   if (exit == JOptionPane.YES_OPTION)//if user says yes
   {
     System.exit(0);//closes the program 
   }
     
  }
  public void clearhighscores()
  {
    //clears highscores from text file
  }
  public void shoot()//shoots the bullets from the ship as displayed in the paint method
  {
    if(bulletpulse > 3)//user can only shoot every 180 milliseconds to ensure no rapidfiring
    {
    Bullet bul = new Bullet(shipx + 33, shipy);//declares new bullet objects
    bullets.add(bul);//adds new bullet to the bullet array
    drawprompt = 1;//starts a new draw prompt for the paint method
    }
    for(int i = 0;i < bullets.size();i++)//checks all bullets
    {
     if(bullets.get(i).ybullet <= 0)//if any bullet goes off the screen
     {
       bullets.remove(i);//remove it from the array
     }
    }
    
  }
  public void checkkill()//checks to see if any bullets have collided with any enemies
  {
    for(int i = 0;i<enemylist.size();i++)//checks all aliens
    {
      for(int q = 0;q<bullets.size();q++)//checks all bullets
      {
        try
        {
       if(bullets.get(q).xbullet < enemylist.get(i).xpos + xaliensize && bullets.get(q).xbullet > enemylist.get(i).xpos && bullets.get(q).ybullet < enemylist.get(i).ypos + yaliensize && bullets.get(q).ybullet > enemylist.get(i).ypos)
         //if any bullets occupy the same space as any aliens
       {
       bullets.remove(q);
       enemylist.remove(i);
       //delete both the alien and bullet from the array so that they are out of play
       score = score + 100;//increase the user's score by 100
       break;
       }
        }
        catch(IndexOutOfBoundsException e)
        {
          System.out.println("Comparison Failure");//sometimes while comparing and removing there will be an index out of bounds exception as the bullet it was going to compare was deleted
        }
      }
    }
  }
  
  public void newlevel()//prompts a new level to the painter
  {
    enemyprompt = 1;
    levelprompt = 1;
    //issues new paint prompts
    enemyspeed--;//increases enemy speed
    enemylist.clear();
    bullets.clear();
    //clears both aliens and bullets before adding more
    if(levelcount < 4)//if it is level 1-3
    {
      for(int i = 1;i < (levelcount*12) + 1;i++)
      {
      if(i <= 12)
      {
      Enemy en = new Enemy(75*i + 100,100);
      enemylist.add(en);
      }
      else if(i > 12 && i <= 24)
      {
      Enemy en = new Enemy(75*(i-12) + 100,175);
      enemylist.add(en);
      }
      else if(i > 24 && i <= 37)
      {
      Enemy en = new Enemy(75*(i-24) + 100,250);
      enemylist.add(en);
      }
      // aliens are added in rows of 12 a uniform distance away from each other up to three rows with new alien objects being declared
      ////////////////////////////////////////////////////////////////                         
      }
    }
    else if(levelcount >= 4 && levelcount < 11)//if it is level 4 or higher 
    {
      for(int i = 1;i < 37;i++)
      {
      if(i <= 12)
      {
      Enemy en = new Enemy(75*i + 100,100);
      enemylist.add(en);
      }
      else if(i > 12 && i <= 24)
      {
      Enemy en = new Enemy(75*(i-12) + 100,175);
      enemylist.add(en);
      }
      else if(i > 24 && i < 37)
      {
      Enemy en = new Enemy(75*(i-24) + 100,250);
      enemylist.add(en);
      }
        
      }
      enemyspeed--;//increase enemyspeed too
      //no longer start adding enemies every round but instead increase speed of enemies every round
    }
  else if(levelcount >= 11)//if user completes level 10
  {
    endgame();//end the game
  }
  score = score + levelcount*(1000 - enemypulse);//if the user takes less than 100 seconds to finish the level award them points, if not penalize them some points
  enemypulse = 0;//reset enemy pulse
  }
  public void checklose()//check if payer has lost by allowing aliens to reach the bottom
  {
    for(int i = 0;i<enemylist.size();i++)//for every alien
    {
     if(enemylist.get(i).ypos > 675 && lose != true)//check if they have reach the bottom if so:
     {
       lose = true;
       endgame();
       break;
       //stop comparing aliens, set the lose flag to true and end the game
     }
    } 
  }
  public void endgame()//ending the game method
  {
    int scorerecorded = 0;//resets score recorded check
    //////Opens highscores screen
    if(lose == false)
    {
      JOptionPane.showMessageDialog(null,"You Won!","Galaga",JOptionPane.PLAIN_MESSAGE);//tells user theyve won if they have 
    }
    else if(lose == true)
    {
      JOptionPane.showMessageDialog(null,"You Lost, Please Try again!","Galaga",JOptionPane.PLAIN_MESSAGE); //tells users theyve lost if they have  
    }
    Collections.sort(champs, Collections.reverseOrder());//sorts high scores from highest to lowest
    if(champs.size() < 10 && scorerecorded == 0)//if there are open spots on the leaderboard
    {
      scorerecorded = 1;//sets check to true that score has been recorded to prevent infinite loop
      String playname = JOptionPane.showInputDialog(null, "Please input your name for the highscore list!", "Galaga", JOptionPane.QUESTION_MESSAGE);//asks user for their name
     recordScore(score,playname);//records score using recordscore method from above
     loadmenu();//returns player to the menu
    }
    else if(score > champs.get(9).gethighscore() && scorerecorded == 0)//if the 10th place score is lower than the user's score and there isn't space on the leaderboard
      {
      scorerecorded = 1;//set flag to true that score hs been recorded
      String playname = JOptionPane.showInputDialog(null, "Please input your name for the highscore list!", "Galaga", JOptionPane.QUESTION_MESSAGE);//ass user for their name
      recordScore(score,playname);//records user's name using recordscore
      loadmenu();//returns user to the menu
      }
    else//if user didnt crack the top 10
    {
      loadmenu();//return user to the menu
    }

  }
  public void actionPerformed(ActionEvent e)//Event Listener
  {
    if(e.getSource() == playgame)//if menu button play game is pressed the gamepanel is loaded 
    {
     loadgame();
    }
    if(e.getSource() == highscorebutton)//if menu button high score is pressed the leaderboard is loaded
    {
     loadhighscores();
    }
    if(e.getSource() == quitgame)//if quit game menu button is pressed run the exit game confirmation dialogue
    {
     quitgame();
    }
    if(e.getSource() == backtomenu)//if back to menu button on high score menu return user to the menu
    {
     loadmenu();
    }
    if(e.getSource() == clearscores)//clears the leaderboard and asks user for confirmation
    {
     int clearchoice = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear highscores?\n(This can't be reversed)", "Clear Scores?", JOptionPane.YES_NO_OPTION);
     if(clearchoice == JOptionPane.YES_OPTION)
     {
     clearhighscores();
     }
    }
    if(currentmenu == 1)//if the current menu is the game panel menu
    {
      if(shipx >= 0 && shipx <= 1250)//if the ship is within bounds 
      {
      shipx = shipx + shipxvel;//change the position of the ship by the speed of the ship every tick
      }
      else if (shipx < 0)//ensures the ship doesn't go off the screen to the left
      {
       shipx = 0; 
      }
      else if (shipx > 1200)//ensures the ship doesn't go off the screen to the right
      {
       shipx = 1200; 
      }
      if(e.getSource() == time)//every tick run the following
      {
        checkkill();//check for enemy kills
        if(bullets.peek() != null)
        {
          for(int i = 0;i< bullets.size();i++)
          {
        bullets.get(i).moveup();//move all bullets on the screen up as defined up the movables class
          }
        }
        enemypulse++;
        bulletpulse++;
        //increase the tick timers by one
        if(enemylist.size() != 0 && enemypulse%enemyspeed == 0)//if the pulse is divisible by the speed then move evemies (the smaller the more frequent the moves)
        {
          levelprompt = 0;//resets level prompt
          for(int i = 0;i < enemylist.size();i++)//checks all alien coordinates
          {
           if(enemylist.get(i).xpos > 1100)//if aliens all the way to the right
           {
             movingleft = true;              
             movingdown = true;
             hasmoveddown++;
             break;
             //move aliens down and invert direction
           }
           else if(enemylist.get(i).xpos < 30)//if aliens all the way to left 
           {
            movingleft = false;
            movingdown = true;
            hasmoveddown++;
            break;
            //move aliens down and change direction
           }
          }
          for(int q = 0;q < enemylist.size();q++)//compares all aliens
          {
            if(movingdown == true && hasmoveddown == 2)//if the flag is given, move the aliens down
            {
              enemylist.get(q).movedown();
              checklose();
            }
            else if(movingleft == false)//if this is true move aliens right
            {
              enemylist.get(q).moveright();
            }
            else if(movingleft == true)//if this is true move aliens left
            {         
              enemylist.get(q).moveleft();
            }
          }
          movingdown = false;//do not move aliens down
          if(hasmoveddown == 2)
          {
            hasmoveddown = 0;
          }
        }
        else if(enemylist.size() == 0 && levelcount <= 10)//if all aliens removed and level is below 10 start a new level
        {
          newlevel();
          levelcount++;
        }
        else if(enemylist.size() == 0 && levelcount > 10)
        {
          endgame();//ends game, gives win screen
        }
       
      }
    }
    repaint();//update graphics
  }
  public void keyPressed(KeyEvent e)
  {
    int c = e.getKeyCode();
    if(c == KeyEvent.VK_LEFT && c == KeyEvent.VK_RIGHT)//if both and right are pressed nthing happens they cancel out
    {
     shipxvel = 0;
    }
    else if(c == KeyEvent.VK_LEFT)//if left arrow key is pressed move left 10 pixels every tick
    {
      shipxvel = -10;
    }
    else if(c == KeyEvent.VK_RIGHT)//if right arrow key is pressed move right 10 pixels every tick
    { 
      shipxvel = 10;
    }
    if(c == KeyEvent.VK_SPACE)//if space is pushed, shoot a bullet and restart the bullettimer
    {
      shoot();
      bulletpulse = 0;
     //activate shooty 
     //shooty is instakill  
     //shooty is gun
    }
  }
   public void keyTyped(KeyEvent e)
  {
  }
  public void keyReleased(KeyEvent e)//when keys are released set speed to zero so ship can stand still
  {
   int c = e.getKeyCode();
   if(c == KeyEvent.VK_LEFT || c == KeyEvent.VK_RIGHT)
   {
   shipxvel = 0;
   }
  }
  public static void main(String [] args)
  {
   new galagun(); //starts new instance of program
  }
  
}