public class Highscorer implements Comparable <Highscorer>
{
  public int score;
  public String name;
  public static int sortValue;
  
  public Highscorer(String name, int points)
  { 
    sortValue = 0;
  }
  public void sethighscore(int points)
  {
   score = points;
  }
  public int gethighscore()
  {
    return score;
  }
  public void setname(String newname)
  {
   name = newname; 
  }
  public String getname()
  {
   return name;
  }
  
   public int compareTo (Highscorer hser)
 {
  int comparison;
   comparison = new Integer(score).compareTo(new Integer(hser.gethighscore()));
   return comparison;
  }
 }
  ///REMEMBER TO SET SORT VALUE IN SORT FUNCTION IN OTHER PROGRAM!!!!1!
