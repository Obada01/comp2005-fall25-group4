public class RemoveBridgeCard extends ActionCard {

    public RemoveBridgeCard(int playerNum) {
        super(playerNum);
    }

    @Override
    public void useCard(GameBoard board, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge) {
        if(used){
            System.out.println("Remove Bridge Card Already Used!");
            return;
        }
        if(targetA==null || targetB==null){
            System.out.println("Remove Bridge Card: Lilipads not Selected");
        }
        board.removeBridgeBetween(targetA, targetB);

        used=true;
        System.out.println("Bridge removed at: "+ targetA.getIndexNumber() + " and " + targetB.getIndexNumber());
    }
    
    //Look at EJ-Card and JNB-Card's formatting with their print messages and changing the isUsed varaible
    //Replicate that formatting in this card and TB-Card
    //We'll need to also make the actual buttons work
    //I've added the ActionListener to them and made the framework of their "methods" at the very bottom of GameBoard
    //Said methods have to be implemented though
    //They MUST receive all of the above parameters, even if said parameters aren't used
    //Maybe try making and passing pointless defaults for the unused ones in each card, it'd be my first idea
    //But if you can think if something better, go nuts
    //I'm out of time to work on this right now, but I'll be in class later today
    //-Miller
}

   