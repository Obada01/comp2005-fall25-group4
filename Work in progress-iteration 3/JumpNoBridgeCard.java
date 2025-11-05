public class JumpNoBridgeCard extends ActionCard{

    public JumpNoBridgeCard(int playerNumber) {
        super(playerNumber);
    }

    @Override
    public void useCard(GameBoard board, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge){
        if (currentPad == null || targetA == null) {
            System.out.println("Invalid arguments for special card.");
            return;
        }
        if (!used) {
            if (targetBridge == null){
                Frog frog = currentPad.getFrog();
                currentPad.setOccupied(null);
                targetA.setOccupied(frog);
                System.out.println("Player " + playerNumber + " jumped to Lilypad " + targetA + " without using a bridge.");
            }
            used = true;
        } else {
            System.out.println("This card has already been used.");
        }
    }
}