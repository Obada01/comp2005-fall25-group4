import javax.swing.*;

// Class does not need a constructor, as all of it's methods are static.
public class CardHandler
{
    // Method to handle the extra jump card use. Mainly the dialogue.
    public static void handleExtraJump(GameLogic gameLogic, java.awt.Component parent)
    {
        ActionCard card = gameLogic.getExtraJumpCard();
        if (card.isUsed())
        {
            showMessage(parent, "You cannot use a card more than once per game.", "Card Already Used", JOptionPane.WARNING_MESSAGE);
            return;
        }
        showMessage(parent, "Select one of your frogs to jump with!");
        gameLogic.startExtraJump();
    }

    // Method to handle the parachute card use. Mainly the dialogue.
    public static void handleParachute(GameLogic gameLogic, java.awt.Component parent)
    {
        ActionCard card = gameLogic.getParachuteCard();
        if (card.isUsed())
        {
            showMessage(parent, "You cannot use a card more than once per game.", "Card Already Used", JOptionPane.WARNING_MESSAGE);
            return;
        }
        gameLogic.startParachute();
        showMessage(parent, "Select a frog to move, then a lilipad without a bridge to jump to!");
    }

    // Method to handle the two bridges card use. Mainly the dialogue.
    public static void handleTwoBridge(GameLogic gameLogic, java.awt.Component parent)
    {
        ActionCard card = gameLogic.getTwoBridgeCard();
        if (card.isUsed())
        {
            showMessage(parent, "You cannot use a card more than once per game.", "Card Already Used", JOptionPane.WARNING_MESSAGE);
            return;
        }
        gameLogic.startTwoBridge();
        showMessage(parent, "Select a pair of lilipads to place an extra bridge!");
    }

    // Method to handle the remove bridge card use. Mainly the dialogue.
    public static void handleRemoveBridge(GameLogic gameLogic, java.awt.Component parent)
    {
        ActionCard card = gameLogic.getRemoveBridgeCard();
        if (card.isUsed())
        {
            showMessage(parent, "You cannot use a card more than once per game.", "Card Already Used", JOptionPane.WARNING_MESSAGE);
            return;
        }
        gameLogic.startRemoveBridge();
        showMessage(parent, "Select a pair of lilipads to remove the bridge between them!");
    }

    // Helper method to show messages
    public static void showMessage(java.awt.Component parent, String message, String title, int messageType)
    {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }
    
    // Simpler version of the above, meant to cover common parameters.
    private static void showMessage(java.awt.Component parent, String message)
    {
        showMessage(parent, message, "Card Action", JOptionPane.INFORMATION_MESSAGE);
    }
}
