public class TwoBridgeCard extends ActionCard {

    public TwoBridgeCard(int  playerNum) {
        super(playerNum);
    }
 
    @Override
    public boolean useCard(GameBoard board, Lilipad currentPad, Lilipad lPad1, Lilipad lPad2) {
        if (currentPad==null || lPad1==null || lPad2==null) {
            return false;
        }
        if (currentPad==lPad1 || lPad1==lPad2 || currentPad==lPad2) {
            return false;
        }
        
        boolean bridge1Placed= board.placeBridge(currentPad, lPad1);
        boolean bridge2Placed= board.placeBridge(lPad1, lPad2);

        return bridge1Placed && bridge2Placed;
    }
}

