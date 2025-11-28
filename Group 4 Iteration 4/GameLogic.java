import java.awt.Color;
import java.util.*;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GameLogic
{
    private static final int GRID_SIZE = 7;
    private static final int INVALID_INDEX = 999;
    private static final int FROG_START_LEFT = 100;
    private static final int FROG_START_RIGHT = 101;
    private static final int FROG_START_TOP = 102;
    private static final int FROG_START_BOTTOM = 103;
    
    private final Lilipad[][] grid;
    private final ArrayList<Bridge> bridges;
    private final ArrayList<Frog> playerFrogs;
    private final Color color;
    
    private final Map<Integer, ExtraJumpCard> extraJumpCards = new HashMap<>();
    private final Map<Integer, RemoveBridgeCard> removeBridgeCards = new HashMap<>();
    private final Map<Integer, TwoBridgeCard> twoBridgeCards = new HashMap<>();
    private final Map<Integer, ParachuteCard> parachuteCards = new HashMap<>();
    private final Map<Integer, CPUPlayer> cpuPlayers = new HashMap<>();

    private static class CardStateManager
    {
        private CardState currentState = CardState.NONE;
        private Map<CardState, Object> stateData = new EnumMap<>(CardState.class);
        
        void setState(CardState newState, Object data)
        {
            currentState = newState;
            
            if (data != null)
            {
                stateData.put(newState, data);
            }
        }
        
        CardState getState()
        {
            return currentState;
        }
        
        @SuppressWarnings("unchecked")
        <T> T getStateData(Class<T> type)
        {
            return (T) stateData.get(currentState);
        }
        
        void clear()
        {
            currentState = CardState.NONE;
            stateData.clear();
        }
        
        boolean isActive()
        {
            return currentState != CardState.NONE;
        }
    }
    
    private CardStateManager cardStateManager = new CardStateManager();

    public static class ChainReactionState
    {
        final Frog movingFrog;
        final Frog displacedFrog;
        final Lilipad fromPad;
        final Lilipad toPad;
        final List<Lilipad> options;
        final boolean consumesBridge;
        
        ChainReactionState(Frog movingFrog, Frog displacedFrog, Lilipad fromPad, Lilipad toPad, List<Lilipad> options, boolean consumesBridge)
        {
            this.movingFrog = movingFrog;
            this.displacedFrog = displacedFrog;
            this.fromPad = fromPad;
            this.toPad = toPad;
            this.options = new ArrayList<>(options);
            this.consumesBridge = consumesBridge;
        }
    }
    
    private Deque<ChainReactionState> chainReactionStack = new ArrayDeque<>();
    private ChainReactionState currentChainReaction = null;
    
    public ChainReactionState getCurrentChainReaction()
    {
        return currentChainReaction;
    }

    private Lilipad firstBridgeLilipad = null;
    private Frog selectedFrog = null;

    private final int playerCount;
    private final int totalPlayers;
    private boolean gameOver = false;
    private final LinkedHashSet<Integer> finishedPlayers = new LinkedHashSet<>();
    private final TurnLogic turnLogic;
    
    public int getPlayerCount()
    {
        return playerCount;
    }
    
    public TurnLogic getTurnLogic()
    {
        return turnLogic;
    }
    
    public void onTurnChanged(int playerNumber)
    {
        resetCardState();
    }
    
    public boolean isCardActive()
    {
        return cardStateManager.isActive();
    }
    
    public boolean canMoveFrog(Frog frog)
    {
        if(frog != null && turnLogic.canMoveFrog(frog) && !isCardActive() && !turnLogic.isPlacingBridge())
        {
            return true;
        }
        return false;
    }
    
    private boolean isOpponentHomepad(Lilipad lili, int playerNumber)
    {
        if (lili == null || !lili.getIsStarterPad())
        {
            return false;
        }
        
        for (int i = 1; i <= playerCount; i++)
        {
            if (i != playerNumber)
            {
                int homepadIndex = getStartPositionForPlayer(i);
                
                if (lili.getIndexNumber() == homepadIndex)
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasFrogOnHomepad(int playerNumber, Lilipad homepad)
    {
        if (homepad == null || !homepad.getIsStarterPad())
        {
            return false;
        }
        
        for (Frog frog : homepad.getFrogs())
        {
            if (frog.getPlayerNumber() == playerNumber)
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean isValidMove(Frog frog, Lilipad target)
    {
        if (frog == null || target == null || target == frog.getCurrentLilipad())
        {
            return false;
        }
        
        if (playerCount == 4)
        {
            int frogPlayerNumber = frog.getPlayerNumber();
            if (isOpponentHomepad(target, frogPlayerNumber) && hasFrogOnHomepad(frogPlayerNumber, target))
            {
                return false;
            }
        }
        
        Lilipad current = frog.getCurrentLilipad();

        if (getBridgeBetween(current, target) == null)
        {
            return false;
        }

        if (target.getIsStarterPad() || !target.isOccupied())
        {
            return true;
        }

        ArrayList<Lilipad> options = getDisplacementOptions(current, target);
        return !options.isEmpty();
    }
    
    private Bridge getBridgeBetween(Lilipad lili1, Lilipad lili2)
    {
        if (lili1 == null || lili2 == null)
        {
            return null;
        }
        
        for (Bridge bridge : lili1.getBridges())
        {
            if (bridge.connects(lili1, lili2))
            {
                return bridge;
            }
        }
        return null;
    }
    
    public boolean moveFrog(Frog frog, Lilipad target)
    {
        return moveFrogInternal(frog, target, true);
    }

    public boolean moveFrogParachute(Frog frog, Lilipad target)
    {
        return moveFrogInternal(frog, target, false);
    }
    
    private boolean moveFrogInternal(Frog frog, Lilipad target, boolean useBridge)
    {
        if (frog == null || target == null)
        {
            return false;
        }
        
        Lilipad current = frog.getCurrentLilipad();

        if (current == null || current == target)
        {
            return false;
        }
        
        if (useBridge && !isValidMove(frog, target))
        {
            return false;
        }
        
        if (!target.isOccupied() || target.getIsStarterPad())
        {
            moveFrogToTarget(frog, current, target, useBridge);
            return true;
        }
        
        return handleChainReaction(frog, current, target, useBridge);
    }

    private boolean handleChainReaction(Frog movingFrog, Lilipad from, Lilipad to, boolean consumesBridge)
    {
        Frog targetFrog = to.getFrogs().get(0);
        List<Lilipad> options = getDisplacementOptions(from, to);

        if (options.isEmpty())
        {
            return false;
        }

        ChainReactionState reactionState = new ChainReactionState(movingFrog, targetFrog, from, to, options, consumesBridge);
        
        if (currentChainReaction != null)
        {
            chainReactionStack.push(currentChainReaction);
        }

        currentChainReaction = reactionState;

        if (isCPU(targetFrog.getPlayerNumber()))
        {
            handleCpuDisplacedFrog();
            return true;
        }

        for (Lilipad pad : options)
        {
            pad.setHighlighted(true);
        }

        return true;
    }

    public ArrayList<Lilipad> getDisplacementOptions(Lilipad from, Lilipad to)
    {
        ArrayList<Lilipad> options = new ArrayList<>();
        if (to == null)
        {
            return options;
        }

        for (Bridge bridge : to.getBridges())
        {
            for (Lilipad connected : bridge.getConnections())
            {
                if (connected != to && connected != from && !connected.isOccupied())
                {
                    if (!options.contains(connected))
                    {
                        options.add(connected);
                    }
                }
            }
        }

        return options;
    }
    
    private void moveFrogCommon(Frog frog, Lilipad from, Lilipad to, boolean removeBridge)
    {
        if (frog == null || from == null || to == null) return;
        
        from.removeFrog(frog);
        to.addFrog(frog);
        frog.moveTo(to);
        
        if (removeBridge)
        {
            removeBridgeBetween(from, to);
            from.repaint();
            to.repaint();
            
            if (from.getParent() != null)
            {
                from.getParent().repaint();
            }
        }
    }
    
    private void moveFrogToTarget(Frog frog, Lilipad from, Lilipad to, boolean removeBridge)
    {
        moveFrogCommon(frog, from, to, removeBridge);
    }
    
    
    public boolean handleBridgePlacement(Lilipad lili)
    {
        if (!turnLogic.isPlacingBridge() || lili == null)
        {
            return false;
        }
        
        if (firstBridgeLilipad == null)
        {
            firstBridgeLilipad = lili;
            return false;
        }
        else if (firstBridgeLilipad != lili)
        {
            createBridge(firstBridgeLilipad, lili);
            firstBridgeLilipad = null;
            turnLogic.markBridgePlaced();
            return true;
        }
        
        return false;
    }
    
    private boolean createBridge(Lilipad lili1, Lilipad lili2)
    {
        if (lili1 == null || lili2 == null || lili1 == lili2) {
            return false;
        }
        
        int row1 = -1, col1 = -1, row2 = -1, col2 = -1;
        
        outer: for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                if (grid[i][j] == lili1)
                {
                    row1 = i;
                    col1 = j;
                }
                if (grid[i][j] == lili2)
                {
                    row2 = i;
                    col2 = j;
                }
                if (row1 != -1 && row2 != -1)
                {
                    break outer;
                }
            }
        }
        
        if (row1 == -1 || row2 == -1)
        {
            return false;
        }
        
        int rowDiff = Math.abs(row1 - row2);
        int colDiff = Math.abs(col1 - col2);
        
        if ((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1))
        {
            addBridgeBetween(lili1, lili2);
            return true;
        }
        
        return false;
    }
    
    public boolean startBridgePlacement()
    {
        if (turnLogic.canPlaceBridge()) {
            turnLogic.startBridgePlacement();
            firstBridgeLilipad = null;
            return true;
        }
        return false;
    }
    
    public void cancelBridgePlacement()
    {
        firstBridgeLilipad = null;
        if (turnLogic.isPlacingBridge())
        {
            turnLogic.nextTurn();
        }
    }

    public void clearAllBridges()
    {
        bridges.clear();
    }
    
    public boolean isUsingRemoveBridge()
    {
        return cardStateManager.getState() == CardState.REMOVE_BRIDGE;
    }
    
    public boolean isUsingTwoBridge()
    {
        return cardStateManager.getState() == CardState.TWO_BRIDGE;
    }
    
    public boolean isUsingExtraJump()
    {
        return cardStateManager.getState() == CardState.EXTRA_JUMP;
    }
    
    public void resetCardState()
    {
        cardStateManager.clear();
        firstBridgeLilipad = null;
    }
    
    public GameLogic(Color color, int playerCount)
    {
        this.color = color;
        this.playerCount = playerCount;
        this.totalPlayers = playerCount;
        this.bridges = new ArrayList<>();
        this.playerFrogs = new ArrayList<>();
        this.turnLogic = new TurnLogic(this, playerCount);

        for (int i = 0; i < playerCount; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                playerFrogs.add(new Frog(true, i + 1, new ArrayList<>(), getStartPositionForPlayer(i + 1)));
            }
        }

        // Initialize cards for each player
        for (int i = 1; i <= playerCount; i++)
        {
            extraJumpCards.put(i, new ExtraJumpCard(i));
            removeBridgeCards.put(i, new RemoveBridgeCard(i));
            twoBridgeCards.put(i, new TwoBridgeCard(i));
            parachuteCards.put(i, new ParachuteCard(i));
        }

        this.grid = new Lilipad[GRID_SIZE][GRID_SIZE];
        
        initializeGrid();
    }

    public void initializeGrid()
    {
        Color filler = new Color(0, 100, 255);
        int idOffset = 0;
        
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int colm = 0; colm < GRID_SIZE; colm++)
            {
                grid[row][colm] = new Lilipad(filler, INVALID_INDEX, false);
            }
        }

        for (int row = 1; row < GRID_SIZE - 1; row++)
        {
            for (int colm = 1; colm < GRID_SIZE - 1; colm++)
            {
                grid[row][colm] = new Lilipad(color, (colm - 1) + idOffset, false);
            }
            if (row > 0 && row < GRID_SIZE - 2)
            {
                idOffset += 5;
            }
        }
        
        if (playerCount == 2)
        {
            grid[3][0] = new Lilipad(color, FROG_START_LEFT, true);

            for (Frog frog : getPlayerFrogs(1))
            {
                grid[3][0].addFrog(frog);
                frog.moveTo(grid[3][0]);
            }
            
            grid[3][0].setText("Frogs");
            
            grid[3][GRID_SIZE - 1] = new Lilipad(color, FROG_START_RIGHT, true);
            
            for (Frog frog : getPlayerFrogs(2))
            {
                grid[3][GRID_SIZE - 1].addFrog(frog);
                frog.moveTo(grid[3][GRID_SIZE - 1]);
            }

        }
        else if (playerCount == 4)
        {
            grid[0][3] = new Lilipad(color, FROG_START_TOP, true);
            grid[GRID_SIZE - 1][3] = new Lilipad(color, FROG_START_BOTTOM, true);
            grid[3][0] = new Lilipad(color, FROG_START_LEFT, true);
            grid[3][GRID_SIZE - 1] = new Lilipad(color, FROG_START_RIGHT, true);
            
            for (Frog frog : getPlayerFrogs(1)) 
            {
                grid[0][3].addFrog(frog);
                frog.moveTo(grid[0][3]);
            }

            for (Frog frog : getPlayerFrogs(2))
            {
                grid[GRID_SIZE - 1][3].addFrog(frog);
                frog.moveTo(grid[GRID_SIZE - 1][3]);
            }

            for (Frog frog : getPlayerFrogs(3))
            {
                grid[3][0].addFrog(frog);
                frog.moveTo(grid[3][0]);
            }

            for (Frog frog : getPlayerFrogs(4))
            {
                grid[3][GRID_SIZE - 1].addFrog(frog);
                frog.moveTo(grid[3][GRID_SIZE - 1]);
            }
            
            if (grid[0][3] != null && grid[1][3] != null)
            {
                addBridgeBetween(grid[0][3], grid[1][3]);
            }
            if (grid[GRID_SIZE-1][3] != null && grid[GRID_SIZE-2][3] != null)
            {
                addBridgeBetween(grid[GRID_SIZE-1][3], grid[GRID_SIZE-2][3]);
            }
            if (grid[3][0] != null && grid[3][1] != null)
            {
                addBridgeBetween(grid[3][0], grid[3][1]);
            }
            if (grid[3][GRID_SIZE-1] != null && grid[3][GRID_SIZE-2] != null)
            {
                addBridgeBetween(grid[3][GRID_SIZE-1], grid[3][GRID_SIZE-2]);
            }
        }
        connectAllAdjacentPads();
    }

    public void addBridgeBetween(Lilipad lili1, Lilipad lili2)
    {
        if (lili1 == null || lili2 == null || lili1 == lili2)
        {
            return;
        }
        
        if (lili1.getIndexNumber() == INVALID_INDEX || lili2.getIndexNumber() == INVALID_INDEX)
        {
            return;
        }
        
        if (findBridgeBetween(lili1, lili2) != null)
        {
            return;
        }
        
        Bridge bridge = new Bridge();
        bridge.connectLilipads(lili1, lili2);
        bridges.add(bridge);
        
        lili1.addBridge(bridge);
        lili2.addBridge(bridge);
    }

    public void resetBoardState()
    {
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int colm = 0; colm < GRID_SIZE; colm++)
            {
                grid[row][colm] = null;
            }
        }
        bridges.clear();
        
        initializeGrid();
        
        for (Frog frog : playerFrogs)
        {
            int playerNumber = frog.getPlayerNumber();
            int startPosition = getStartPositionForPlayer(playerNumber);
            Lilipad startPad = getLilipadByIndex(startPosition);
            
            if (startPad != null)
            {
                startPad.getFrogs().clear();
                startPad.addFrog(frog);
                frog.moveTo(startPad);
            }
        }
        
        connectAllAdjacentPads();
        
        if (playerCount == 4)
        {
            if (grid[0][3] != null && grid[1][3] != null)
            {
                addBridgeBetween(grid[0][3], grid[1][3]);
            }
            if (grid[GRID_SIZE-1][3] != null && grid[GRID_SIZE-2][3] != null)
            {
                addBridgeBetween(grid[GRID_SIZE-1][3], grid[GRID_SIZE-2][3]);
            }
            if (grid[3][0] != null && grid[3][1] != null)
            {
                addBridgeBetween(grid[3][0], grid[3][1]);
            }
            if (grid[3][GRID_SIZE-1] != null && grid[3][GRID_SIZE-2] != null)
            {
                addBridgeBetween(grid[3][GRID_SIZE-1], grid[3][GRID_SIZE-2]);
            }
        }
    }

    public void handleDisplacedChoiceClick(Lilipad chosenPad)
    {
        if (currentChainReaction == null)
        {
            return;
        }

        if (isCPU(currentChainReaction.displacedFrog.getPlayerNumber()))
        {
            handleCpuDisplacedFrog();
            return;
        }

        if (chosenPad == null || !currentChainReaction.options.contains(chosenPad))
        {
            return;
        }

        for (Lilipad pad : currentChainReaction.options)
        {
            pad.setHighlighted(false);
        }

        if (selectedFrog != null)
        {
            selectedFrog.setSelected(false);
            selectedFrog = null;
        }

        processChainReactionMove(chosenPad);
        processNextChainReaction();
    }

    private void handleCpuDisplacedFrog()
    {
        if (currentChainReaction == null)
        {
            return;
        }

        for (Lilipad pad : currentChainReaction.options)
        {
            pad.setHighlighted(false);
        }

        CPUPlayer cpu = getCPU(currentChainReaction.displacedFrog.getPlayerNumber());
        
        if (cpu == null)
        {
            if (!currentChainReaction.options.isEmpty())
            {
                processChainReactionMove(currentChainReaction.options.get(0));
                processNextChainReaction();
            }
            else
            {
                currentChainReaction = null;
            }
            return;
        }
        
        Lilipad choice = cpu.chooseDisplacementOption(currentChainReaction.options);

        if (choice != null)
        {
            processChainReactionMove(choice);
        }
        else if (!currentChainReaction.options.isEmpty())
        {
            processChainReactionMove(currentChainReaction.options.get(0));
        }
        processNextChainReaction();
    }

    private void processChainReactionMove(Lilipad chosenPad)
    {
        moveFrogToTarget(currentChainReaction.displacedFrog, currentChainReaction.toPad, chosenPad, currentChainReaction.consumesBridge);
        moveFrogToTarget(currentChainReaction.movingFrog, currentChainReaction.fromPad, currentChainReaction.toPad, currentChainReaction.consumesBridge);
    }

    private void processNextChainReaction()
    {
        currentChainReaction = chainReactionStack.poll();
        
        if (currentChainReaction != null)
        {
            if (isCPU(currentChainReaction.displacedFrog.getPlayerNumber()))
            {
                new Thread(() ->
                {
                    try
                    {
                        Thread.sleep(300);
                        SwingUtilities.invokeLater(this::handleCpuDisplacedFrog);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
            else
            {
                for (Lilipad pad : currentChainReaction.options)
                {
                    pad.setHighlighted(true);
                }
            }
        }
    }

    private void connectAllAdjacentPads()
    {
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int colm = 0; colm < GRID_SIZE; colm++)
            {
                Lilipad currentLili = grid[row][colm];
                
                if (currentLili == null || currentLili.getIndexNumber() == INVALID_INDEX)
                {
                    continue;
                }
                
                if (colm + 1 < GRID_SIZE)
                {
                    Lilipad rightLili = grid[row][colm + 1];

                    if (rightLili != null && rightLili.getIndexNumber() != INVALID_INDEX)
                    {
                        addBridgeBetween(currentLili, rightLili);
                    }
                }
                
                if (row + 1 < GRID_SIZE)
                {
                    Lilipad downLili = grid[row + 1][colm];
                    if (downLili != null && downLili.getIndexNumber() != INVALID_INDEX)
                    {
                        addBridgeBetween(currentLili, downLili);
                    }
                }
            }
        }
    }

    public boolean areConnectedByBridge(Lilipad lili1, Lilipad lili2)
    {
        return findBridgeBetween(lili1, lili2) != null;
    }

    private Bridge findBridgeBetween(Lilipad lili1, Lilipad lili2)
    {
        if (lili1 == null || lili2 == null)
        {
            return null;
        }
        return lili1.getBridgeTo(lili2);
    }

    public void removeBridgeBetween(Lilipad lili1, Lilipad lili2)
    {
        if (lili1 == null || lili2 == null || lili1.getIsStarterPad() || lili2.getIsStarterPad()) {
            return;
        }

        Bridge bridge = findBridgeBetween(lili1, lili2);

        if (bridge != null)
        {
            bridge.disconnect();
            bridges.remove(bridge);
        }
    }

    public void performCardJump(Lilipad current, Lilipad next)
    {
        ArrayList<Frog> frogs = current.getOccupants();
        if (!frogs.isEmpty())
        {
            Frog frog = frogs.get(0);
            frog.moveTo(next);
            next.addFrog(frog);
            current.removeFrog(frog);
        }
    }

    public void handleLilipadClick(Lilipad lili)
    {
        if (lili == null || gameOver)
        {
            return;
        }

        if (currentChainReaction != null)
        {
            if (!isCPU(currentChainReaction.displacedFrog.getPlayerNumber()))
            {
                handleDisplacedChoiceClick(lili);
            }
            return;
        }

        
        if (turnLogic.isPlacingBridge())
        {
            handleBridgePlacement(lili);
        }

        else
        {
            CardState currentState = cardStateManager.getState();
            switch (currentState)
            {
                case REMOVE_BRIDGE:
                    handleRemoveBridgeClick(lili);
                    break;
                case TWO_BRIDGE:
                    handleTwoBridgeCard(lili);
                    break;
                case EXTRA_JUMP:
                    handleExtraJumpCard(lili);
                    break;
                case PARACHUTE:
                    handleParachuteClick(lili);
                    break;
                case NONE:
                default:
                    handleDefaultClick(lili);
                    break;
            }
        }
    }
    
    private void handleDefaultClick(Lilipad lili)
    {
        if (selectedFrog == null)
        {
            if (!lili.getOccupants().isEmpty())
            {
                for (Frog frog : lili.getOccupants())
                {
                    if (isCurrentPlayersFrog(frog))
                    {
                        selectedFrog = frog;
                        frog.setSelected(true);
                        break;
                    }
                }
            }
        }
        else if (canMoveFrog(selectedFrog, lili))
        {
            handleFrogMovement(lili);

            if (selectedFrog != null)
            {
                selectedFrog.setSelected(false);
                selectedFrog = null;
            }
        }
    }

    private void handleRemoveBridgeClick(Lilipad lili)
    {
        CardStateData data = cardStateManager.getStateData(CardStateData.class);
        if (data == null)
        {
            data = new CardStateData();
            data.step = 0;
            cardStateManager.setState(CardState.REMOVE_BRIDGE, data);
        }
        
        if (data.step == 0)
        {
            data.firstPad = lili;
            data.step = 1;
        }

        else if (data.step == 1)
        {
            if (lili != data.firstPad)
            {
                if (findBridgeBetween(data.firstPad, lili) != null)
                {
                    getRemoveBridgeCard().useCard(this, null, data.firstPad, lili, null);
                }
                resetCardState();
            }
            else
            {
                resetCardState();
            }
        }
    }

    private void handleTwoBridgeCard(Lilipad targetPad)
    {
        CardStateData data = cardStateManager.getStateData(CardStateData.class);
        if (data == null)
        {
            data = new CardStateData();
            data.step = 0;
            cardStateManager.setState(CardState.TWO_BRIDGE, data);
        }
        
        if (data.step == 0)
        {
            data.firstPad = targetPad;
            data.step = 1;
        }
        
        else if (data.step == 1)
        {
            data.secondPad = targetPad;
            getTwoBridgeCard().useCard(this, null, data.firstPad, data.secondPad, null);
            resetCardState();
        }
    }

    private void handleExtraJumpCard(Lilipad targetPad)
    {
        CardStateData data = cardStateManager.getStateData(CardStateData.class);
        
        if (data == null)
        {
            data = new CardStateData();
            data.step = 0;
            cardStateManager.setState(CardState.EXTRA_JUMP, data);
        }
        
        switch (data.step)
        {
            case 0:
                if (!targetPad.getOccupants().isEmpty())
                {
                    Frog selectedFrog = targetPad.getFrogs().get(0);
                    
                    if (selectedFrog != null && selectedFrog.getPlayerNumber() == turnLogic.getCurrentPlayer())
                    {
                        data.current = targetPad;
                        data.step = 1;
                    }

                    else
                    {
                        CardHandler.showMessage(null, "You can only move your own frogs!", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
                    }
                }
                break;
            case 1:
                if (data.current != null)
                {
                    data.next = targetPad;
                    getExtraJumpCard().useCard(this, data.current, data.next, null, null);
                    resetCardState();
                }
                break;
        }
    }

    private void handleFrogMovement(Lilipad targetPad)
    {
        if (!turnLogic.canMakeMove() || selectedFrog == null)
        {
            return;
        }

        Lilipad currentPad = selectedFrog.getCurrentLilipad();

        if (currentPad == null || currentPad == targetPad)
        {
            return;
        }

        boolean moved = moveFrog(selectedFrog, targetPad);

        if (moved && currentChainReaction == null)
        {
            turnLogic.markMoveMade();
        }
    }

    public Lilipad getLilipadByIndex(int index)
    {
        if (index >= 0 && index < 26)
        {
            int gridY = index / 5 + 1;
            int gridX = index % 5 + 1;

            if(gridY >= 0 && gridY < GRID_SIZE && gridX >= 0 && gridX < GRID_SIZE)
            {
                return grid[gridY][gridX];
            }
            return null;
        }
        
        switch (index)
        {
            case FROG_START_LEFT:  return grid[3][0];
            case FROG_START_RIGHT: return grid[3][6];
            case FROG_START_TOP:   return grid[0][3];
            case FROG_START_BOTTOM:return grid[6][3];
            default:               return null;
        }
    }

    public boolean isGameOver()
    {
        return gameOver;
    }
    
    public boolean isPlayerFinished(int playerNumber)
    {
        return finishedPlayers.contains(playerNumber);
    }
    
    public ArrayList<Integer> getFinishedPlayers()
    {
        return new ArrayList<>(finishedPlayers);
    }
    
    public ArrayList<Frog> getPlayerFrogs(int playerNumber)
    {
        ArrayList<Frog> result = new ArrayList<>();

        for (Frog frog : playerFrogs)
        {
            if (frog.getPlayerNumber() == playerNumber)
            {
                result.add(frog);
            }
        }
        return result;
    }
    
    public Frog getFrogAt(Lilipad pad)
    {
        if (pad == null || pad.getFrogs().isEmpty())
        {
            return null;
        }
        return pad.getFrogs().get(0);
    }
    
    public int getStartPositionForPlayer(int playerNumber)
    {
        if (playerCount == 2)
        {
            return switch (playerNumber)
            {
                case 1 -> FROG_START_LEFT;
                case 2 -> FROG_START_RIGHT;
                default -> throw new IllegalArgumentException("Invalid player number for 2-player mode: " + playerNumber);
            };
        }
        else
        {
            return switch (playerNumber)
            {
                case 1 -> FROG_START_TOP;
                case 2 -> FROG_START_BOTTOM;
                case 3 -> FROG_START_LEFT;
                case 4 -> FROG_START_RIGHT;
                default -> throw new IllegalArgumentException("Invalid player number for 4-player mode: " + playerNumber);
            };
        }
    }
    
    public Lilipad[][] getGrid()
    {
        return grid;
    }

    public ArrayList<Bridge> getBridges()
    {
        return new ArrayList<>(bridges);
    }
    
    private void initializeCardState(CardState state, Consumer<CardStateData> initializer)
    {
        resetCardState();
        CardStateData data = new CardStateData();

        if (initializer != null)
        {
            initializer.accept(data);
        }
        cardStateManager.setState(state, data);
    }
    
    public void startRemoveBridge()
    {
        initializeCardState(CardState.REMOVE_BRIDGE, data -> data.step = 0);
    }
    
    public void startTwoBridge()
    {
        initializeCardState(CardState.TWO_BRIDGE, data -> data.step = 0);
    }
    
    public void startExtraJump()
    {
        initializeCardState(CardState.EXTRA_JUMP, data -> data.step = 0);
    }
    
    private boolean canMoveFrog(Frog frog, Lilipad target)
    {
        return frog != null && target != null && frog.getCurrentLilipad() != target && turnLogic.canMakeMove();
    }
    
    private boolean isCurrentPlayersFrog(Frog frog)
    {
        return frog != null && frog.getPlayerNumber() == turnLogic.getCurrentPlayer();
    }
    
    private static class CardStateData
    {
        Lilipad firstPad;
        Lilipad secondPad;
        int step;
        Lilipad current;
        Lilipad next;
    }
    
    public ExtraJumpCard getExtraJumpCard()
    { 
        return extraJumpCards.get(turnLogic.getCurrentPlayer()); 
    }
    public TwoBridgeCard getTwoBridgeCard()
    { 
        return twoBridgeCards.get(turnLogic.getCurrentPlayer()); 
    }
    public RemoveBridgeCard getRemoveBridgeCard()
    { 
        return removeBridgeCards.get(turnLogic.getCurrentPlayer()); 
    }
    public ParachuteCard getParachuteCard()
    { 
        return parachuteCards.get(turnLogic.getCurrentPlayer()); 
    }

    public ExtraJumpCard getExtraJumpCard(int playerNumber)
    { 
        return extraJumpCards.get(playerNumber); 
    }
    public TwoBridgeCard getTwoBridgeCard(int playerNumber)
    { 
        return twoBridgeCards.get(playerNumber); 
    }
    public RemoveBridgeCard getRemoveBridgeCard(int playerNumber)
    { 
        return removeBridgeCards.get(playerNumber); 
    }
    public ParachuteCard getParachuteCard(int playerNumber)
    { 
        return parachuteCards.get(playerNumber); 
    }
    
    public void startParachute()
    {
        if (turnLogic.hasMoved() || turnLogic.hasPlacedBridge())
        {
            return;
        }
        initializeCardState(CardState.PARACHUTE, null);
    }
    
    private void handleParachuteClick(Lilipad targetLilipad)
    {
        if (targetLilipad == null)
        {
            return;
        }
        
        if (selectedFrog == null)
        {
            if (!targetLilipad.getOccupants().isEmpty())
            {
                for (Frog frog : targetLilipad.getOccupants())
                {
                    if (isCurrentPlayersFrog(frog))
                    {
                        selectedFrog = frog;
                        frog.setSelected(true);
                        break;
                    }
                }
            }
        }

        else if (canMoveFrog(selectedFrog, targetLilipad))
        {
            if (targetLilipad != selectedFrog.getCurrentLilipad())
            {
                getParachuteCard().useCard(this, selectedFrog.getCurrentLilipad(), targetLilipad, null, null);
                resetCardState();
                selectedFrog.setSelected(false);
                selectedFrog = null;
            }
            else
            {
                selectedFrog.setSelected(false);
                selectedFrog = null;
            }
        }
    }
    
    public boolean checkWinCondition(int playerNumber)
    {
        if (gameOver || finishedPlayers.contains(playerNumber))
        {
            return false;
        }

        boolean hasWon = false;
        
        if (playerCount == 2)
        {
            int opponentNumber = (playerNumber % playerCount) + 1;
            Lilipad opponentHome = getLilipadByIndex(getStartPositionForPlayer(opponentNumber));
            
            if (opponentHome != null)
            {
                int frogsOnOpponentHome = 0;
                for (Frog frog : getPlayerFrogs(playerNumber))
                {
                    if (frog.getCurrentLilipad() == opponentHome)
                    {
                        frogsOnOpponentHome++;
                    }
                }
                
                hasWon = (frogsOnOpponentHome >= 3);
            }
        }
        else 
        {
            int[] homePads = new int[3];
            int index = 0;
            
            for (int i = 1; i <= 4; i++)
            {
                if (i != playerNumber)
                {
                    homePads[index++] = getStartPositionForPlayer(i);
                }
            }
            
            boolean[] homepadCovered = new boolean[3];
            int totalFrogsOnHomepads = 0;
            int playerHomePad = getStartPositionForPlayer(playerNumber);
            
            for (Frog frog : getPlayerFrogs(playerNumber))
            {
                Lilipad currentPad = frog.getCurrentLilipad();

                if (currentPad != null)
                {
                    int padIndex = currentPad.getIndexNumber();
                    
                    if (padIndex == playerHomePad)
                    {
                        continue;
                    }
                    
                    for (int i = 0; i < 3; i++)
                    {
                        if (padIndex == homePads[i])
                        {
                            if (!homepadCovered[i])
                            {
                                homepadCovered[i] = true;
                                totalFrogsOnHomepads++;
                            }
                            break;
                        }
                    }
                }
            }
            hasWon = (totalFrogsOnHomepads >= 3);
        }
        
        if (hasWon)
        {
            finishedPlayers.add(playerNumber);

            if (finishedPlayers.size() >= totalPlayers - 1)
            {
                gameOver = true;

                for (int i = 1; i <= totalPlayers; i++)
                {
                    if (!finishedPlayers.contains(i))
                    {
                        finishedPlayers.add(i);
                        break;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void restoreTurnState(boolean hasMoved, boolean hasPlacedBridge, boolean hasUsedParachute)
    {
        turnLogic.restoreTurnState(hasMoved, hasPlacedBridge, hasUsedParachute);
    }

    public void restoreFinishedPlayers(List<Integer> finishedPlayersList)
    {
        finishedPlayers.clear();
        finishedPlayers.addAll(finishedPlayersList);
        
        if (finishedPlayers.size() >= totalPlayers - 1)
        {
            gameOver = true;
        }
    }

    public void registerCPU(int playerNumber, String difficulty)
    {
        cpuPlayers.put(playerNumber, new CPUPlayer(playerNumber, difficulty, this));
    }


    public boolean isCPU(int playerNumber)
    {
        return cpuPlayers.containsKey(playerNumber);
    }

    public CPUPlayer getCPU(int playerNumber)
    {
        return cpuPlayers.get(playerNumber);
    }
}