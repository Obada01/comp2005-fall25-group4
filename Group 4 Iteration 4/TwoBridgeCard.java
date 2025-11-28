public class TwoBridgeCard extends ActionCard {

    public TwoBridgeCard(int playerNum)
    {
        super(playerNum);
    }
 
    @Override
    protected void useCardImpl(GameLogic game, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge)
    {
        if (targetA == null || targetB == null)
        {
            return;
        }

        if (game.areConnectedByBridge(targetA, targetB))
        {
            return;
        }

        game.addBridgeBetween(targetA, targetB);
        
        markAsUsed();
    }
}