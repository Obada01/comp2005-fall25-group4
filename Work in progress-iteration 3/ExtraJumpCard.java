public class ExtraJumpCard extends ActionCard{

    public ExtraJumpCard(int playerNumber) {
        super(playerNumber);
    }

    @Override
    public void useCard(GameBoard board, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge){
        if (targetA.isOccupied != null) //isOccupied returns either null or the frog object, not boolean. This kinda fixes it
        {
            System.out.println("Invalid arguments for special card.");
            return;
        }
        if (!used) {
            Frog frog = currentPad.getIsOccupied();
            frog.moveFrog(targetA);
            System.out.println("Player " + playerNumber + " jumped to Lilypad " + targetA + " using Extra Jump Card.");
            used = true;
        } else {
            System.out.println("This card has already been used.");
        }
    }
}