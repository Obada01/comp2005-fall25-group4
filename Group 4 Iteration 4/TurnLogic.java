import java.awt.Color;

public class TurnLogic
{
    private int currentPlayer;
    private final int maxPlayers;
    private final GameLogic gameLogic;
    private boolean hasMoved = false;
    private boolean hasPlacedBridge = false;
    private boolean isPlacingBridge = false;
    private boolean hasUsedParachute = false;

    public TurnLogic(GameLogic gameLogic, int playerCount)
    {
        if (playerCount != 2 && playerCount != 4)
        {
            throw new IllegalArgumentException("Player count must be 2 or 4");
        }

        this.gameLogic = gameLogic;
        this.maxPlayers = playerCount;
        this.currentPlayer = 1;
    }

    public int getCurrentPlayer()
    {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(int playerNumber)
    {
        this.currentPlayer = playerNumber;
        resetTurnState();
    }

    public boolean isPlayersTurn(int playerNumber)
    {
        return currentPlayer == playerNumber;
    }

    public boolean canMakeMove()
    {
        return !hasMoved && !hasPlacedBridge && !hasUsedParachute;
    }
    
    public boolean canPlaceBridge()
    {
        return !hasMoved && !hasPlacedBridge && !hasUsedParachute;
    }

    public void markMoveMade()
    {
        hasMoved = true;
        isPlacingBridge = false;
    }
    
    public void markBridgePlaced()
    {
        hasPlacedBridge = true;
        isPlacingBridge = false;
    }
    
    public boolean isPlacingBridge()
    {
        return isPlacingBridge;
    }
    
    public void startBridgePlacement()
    {
        if (canPlaceBridge())
        {
            isPlacingBridge = true;
        }
    }
    
    public void markParachuteUsed()
    {
        hasUsedParachute = true;
        hasMoved = true;
    }
    
    private void resetTurnState()
    {
        hasMoved = false;
        hasPlacedBridge = false;
        hasUsedParachute = false;
        isPlacingBridge = false;
    }
    
    public boolean hasUsedParachute()
    {
        return hasUsedParachute;
    }
    
    public boolean hasMoved()
    {
        return hasMoved;
    }
    
    public boolean hasPlacedBridge()
    {
        return hasPlacedBridge;
    }
    
    public void nextTurn()
    {
        currentPlayer = (currentPlayer % maxPlayers) + 1;
        hasMoved = false;
        hasPlacedBridge = false;
        isPlacingBridge = false;
        hasUsedParachute = false;
        
        if (gameLogic != null)
        {
            gameLogic.resetCardState();
        }
    }

    public boolean canMoveFrog(Frog frog)
    {
        if (frog == null || !isPlayersTurn(frog.getPlayerNumber()))
        {
            return false;
        }

        Lilipad currentPad = frog.getCurrentLilipad();
        if (currentPad != null && currentPad.getIsStarterPad())
        {
            for (int playerNum = 1; playerNum <= maxPlayers; playerNum++)
            {
                if (playerNum != frog.getPlayerNumber())
                {
                    Lilipad opponentHome = gameLogic.getLilipadByIndex(getHomepadIndexForPlayer(playerNum));

                    if (opponentHome != null && currentPad == opponentHome)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int getHomepadIndexForPlayer(int playerNumber)
    {
        if (maxPlayers == 2)
        {
            return playerNumber == 1 ? 100 : 101;
        }
        
        else
        {
            return switch (playerNumber)
            {
                case 1 -> 102;
                case 2 -> 103;
                case 3 -> 100;
                case 4 -> 101;
                default -> 100;
            };
        }
    }

    public boolean canUseActionCard(int playerNumber)
    {
        return isPlayersTurn(playerNumber);
    }

    public void endTurn()
    {
        nextTurn();
    }
    
    public Color getCurrentPlayerColor()
    {
        return switch (currentPlayer)
        {
            case 1 -> Color.RED;
            case 2 -> Color.BLUE;
            case 3 -> Color.YELLOW;
            case 4 -> Color.MAGENTA;
            default -> Color.BLACK;
        };
    }

    public void restoreTurnState(boolean hasMoved, boolean hasPlacedBridge, boolean hasUsedParachute)
    {
        this.hasMoved = hasMoved;
        this.hasPlacedBridge = hasPlacedBridge;
        this.hasUsedParachute = hasUsedParachute;
        this.isPlacingBridge = false;
    }
}
