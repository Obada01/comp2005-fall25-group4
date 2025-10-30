public abstract class ActionCard {
    int playerNumber;
    boolean used;

    public ActionCard(int playerNumber) {
        this.playerNumber = playerNumber;
        this.used = false;
    }

    // Method to be implemented by subclasses
    public abstract void useCard(){}
}