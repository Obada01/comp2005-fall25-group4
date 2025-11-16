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
                Frog frog = currentPad.getIsOccupied();
                currentPad.removeFrog(); //These two used to both be setIsOccupied() which doesn't exist
                targetA.addFrog(frog); //I replaced them with actual methods that *should* do what they were trying to do
                System.out.println("Player " + playerNumber + " jumped to Lilypad " + targetA + " without using a bridge.");
            }
            used = true;
        } else {
            System.out.println("This card has already been used.");
        }
    }
}