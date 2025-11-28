import java.util.ArrayList;

public class ParachuteCard extends ActionCard {

    public ParachuteCard(int playerNumber)
    {
        super(playerNumber);
    }

    @Override
    protected void useCardImpl(GameLogic game, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge) {
        Lilipad targetLilipad = targetA;
        
        if (targetLilipad == null)
        {
            return;
        }
        
        if (!isAdjacent(currentPad, targetLilipad))
        {
            return;
        }
        
        if (targetLilipad.getIsStarterPad() && targetLilipad.getOccupants().size() >= 3)
        {
            return;
        }
        
        ArrayList<Frog> frogs = currentPad.getOccupants();
        
        if (frogs.isEmpty())
        {
            return;
        }
        
        Frog frog = frogs.get(0);

        boolean moved = game.moveFrogParachute(frog, targetLilipad);

        if (!moved)
        {
            return;
        }

        markAsUsed();
        game.getTurnLogic().markParachuteUsed();
    }

    private boolean isAdjacent(Lilipad pad1, Lilipad pad2)
    {
        if (pad1 == null || pad2 == null)
        {
            return false;
        }

        int gridSize = 5;

        int idx1 = pad1.getIndexNumber();
        int idx2 = pad2.getIndexNumber();
        
        int row1 = idx1 / gridSize;
        int col1 = idx1 % gridSize;
        int row2 = idx2 / gridSize;
        int col2 = idx2 % gridSize;
        
        boolean horizontallyAdjacent = (Math.abs(col1 - col2) == 1) && (row1 == row2);
        boolean verticallyAdjacent = (Math.abs(row1 - row2) == 1) && (col1 == col2);
        
        return horizontallyAdjacent || verticallyAdjacent;
    }
}