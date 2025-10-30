public abstract class ActionCard {
    int playerNumber;
    boolean used;

    public ActionCard(int playerNumber) {
        this.playerNumber = playerNumber;
        this.used = false;
    }

    // Method to be implemented by subclasses
    public abstract void useCard(GameBoard board, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge){}
    
}