import java.io.Serializable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int playerCount;
    private boolean computerPlayer;
    private String difficulty;
    private int colorRGB;
    private int currentPlayer;
    private boolean hasMoved;
    private boolean hasPlacedBridge;
    private boolean hasUsedParachute;
    private Map<Integer, Boolean> extraJumpUsed;
    private Map<Integer, Boolean> removeBridgeUsed;
    private Map<Integer, Boolean> twoBridgeUsed;
    private Map<Integer, Boolean> parachuteUsed;
    private List<FrogPosition> frogPositions;
    private List<BridgePair> bridges;
    private List<Integer> finishedPlayers;
    
    public static class FrogPosition implements Serializable
    {
        private static final long serialVersionUID = 1L;
        public int playerNumber;
        public int frogIndex;
        public int lilipadIndex;
        public boolean canMove;
        
        public FrogPosition(int playerNumber, int frogIndex, int lilipadIndex, boolean canMove)
        {
            this.playerNumber = playerNumber;
            this.frogIndex = frogIndex;
            this.lilipadIndex = lilipadIndex;
            this.canMove = canMove;
        }
    }

    public static class BridgePair implements Serializable
    {
        private static final long serialVersionUID = 1L;
        public int lilipad1Index;
        public int lilipad2Index;
        public boolean isPermanent;
        
        public BridgePair(int lilipad1Index, int lilipad2Index, boolean isPermanent)
        {
            this.lilipad1Index = lilipad1Index;
            this.lilipad2Index = lilipad2Index;
            this.isPermanent = isPermanent;
        }
    }
    
    public GameState()
    {
        extraJumpUsed = new HashMap<>();
        removeBridgeUsed = new HashMap<>();
        twoBridgeUsed = new HashMap<>();
        parachuteUsed = new HashMap<>();
        frogPositions = new ArrayList<>();
        bridges = new ArrayList<>();
        finishedPlayers = new ArrayList<>();
    }
    
    public int getPlayerCount()
    {
        return playerCount;
    }
    
    public void setPlayerCount(int playerCount)
    {
        this.playerCount = playerCount;
    }
    
    public boolean isComputerPlayer()
    {
        return computerPlayer;
    }
    
    public void setComputerPlayer(boolean computerPlayer)
    {
        this.computerPlayer = computerPlayer;
    }
    
    public String getDifficulty()
    {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }
    
    public Color getColor()
    {
        return new Color(colorRGB);
    }
    
    public void setColor(Color color)
    {
        this.colorRGB = color.getRGB();
    }
    
    public int getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer)
    {
        this.currentPlayer = currentPlayer;
    }
    
    public boolean hasMoved()
    {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }
    
    public boolean hasPlacedBridge()
    {
        return hasPlacedBridge;
    }

    public void setHasPlacedBridge(boolean hasPlacedBridge)
    {
        this.hasPlacedBridge = hasPlacedBridge;
    }
    
    public boolean hasUsedParachute()
    {
        return hasUsedParachute;
    }

    public void setHasUsedParachute(boolean hasUsedParachute)
    {
        this.hasUsedParachute = hasUsedParachute;
    }
    
    public Map<Integer, Boolean> getExtraJumpUsed()
    {
        return extraJumpUsed;
    }

    public void setExtraJumpUsed(Map<Integer, Boolean> extraJumpUsed)
    {
        this.extraJumpUsed = extraJumpUsed;
    }
    
    public Map<Integer, Boolean> getRemoveBridgeUsed()
    {
        return removeBridgeUsed;
    }

    public void setRemoveBridgeUsed(Map<Integer, Boolean> removeBridgeUsed)
    { 
        this.removeBridgeUsed = removeBridgeUsed;
    }
    
    public Map<Integer, Boolean> getTwoBridgeUsed()
    {
        return twoBridgeUsed;
    }

    public void setTwoBridgeUsed(Map<Integer, Boolean> twoBridgeUsed)
    {
        this.twoBridgeUsed = twoBridgeUsed;
    }
    
    public Map<Integer, Boolean> getParachuteUsed()
    {
        return parachuteUsed;
    }

    public void setParachuteUsed(Map<Integer, Boolean> parachuteUsed)
    {
        this.parachuteUsed = parachuteUsed;
    }
    
    public List<FrogPosition> getFrogPositions()
    {
        return frogPositions;
    }

    public void setFrogPositions(List<FrogPosition> frogPositions)
    {
        this.frogPositions = frogPositions;
    }
    
    public List<BridgePair> getBridges()
    {
        return bridges;
    }

    public void setBridges(List<BridgePair> bridges)
    {
        this.bridges = bridges;
    }
    
    public List<Integer> getFinishedPlayers()
    {
        return finishedPlayers;
    }

    public void setFinishedPlayers(List<Integer> finishedPlayers)
    {
        this.finishedPlayers = finishedPlayers;
    }
}