public class ActionCard {
    private int playerNumber;
    private boolean used;

    public ActionCard(int playerNumber) {
        this.playerNumber = playerNumber;
        this.used = false;
    }

    // Method to be implemented by subclasses
    public int useCard(){}
}