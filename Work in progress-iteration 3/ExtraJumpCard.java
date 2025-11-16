public class ExtraJumpCard extends ActionCard{

    public ExtraJumpCard(int playerNumber) {
        super(playerNumber);
    }

    @Override
    public void useCard(GameBoard board, Lilipad currentPad, Lilipad targetA, Lilipad targetB, Bridge targetBridge){
     
       if(used){
        System.out.println("Extra Jump Card is already used");
        return;
       }
       if(currentPad==null||targetA==null){
        System.out.println("ExtraJumpCard: Missing Arguments");
        return;
       }
       if(targetA.getIsOccupied() !=null){
        System.out.println("The first Lilipad must be empty");
        return;
       }

       board.performCardJump(currentPad, targetA);

       if(targetB !=null){
        board.performCardJump(targetA, targetB);
       }
       used = true;

    }


}