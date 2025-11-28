import java.io.*;
import java.util.*;

public class SaveManager
{
    private static final String SAVE_DIRECTORY = "saves";
    private static final String SAVE_FILE = "game_save.dat";
    
    public static boolean saveGame(GameState gameState)
    {
        File saveDir = new File(SAVE_DIRECTORY);

        if (!saveDir.exists())
        {
            saveDir.mkdir();
        }
        
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile)))
        {
            oos.writeObject(gameState);
            return true;
        }

        catch (IOException e)
        {
            return false;
        }
    }
    
    public static GameState loadGame()
    {
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        
        if (!saveFile.exists())
        {
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile)))
        {
            return (GameState) ois.readObject();
        }

        catch (IOException | ClassNotFoundException e)
        {
            return null;
        }
    }
    
    public static boolean saveFileExists()
    {
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        return saveFile.exists();
    }
    
    public static boolean deleteSave()
    {
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        
        if (saveFile.exists())
        {
            return saveFile.delete();
        }
        
        return false;
    }
    
    public static GameLogic restoreGameState(GameState state, SettingsMenu settingsMenu)
    {
        settingsMenu.playerAmount = state.getPlayerCount();
        settingsMenu.computerPlayer = state.isComputerPlayer();
        settingsMenu.difficulty = state.getDifficulty();
        settingsMenu.color = state.getColor();
        
        GameLogic gameLogic = new GameLogic(state.getColor(), state.getPlayerCount());
        
        Lilipad[][] grid = gameLogic.getGrid();
        
        for (int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[row].length; col++)
            {
                if (grid[row][col] != null)
                {
                    grid[row][col].getFrogs().clear();
                }
            }
        }
        
        for (GameState.FrogPosition pos : state.getFrogPositions())
        {
            java.util.ArrayList<Frog> playerFrogs = gameLogic.getPlayerFrogs(pos.playerNumber);
            
            if (pos.frogIndex < playerFrogs.size())
            {
                Frog frog = playerFrogs.get(pos.frogIndex);
                frog.setCanMove(pos.canMove);

                Lilipad targetPad = findLilipadByIndex(grid, pos.lilipadIndex);
                
                if (targetPad != null)
                {
                    targetPad.addFrogDirectly(frog);
                    frog.moveTo(targetPad);
                }
            }
        }
        
        for (int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[row].length; col++)
            {
                if (grid[row][col] != null)
                {
                    grid[row][col].getBridges().clear();
                }
            }
        }

        gameLogic.getBridges().clear();
        gameLogic.clearAllBridges();
        
        for (GameState.BridgePair pair : state.getBridges())
        {
            Lilipad lili1 = findLilipadByIndex(grid, pair.lilipad1Index);
            Lilipad lili2 = findLilipadByIndex(grid, pair.lilipad2Index);
            
            if (lili1 != null && lili2 != null)
            {
                gameLogic.addBridgeBetween(lili1, lili2);
                Bridge addedBridge = lili1.getBridgeTo(lili2);
                
                if (addedBridge != null)
                {
                    addedBridge.setPermanent(pair.isPermanent);
                }
            }
        }
        
        gameLogic.getTurnLogic().setCurrentPlayer(state.getCurrentPlayer());
        gameLogic.restoreTurnState(state.hasMoved(), state.hasPlacedBridge(), state.hasUsedParachute());
        
        for (int i = 1; i <= state.getPlayerCount(); i++)
        {
            if (state.getExtraJumpUsed().getOrDefault(i, false))
            {
                gameLogic.getExtraJumpCard(i).markAsUsed();
            }
            if (state.getRemoveBridgeUsed().getOrDefault(i, false))
            {
                gameLogic.getRemoveBridgeCard(i).markAsUsed();
            }
            if (state.getTwoBridgeUsed().getOrDefault(i, false))
            {
                gameLogic.getTwoBridgeCard(i).markAsUsed();
            }
            if (state.getParachuteUsed().getOrDefault(i, false))
            {
                gameLogic.getParachuteCard(i).markAsUsed();
            }
        }
        
        gameLogic.restoreFinishedPlayers(state.getFinishedPlayers());
        
        return gameLogic;
    }

    private static Lilipad findLilipadByIndex(Lilipad[][] grid, int index)
    {
        for (int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[row].length; col++)
            {
                if (grid[row][col] != null && grid[row][col].getIndexNumber() == index)
                {
                    return grid[row][col];
                }
            }
        }
        return null;
    }

    public static GameState createGameState(SettingsMenu settingsMenu, GameLogic gameLogic)
    {
        GameState state = new GameState();
        
        state.setPlayerCount(settingsMenu.playerAmount);
        state.setComputerPlayer(settingsMenu.computerPlayer);
        state.setDifficulty(settingsMenu.difficulty);
        state.setColor(settingsMenu.color);
        
        TurnLogic turnLogic = gameLogic.getTurnLogic();
        state.setCurrentPlayer(turnLogic.getCurrentPlayer());
        state.setHasMoved(turnLogic.hasMoved());
        state.setHasPlacedBridge(turnLogic.hasPlacedBridge());
        state.setHasUsedParachute(turnLogic.hasUsedParachute());
        
        Map<Integer, Boolean> extraJumpUsed = new HashMap<>();
        Map<Integer, Boolean> removeBridgeUsed = new HashMap<>();
        Map<Integer, Boolean> twoBridgeUsed = new HashMap<>();
        Map<Integer, Boolean> parachuteUsed = new HashMap<>();
        
        for (int i = 1; i <= settingsMenu.playerAmount; i++)
        {
            extraJumpUsed.put(i, gameLogic.getExtraJumpCard(i).isUsed());
            removeBridgeUsed.put(i, gameLogic.getRemoveBridgeCard(i).isUsed());
            twoBridgeUsed.put(i, gameLogic.getTwoBridgeCard(i).isUsed());
            parachuteUsed.put(i, gameLogic.getParachuteCard(i).isUsed());
        }
        
        state.setExtraJumpUsed(extraJumpUsed);
        state.setRemoveBridgeUsed(removeBridgeUsed);
        state.setTwoBridgeUsed(twoBridgeUsed);
        state.setParachuteUsed(parachuteUsed);
        
        for (int i = 1; i <= settingsMenu.playerAmount; i++)
        {
            ArrayList<Frog> playerFrogs = gameLogic.getPlayerFrogs(i);
            
            for (int j = 0; j < playerFrogs.size(); j++)
            {
                Frog frog = playerFrogs.get(j);
                Lilipad currentPad = frog.getCurrentLilipad();
                
                if (currentPad != null)
                {
                    GameState.FrogPosition pos = new GameState.FrogPosition(frog.getPlayerNumber(), j, currentPad.getIndexNumber(), frog.canMove());
                    state.getFrogPositions().add(pos);
                }
            }
        }
        
        for (Bridge bridge : gameLogic.getBridges())
        {
            if (bridge.getConnections().size() == 2)
            {
                Lilipad lili1 = bridge.getConnections().get(0);
                Lilipad lili2 = bridge.getConnections().get(1);
                GameState.BridgePair pair = new GameState.BridgePair(lili1.getIndexNumber(), lili2.getIndexNumber(), bridge.isPermanent());
                state.getBridges().add(pair);
            }
        }
        
        state.setFinishedPlayers(gameLogic.getFinishedPlayers());
        
        return state;
    }
}