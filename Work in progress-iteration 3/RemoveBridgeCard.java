public class RemoveBridgeCard extends ActionCard {

    public RemoveBridgeCard(int playerNum) {
        super(playerNum);
    }

    @Override
    public boolean useCard(GameBoard board, Lilipad lPad1, Lilipad lPad2) {
        if (lPad1==null || lPad2==null) {
            return false;
        }
        if (lPad1==lPad2) {
            return false;
        }

        boolean removedBridge= board.removeBridge(lPad1, lPad2);
        return removedBridge;
    }
}
