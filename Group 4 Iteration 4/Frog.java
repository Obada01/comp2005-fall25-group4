import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Frog
{
    private static final int MAX_GOALS = 3;

    private Color color;
    private boolean canMove;
    private int playerNumber;
    private int position;
    private Lilipad currentLilipad;
    private List<Lilipad> goals;
    private boolean selected;

    public Frog(boolean canMove, int playerNumber, List<Lilipad> goals, int position)
    {
        this.canMove = canMove;
        this.playerNumber = playerNumber;
        this.goals = new ArrayList<>(goals.subList(0, Math.min(goals.size(), MAX_GOALS)));
        this.position = position;
        this.color = getPlayerColor(playerNumber);
        this.currentLilipad = null;
        this.selected = false;
    }

    private static Color getPlayerColor(int playerNumber)
    {
        return switch (playerNumber)
        {
            case 1 -> new Color(255, 0, 0);
            case 2 -> new Color(0, 0, 255);
            case 3 -> new Color(255, 255, 0);
            case 4 -> new Color(255, 0, 255);
            default -> new Color(0, 0, 0);
        };
    }

    public void moveTo(Lilipad target)
    {
        if (currentLilipad != null)
        {
            currentLilipad.removeFrog(this);
        }

        this.position = target.getIndexNumber();
        this.currentLilipad = target;

        target.addFrogDirectly(this);
    }

    public Color getColor()
    {
        return color;
    }

    public int getPlayerNumber()
    {
        return playerNumber;
    }

    public boolean canMove()
    {
        return canMove;
    }

    public void setCanMove(boolean canMove)
    {
        this.canMove = canMove;
    }

    public int getPosition()
    {
        return position;
    }

    public Lilipad getCurrentLilipad()
    {
        return currentLilipad;
    }
    
    public List<Lilipad> getGoals()
    {
        return Collections.unmodifiableList(goals);
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
        if (currentLilipad != null)
        {
            currentLilipad.repaint();
        }
    }
    
    public boolean isSelected()
    {
        return selected;
    }
}