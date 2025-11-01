import java.awt.Color;
import java.util.ArrayList;

public class Frog
{
    Color color;
    boolean canMove;
    int playerNumber;
    int position;
    ArrayList<Lilipad> goal = new ArrayList<>(3); //Frog can only have a max of 3 goals
    
    public Frog(boolean canMove, int playerNumber, ArrayList<Lilipad> goal, int position)
    {
        this.canMove = canMove;
        this.playerNumber = playerNumber;
        this.goal = goal;
        this.position = position;
        
        if(playerNumber == 1)
        {
            this.color = new Color(255, 0, 0);
        }
        
        else if(playerNumber == 2)
        {
            this.color = new Color(0, 0, 255);
        }
        
        else if(playerNumber == 3)
        {
            this.color = new Color(255, 255, 0);
        }
        
        else
        {
            this.color = new Color(255, 0, 255);
        }
    }
    
    public void setPlayer(int playerNumber)
    {
        this.playerNumber = playerNumber;
    }
    
    public void setGoal(ArrayList<Lilipad> goals)
    {
        this.goal = goals;
    }
    
    public void setCanMove(boolean move)
    {
        this.canMove = move;
    }
    
    public void moveFrog(Lilipad pad)
    {
        if(pad.getIsOccupied() == null)
        {
            if(canMove == true)
            {
                position = pad.getIndexNumber(); // Moves the frog to the current index number
            }
        }
    }
    
    public int getPosition()
    {
        return this.position;
    }
}