public abstract class ActionCard
{
    protected int playerNumber;
    protected boolean used;

    public ActionCard(int playerNumber)
    {
        this.playerNumber = playerNumber;
        this.used = false;
    }

    // Method to be implemented by subclasses
    public void useCard(GameLogic game, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge)
    {
        if (used)
        {
            String message = "You cannot use a card more than once per game.";
            System.out.println(message);
            
            // Show in UI using CardHandler
            if (game != null) {
                javax.swing.SwingUtilities.invokeLater(() -> 
                    CardHandler.showMessage(
                        null, 
                        message, 
                        "Card Already Used", 
                        javax.swing.JOptionPane.WARNING_MESSAGE)
                );
            }
            return;
        }
        useCardImpl(game, currentPad, targetA, targetB, targetBridge);
    }
    
    // Implementation method for subclasses
    protected abstract void useCardImpl(GameLogic game, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge);
    
    // Getter for if a card is used
    public boolean isUsed()
    {
        return used;
    }

    // Sets a card as having been used
    public void markAsUsed()
    {
        this.used = true;
    }
}