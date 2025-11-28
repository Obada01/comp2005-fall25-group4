public class RemoveBridgeCard extends ActionCard {

    public RemoveBridgeCard(int playerNum) {
        super(playerNum);
    }

    @Override
    protected void useCardImpl(GameLogic game, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge) {
        if (targetA == null || targetB == null)
        {
            return;
        }
        
        if (!game.areConnectedByBridge(targetA, targetB))
        {
            return;
        }
        
        game.removeBridgeBetween(targetA, targetB);
        markAsUsed();
    }
}