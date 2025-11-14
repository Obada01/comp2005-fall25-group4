public class TwoBridgeCard extends ActionCard {

    public TwoBridgeCard(int  playerNum) {
        super(playerNum);
    }
 
    @Override
    public void useCard(GameBoard board, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge) {
        board.addBridgeBetween(currentPad, targetA);
        board.addBridgeBetween(targetA, targetB);
    }
}

