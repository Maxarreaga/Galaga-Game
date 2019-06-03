public class HS implements Comparable<HS>
{
  int score;
  String name;
  public HS(String namee, int scoree)
  {
   name = namee;
   score = scoree;
    
  }
  public int getscore()
  {
   return score; 
  }
  public void setscore(int sc)
  {
    score = sc;
  }
  public void setname(String namee)
  { 
   name = namee;
  }
  public String getname()
  {
   return name; 
  }
  
  public int compareTo(HS highscore)
  {
    int comparison;
    comparison = new Integer(score).compareTo(new Integer(highscore.getscore()));
    
   return comparison;
  }
  
  
}