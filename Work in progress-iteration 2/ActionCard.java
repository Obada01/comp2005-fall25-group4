public abstract class ActionCard {
    int playerNumber;
    boolean used;

    public ActionCard(int playerNumber) {
        this.playerNumber = playerNumber;
        this.used = false;
    }
    public int getPlayerNum() {
        return playerNum;
    }

    public boolean isUsed() {
        return used;
    }

    // Method to be implemented by subclasses
<<<<<<< HEAD
    public abstract void useCard(GameBoard board, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge);
    
=======
    public abstract void useCard();
>>>>>>> 969af248318d9f23bec6f95343f69b9708a6b6e1
}