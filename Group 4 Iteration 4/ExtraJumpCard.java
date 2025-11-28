public class ExtraJumpCard extends ActionCard {

    // Easy constructor, just needs a player number.
    public ExtraJumpCard(int playerNumber)
    {
        super(playerNumber);
    }

    @Override
    protected void useCardImpl(GameLogic game, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge)
    {
        // The second target (targetB) is where the card user wants to move the frog
        Lilipad targetLilipad = targetB != null ? targetB : targetA;
        
        if (targetLilipad == null || currentPad == null || !currentPad.isOccupied())
        {
            return;
        }
        
        Frog frog = currentPad.getFrogs().get(0);
    
        if (frog != null && game.moveFrog(frog, targetLilipad))
        {
            markAsUsed(); // Only mark as used if the move was successful
        }
    }
}