public class TwoBridgeCard extends ActionCard {

    public TwoBridgeCard(int  playerNum) {
        super(playerNum);
    }
 
    @Override
    public void useCard(GameBoard board, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge) {

        if(used){
            System.out.println("This card is already used");
            return;
        }

        if(targetA==null||targetB==null){
            System.out.println("Invalid arguments for Two Bridge Card");
            return;
        }

        if(board.areConnectedByBridge(targetA,targetB)){
            System.out.println("These Lilipads already have a bridge");
            return;
        }

        board.addBridgeBetween(targetA, targetB);
    }
}

