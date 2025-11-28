import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CPUPlayer
{
    // Instance variables
    private int playerNumber;
    private String difficulty;
    private Random random;
    private GameLogic gameLogic;
    
    // Keep track of all CPU players by player number using a simple hashmap
    private static Map<Integer, CPUPlayer> cpuPlayers = new HashMap<>();
    
    // Simple check to see if a player is a CPU.
    public static boolean isCPU(int playerNumber)
    {
        return cpuPlayers.containsKey(playerNumber);
    }
    
    // Getter for the CPU player. Takes a player number for retrieval from the hashmap.
    public static CPUPlayer getCPU(int playerNumber)
    {
        return cpuPlayers.get(playerNumber);
    }
    
    // Memory to prevent immediate backtracking to help alleviate looping
    private Lilipad lastFrogPosition = null;
    private Frog lastMovedFrog = null;
    
    public CPUPlayer(int playerNumber, String difficulty, GameLogic gameLogic)
    {
        this.playerNumber = playerNumber;
        this.difficulty = difficulty;
        this.gameLogic = gameLogic;
        this.random = new Random();
        
        // Register this CPU player
        cpuPlayers.put(playerNumber, this);
    }
    
    // Simple call that can be made anywhere to move a CPU regardless of difficulty.
    public boolean makeMove()
    {
        if ("Hard".equals(difficulty))
        {
            return makeHardMove();
        }
        else
        {
            return makeEasyMove();
        }
    }
    
    // Easy mode movement rules by priority:
    // 1. Make a move towards the goal.
    // 2. Try a valid move that doesn't go back to the last position.
    // 3. Try to place a bridge.
    // 4. Cry. Weep.
    private boolean makeEasyMove()
    {
        // First, the CPU should try to make progress towards the goal
        if (tryMoveFrogTowardGoal(true))
        {
            return true;
        }
        
        // If no progress is possible, then the CPU will try any valid move that doesn't go back to the last position
        ArrayList<Frog> myFrogs = gameLogic.getPlayerFrogs(playerNumber);
        for (Frog frog : myFrogs)
        {
            if (!gameLogic.canMoveFrog(frog))
            {
                continue;
            }
            
            Lilipad currentPad = frog.getCurrentLilipad();

            if (currentPad == null) 
            {
                continue;
            }

            List<Lilipad> connectedPads = getConnectedPads(currentPad);

            for (Lilipad target : connectedPads)
            {
                // Skip invalid moves.
                if (!gameLogic.isValidMove(frog, target))
                {
                    continue;
                }

                // Skip moves that would go back to the last position.
                if (lastFrogPosition != null && target == lastFrogPosition)
                {
                    continue;
                }

                // Skip occupied lilipads that are not starter pads
                if (target.isOccupied() && !target.getIsStarterPad())
                {
                    continue;
                }
                
                // The CPU makes the move and remembers the position its moving from.
                updateMemory(frog, currentPad);

                return gameLogic.moveFrog(frog, target);
            }
        }
        
        // If there are no valid moves, then the CPU will try placing a random bridge
        if (gameLogic.getTurnLogic().canPlaceBridge())
        {
            return tryPlaceBridgeRandomly();
        }
        
        // Can't do anything. Shouldn't happen realistically, but better to have a safety net.
        return false;
    }
    
    // Hard mode movement rules by priority:
    // 1. If a frog can move to the goal, do it immediately.
    // 2. Move closer to the goal.
    // 3. Build a bridge towards the goal.
    // 4. Destroy bridges which are beneficial to opponents.
    // 5. Give up and cry.
    private boolean makeHardMove()
    {
        // First, the CPU should try to go to the goal if it's adjacent
        if (tryWinningMove())
        {
            return true;
        }

        // 2. If the CPU is not close enough to the goal, get closer.
        if (tryMoveFrogTowardGoal(true))
        {
            return true;
        }
        
        // 3. If the CPU needs a bridge to get closer to the goal, make it.
        if (gameLogic.getTurnLogic().canPlaceBridge())
        {
            if (trySmartBridgePlacement())
            {
                return true;
            }
        }

        // 4. The CPU will use the remove bridge card to try and hinder opponents.
        if (tryUseStrategicCard())
        {
            return true;
        }
        
        // 5. Fallback: Try any other valid move
        if (tryMoveFrogTowardGoal(false))
        {
            return true;
        }

        // 6. Weep in place (shouldn't happen)
        return false;
    }
    

    // Simply checks if any frogs are adjacent to opponent homepads.
    private boolean tryWinningMove()
    {
        ArrayList<Frog> myFrogs = gameLogic.getPlayerFrogs(playerNumber);
        List<Lilipad> goalPads = getGoalPads();

        for (Frog frog : myFrogs)
        {
            if (!gameLogic.canMoveFrog(frog))
            {
                continue;
            }

            Lilipad current = frog.getCurrentLilipad();
            if (current == null)
            {
                continue;
            }

            List<Lilipad> connectedPads = getConnectedPads(current);

            for (Lilipad target : connectedPads)
            {
                // If the target is one of our goals, a valid move, and not already occupied by our frog, go to it.
                if (goalPads.contains(target) && gameLogic.isValidMove(frog, target) && !wouldViolateHomepadRule(frog, target))
                {
                    updateMemory(frog, current);
                    return gameLogic.moveFrog(frog, target);
                }
            }
        }

        // If all else fails, move on.
        return false;
    }

    // Function to attempt to move a frog towards a goal.
    private boolean tryMoveFrogTowardGoal(boolean strictlyCloser)
    {
        ArrayList<Frog> myFrogs = gameLogic.getPlayerFrogs(playerNumber);
        List<Lilipad> goalPads = getGoalPads();
        
        // If in 4-player mode and we have unoccupied goals, prefer moving to them
        if (gameLogic.getPlayerCount() == 4)
        {
            // Get all unoccupied goals by using a couple filtered streams (still can't believe this worked)
            List<Lilipad> unoccupiedGoals = goalPads.stream().filter(goal -> !goal.getFrogs().stream()
                                                    .anyMatch(f -> f.getPlayerNumber() == playerNumber)).toList();

            if (!unoccupiedGoals.isEmpty())
            {
                // Try to find a path to any unoccupied goal
                for (Frog frog : myFrogs)
                {
                    if (!gameLogic.canMoveFrog(frog))
                    {
                        continue;
                    }

                    Lilipad currentPad = frog.getCurrentLilipad();

                    if (currentPad == null)
                    {
                        continue;
                    }
                    
                    for (Lilipad goal : unoccupiedGoals)
                    {
                        // Check if we can reach this unoccupied goal directly
                        List<Lilipad> path = findPathToGoal(frog, goal);
                        
                        if (!path.isEmpty())
                        {
                            // Move to the first step towards the goal
                            updateMemory(frog, currentPad);
                            boolean success = gameLogic.moveFrog(frog, path.get(0));
                            
                            if (success)
                            {
                                // If this started a chain reaction, process it
                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        // Shuffle CPU frogs to add variety so the CPU doesn't always move the same frog first.
        List<Frog> shuffledFrogs = new ArrayList<>(myFrogs);
        java.util.Collections.shuffle(shuffledFrogs, random);
        
        Lilipad bestMove = null;
        Frog bestFrog = null;

        double bestScore = -Double.MAX_VALUE; // Higher is better

        for (Frog frog : shuffledFrogs)
        {
            if (!gameLogic.canMoveFrog(frog))
            {
                continue;
            }
            
            Lilipad currentPad = frog.getCurrentLilipad();
            if (currentPad == null)
            {
                continue;
            }
            
            double currentDist = getMinDistanceToAnyGoal(currentPad, goalPads);
            List<Lilipad> connectedPads = getConnectedPads(currentPad);
            
            for (Lilipad target : connectedPads)
            {
                // Skip invalid moves
                if (!gameLogic.isValidMove(frog, target)) continue;
                
                // Check if moving to an occupied pad would start a chain reaction
                if (target.isOccupied() && !target.getIsStarterPad())
                {
                    // This will trigger a chain reaction, so check if the displaced frog has any valid moves
                    ArrayList<Lilipad> displacementOptions = gameLogic.getDisplacementOptions(currentPad, target);
                    
                    // If the displaced frog would have no valid moves, skip the chain reaction
                    if (displacementOptions.isEmpty())
                    {
                        continue;
                    }
                }
                
                // In 4-player mode, the frogs should avoid moving to already occupied homepads
                if (gameLogic.getPlayerCount() == 4 && target.getIsStarterPad())
                {
                    boolean isOccupied = target.getFrogs().stream()
                        .anyMatch(f -> f.getPlayerNumber() == playerNumber);
                    
                    if (isOccupied)
                    {
                        continue; // Skip already occupied homepads
                    }

                }
                
                else if (wouldViolateHomepadRule(frog, target))
                {
                    continue;
                }

                // Prevent loops if possible
                if (strictlyCloser && lastMovedFrog == frog && target == lastFrogPosition)
                {
                    continue; 
                }

                double newDist = getMinDistanceToAnyGoal(target, goalPads);
                double improvement = currentDist - newDist;
                
                // Calculate a score that considers both distance improvement and goal desirability
                double score = improvement;
                
                // Bonus for moving to an unoccupied goal
                if (target.getIsStarterPad())
                {
                    boolean isOccupied = target.getFrogs().stream()
                        .anyMatch(f -> f.getPlayerNumber() == playerNumber);
                    
                    if (!isOccupied)
                    {
                        score += 10.0;
                    }

                    else
                    {
                        score -= 5.0;
                    }

                }
                
                // Bonus for initiating chain reactions that might help us
                if (target.isOccupied() && !target.getIsStarterPad())
                {
                    // Slight bonus for chain reactions that might disrupt opponents
                    score += 2.0;
                }
                
                // Small random factor to add variety
                score += (random.nextDouble() * 0.5) - 0.25;
                
                if (strictlyCloser)
                {
                    // Must be strictly closer (improvement > 0)
                    if (improvement > 0.1 && score > bestScore)
                    {
                        bestScore = score;
                        bestMove = target;
                        bestFrog = frog;
                    }

                }
                
                else
                {
                    // Fallback: Use the score to determine best move
                    if (bestFrog == null || score > bestScore)
                    {
                        bestScore = score;
                        bestMove = target;
                        bestFrog = frog;
                    }
                }
            }
        }
        
        if (bestFrog != null && bestMove != null)
        {
            updateMemory(bestFrog, bestFrog.getCurrentLilipad());
            boolean success = gameLogic.moveFrog(bestFrog, bestMove);
            
            if (success)
            {
                // If this started a chain reaction, then process it.
                processCPUChainReactions();
                return true;
            }
        }
        
        // Failure
        return false;
    }
    
    // Places a bridge ONLY if it connects a frog to a spot closer to the goal.
    private boolean trySmartBridgePlacement()
    {
        ArrayList<Frog> myFrogs = gameLogic.getPlayerFrogs(playerNumber);
        Lilipad[][] grid = gameLogic.getGrid();
        List<Lilipad> goalPads = getGoalPads();
        
        for (Frog frog : myFrogs)
        {
            Lilipad currentPad = frog.getCurrentLilipad();
            if (currentPad == null)
            {
                continue;
            }
            
            double currentDist = getMinDistanceToAnyGoal(currentPad, goalPads);
            
            // Find adjacent lilipads without bridges
            List<Lilipad> adjacentPads = getAdjacentPads(currentPad, grid);
            
            for (Lilipad adjacent : adjacentPads)
            {
                // Check if a connection exists
                if (gameLogic.areConnectedByBridge(currentPad, adjacent))
                {
                    continue;
                }

                // Check if the destination is valid
                if (adjacent.isOccupied() && !adjacent.getIsStarterPad())
                {    
                    continue;
                }

                // Only build the bridge if the new pad is CLOSER to the goal
                double newDist = getMinDistanceToAnyGoal(adjacent, goalPads);
                
                if (newDist < currentDist)
                {
                    gameLogic.startBridgePlacement();
                    gameLogic.handleBridgePlacement(currentPad);
                    gameLogic.handleBridgePlacement(adjacent);
                    return true;
                }
            }
        }
        
        // Failure
        return false;
    }
    
    // Easy mode bridge placement. Very sad and pathetic.
    private boolean tryPlaceBridgeRandomly()
    {
        ArrayList<Frog> myFrogs = gameLogic.getPlayerFrogs(playerNumber);
        Lilipad[][] grid = gameLogic.getGrid();
        
        for (Frog frog : myFrogs)
        {
            Lilipad currentPad = frog.getCurrentLilipad();
            
            if (currentPad == null) 
            {
                continue;
            }

            List<Lilipad> adjacentPads = getAdjacentPads(currentPad, grid);

            for (Lilipad adjacent : adjacentPads)
            {
                if (!gameLogic.areConnectedByBridge(currentPad, adjacent) && (!adjacent.isOccupied() || adjacent.getIsStarterPad()))
                {
                    gameLogic.startBridgePlacement();
                    gameLogic.handleBridgePlacement(currentPad);
                    gameLogic.handleBridgePlacement(adjacent);
                    return true;
                }
            }
        }

        // Failure
        return false;
    }

    // Method to find a path to a goal using my behated breadth first search. Yay. I sure missed this from COMP 2002.
    private List<Lilipad> findPathToGoal(Frog frog, Lilipad goal)
    {
        if (frog == null || goal == null)
        {
            // Gotta return SOMETHING
            return new ArrayList<>();
        }
        
        Lilipad start = frog.getCurrentLilipad();

        if (start == null || start == goal) return new ArrayList<>();
        
        // Breadth first search setup. It's 11:37PM right now. Please help.
        java.util.Map<Lilipad, Lilipad> cameFrom = new java.util.HashMap<>();
        java.util.Queue<Lilipad> queue = new java.util.LinkedList<>();
        java.util.Set<Lilipad> visited = new java.util.HashSet<>();
        
        queue.add(start);
        visited.add(start);
        
        while (!queue.isEmpty())
        {
            Lilipad current = queue.poll();
            
            // If we've reached the goal, reconstruct the path
            if (current == goal)
            {
                List<Lilipad> path = new ArrayList<>();
                
                while (current != start)
                {
                    path.add(0, current);
                    current = cameFrom.get(current);
                }

                return path;
            }
            
            // Explore neighbors
            for (Lilipad neighbor : getConnectedPads(current))
            {
                if (!visited.contains(neighbor) &&  gameLogic.isValidMove(frog, neighbor) && !(neighbor.isOccupied() && 
                    !neighbor.getIsStarterPad()))
                {
                    // In 4-player mode, avoid already occupied homepads (unless it's the goal)
                    if (gameLogic.getPlayerCount() == 4 && neighbor.getIsStarterPad() && neighbor != goal)
                    {
                        boolean isOccupied = neighbor.getFrogs().stream().anyMatch(f -> f.getPlayerNumber() == playerNumber);
                        if (isOccupied)
                        {
                            continue; // Skip already occupied homepads
                        }
                    }
                    
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);

                }
            }
        }
        
        return new ArrayList<>(); // No path found, needs a return
    }
    
    // Uses the remove bridge card to cause trouble for players
    private boolean tryUseStrategicCard()
    {
        if (!gameLogic.getRemoveBridgeCard(playerNumber).isUsed())
        {
            Bridge bridgeToRemove = findOpponentBridgeToRemove();
            
            if (bridgeToRemove != null && bridgeToRemove.getConnections().size() == 2)
            {
                gameLogic.startRemoveBridge();
                gameLogic.getRemoveBridgeCard(playerNumber).useCard(gameLogic, null, bridgeToRemove.getConnections().get(0),
                bridgeToRemove.getConnections().get(1), null);
                return true;
            }
        }

        // Failure
        return false;
    }

    // Simple setter to update the memory variables    
    private void updateMemory(Frog frog, Lilipad from) {
        this.lastMovedFrog = frog;
        this.lastFrogPosition = from;
    }

    // Mostly used for scoring, but used for other things too. Gets minimum distance to goal.
    private double getMinDistanceToAnyGoal(Lilipad from, List<Lilipad> goals)
    {
        // Huge, equivalent defaults to keep things consistent.
        double minDistance = Double.MAX_VALUE;
        double minOccupiedDistance = Double.MAX_VALUE;
        
        for (Lilipad goal : goals)
        {
            if (goal == null) 
            {
                continue;
            }
            
            double distance = calculateDistance(from, goal);
            boolean isOccupied = goal.isOccupied() && goal.getFrogs().stream().anyMatch(f -> f.getPlayerNumber() == playerNumber);
                
            if (isOccupied)
            {
                // Keep track of the closest occupied goal, but prefer unoccupied ones
                if (distance < minOccupiedDistance)
                {
                    minOccupiedDistance = distance;
                }
            }
            
            else
            {
                // Prefer unoccupied goals
                if (distance < minDistance)
                {
                    minDistance = distance;
                }
            }
        }
        
        // If we have unoccupied goals, use their distance
        if (minDistance < Double.MAX_VALUE)
        {
            return minDistance * 0.9; // Slight bias towards unoccupied goals
        }
        
        // Fall back to occupied goals if no unoccupied ones are available
        return minOccupiedDistance;
    }

    // Method to find goals, particularly for 4 player matches.
    private List<Lilipad> getGoalPads()
    {
        List<Lilipad> allGoals = new ArrayList<>();
        List<Lilipad> unoccupiedGoals = new ArrayList<>();
        Lilipad[][] grid = gameLogic.getGrid();
        
        // Define goal indices based on player count
        List<Integer> goalIndices = new ArrayList<>();
        
        // CPU Opponent will always be player 1 in 2 player mode.
        if (gameLogic.getPlayerCount() == 2)
        {
            goalIndices.add(getStartPositionForPlayer(1));
        }
        else
        {
            // In 4-player mode, add all opponents' homepads as potential goals
            for (int i = 1; i <= 4; i++)
            {
                if (i != playerNumber)
                {
                    goalIndices.add(getStartPositionForPlayer(i));
                }
            }
        }
        
        // Find all goal pads and separate the occupied ones from the unoccupied ones
        for (int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[row].length; col++)
            {
                if (grid[row][col] != null)
                {
                    for (int idx : goalIndices)
                    {
                        if (grid[row][col].getIndexNumber() == idx)
                        {
                            allGoals.add(grid[row][col]);
                            
                            // Check if this goal is already occupied by a CPU frog
                            boolean isOccupied = grid[row][col].getFrogs().stream().anyMatch(f -> f.getPlayerNumber() == playerNumber);
                            
                            if (!isOccupied)
                            {
                                unoccupiedGoals.add(grid[row][col]);
                            }
                        }
                    }
                }
            }
        }

        if(gameLogic.getPlayerCount() == 4 && !unoccupiedGoals.isEmpty())
        {
            return unoccupiedGoals;
        }
        
        else
        {
            return allGoals;
        }
    }
    
    // Helper method to return a list of connected lilipads from a given lilipad
    private List<Lilipad> getConnectedPads(Lilipad pad)
    {
        List<Lilipad> connected = new ArrayList<>();
        for (Bridge bridge : pad.getBridges())
        {
            for (Lilipad lili : bridge.getConnections())
            {
                if (lili != pad && !connected.contains(lili))
                {
                    connected.add(lili);
                }
            }
        }
        return connected;
    }
    
    // Math function for calculating the distance between two lilipads on the gameboard. Uses pythagorean theorem.
    private double calculateDistance(Lilipad pad1, Lilipad pad2)
    {
        Lilipad[][] grid = gameLogic.getGrid();
        int[] pos1 = findPosition(pad1, grid);
        int[] pos2 = findPosition(pad2, grid);
        
        if (pos1 == null || pos2 == null)
        {
            return Double.MAX_VALUE;
        }
        
        int dx = pos1[0] - pos2[0];
        int dy = pos1[1] - pos2[1];

        // c = sqrt(a^2 + b^2)
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    // Helper function to find the position of a given lilipad on a given grid of lilipads.
    private int[] findPosition(Lilipad pad, Lilipad[][] grid)
    {
        for (int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[row].length; col++)
            {
                if (grid[row][col] == pad)
                {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }
    
    // Helper function to get the adjacent pads of a lilipad, regardless of bridges
    private List<Lilipad> getAdjacentPads(Lilipad pad, Lilipad[][] grid)
    {
        List<Lilipad> adjacent = new ArrayList<>();
        int[] pos = findPosition(pad, grid);

        if (pos == null)
        {
            return adjacent;
        }

        int row = pos[0];
        int col = pos[1];
        
        // Simple coordinate vectors
        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        
        for (int[] d : dirs)
        {
            int r = row + d[0];
            int c = col + d[1];
            
            if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length)
            {
                if (grid[r][c] != null && grid[r][c].getIndexNumber() != 999)
                {
                    adjacent.add(grid[r][c]);
                }
            }
        }
        return adjacent;
    }
    

    // Function to check if moving to the target would violate the one-frog-per-homepad rule in 4-player mode
    private boolean wouldViolateHomepadRule(Frog frog, Lilipad target)
    {
        if (gameLogic.getPlayerCount() != 4)
        {
            return false;
        }
        
        // Only check if the target is an opponent's homepad
        if (!isOpponentHomepad(target, frog.getPlayerNumber()))
        {
            return false;
        }
        
        // Check if we already have a frog on this homepad
        for (Frog f : target.getFrogs())
        {
            if (f.getPlayerNumber() == frog.getPlayerNumber() && f != frog)
            {
                return true;
            }
        }
        return false;
    }
    
    // Method to check if a lilipad is an opponent's homepad for the given player
    private boolean isOpponentHomepad(Lilipad lili, int playerNumber)
    {
        if (lili == null || !lili.getIsStarterPad())
        {
            return false;
        }
        
        // Check if this is any opponent's homepad
        for (int i = 1; i <= gameLogic.getPlayerCount(); i++)
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
    
    // Method to help hard cpu's find bridges to remove to hinder their opponents
    private Bridge findOpponentBridgeToRemove()
    {
        ArrayList<Bridge> bridges = gameLogic.getBridges();
        Bridge bestBridge = null;
        int bestScore = -1;
        
        ArrayList<Frog> opponentFrogs = new ArrayList<>();

        for (int opponent = 1; opponent <= gameLogic.getPlayerCount(); opponent++)
        {
            if (opponent != playerNumber)
            {
                opponentFrogs.addAll(gameLogic.getPlayerFrogs(opponent));
            }
        }
        
        // Find the best bridge to remove
        for (Bridge bridge : bridges)
        {
            if (bridge.isPermanent())
            {
                continue;
            }
            
            Lilipad pad1 = bridge.getConnections().get(0);
            Lilipad pad2 = bridge.getConnections().get(1);
            
            // Skip if either end is a home pad
            if ((pad1 != null && pad1.getIsStarterPad()) || (pad2 != null && pad2.getIsStarterPad()))
            {
                continue;
            }
            
            // Calculate a score for this bridge based on how many opponent frogs are using it
            int score = 0;
            
            // Check if this bridge is currently being used by any opponent frogs
            for (Frog frog : opponentFrogs)
            {
                Lilipad currentPad = frog.getCurrentLilipad();
                if (currentPad == null)
                {
                    continue;
                }
                // Check if this frog is on either end of the bridge
                if ((pad1 != null && currentPad.equals(pad1)) || (pad2 != null && currentPad.equals(pad2)))
                {
                    // Higher score for bridges that are currently being used
                    score += 2;
                    
                    // Additional score if the frog is close to winning
                    List<Lilipad> goals = getGoalPads();
                    if (!goals.isEmpty())
                    {
                        double dist = getMinDistanceToAnyGoal(currentPad, goals);
                        score += (10 - Math.min(10, (int)dist)) * 2; // The closer the frog is to a goal, the higher the score
                    }
                }
            }
            
            // Add some randomness to avoid always choosing the same bridge
            score += random.nextInt(3);
            
            // If this is the best bridge so far, remember it
            if (score > bestScore)
            {
                bestScore = score;
                bestBridge = bridge;
            }
        }
        
        return bestBridge;
    }
    
    private int getStartPositionForPlayer(int playerNum)
    {
        if (gameLogic.getPlayerCount() == 2)
        {
            return playerNum == 1 ? 100 : 101; 
        }
       
        else
        {
            return switch (playerNum)
            {
                case 1 -> 102; 
                case 2 -> 103; 
                case 3 -> 100; 
                case 4 -> 101; 
                default -> 100;
            };
        }
    }
    // Method to tell a CPU player where to jump when it enters a chain.
    public Lilipad chooseDisplacementOption(List<Lilipad> options)
    {
        if (options.isEmpty())
        {
            return null;
        }

        List<Lilipad> myGoalPads = getGoalPads();

        for (Lilipad option : options)
        {
            if (myGoalPads.contains(option) && !option.isOccupied())
            {
                return option; // Always choose an unoccupied goal if it's available
            }
        }
        
        // In easy mode, prefer empty pads to avoid starting chain reactions
        if ("Easy".equals(difficulty))
        {
            List<Lilipad> emptyPads = options.stream().filter(pad -> !pad.isOccupied()).toList();
            if (!emptyPads.isEmpty())
            {
                return emptyPads.get(random.nextInt(emptyPads.size()));
            }

            return options.get(random.nextInt(options.size()));
        }
        
        // Hard mode: choose option that's either closest to a goal or disrupts opponents
        double bestScore = -Double.MAX_VALUE;
        Lilipad bestOption = options.get(0); // Default to the first option
        
        for (Lilipad option : options)
        {
            double score = 0.0;
            
            double distToGoal = getMinDistanceToAnyGoal(option, myGoalPads);
            score -= distToGoal * 2;
            
            if (!option.isOccupied())
            {
                score += 1.0;
            }
            
            // If this is a goal pad (but already occupied by us), it's still an okay choice
            if (myGoalPads.contains(option))
            {
                score += 3.0;
            }
            
            // If this is an opponent's home pad, it's a great choice
            if (option.getIsStarterPad() && option.getFrogs().stream().noneMatch(f -> f.getPlayerNumber() == playerNumber))
            {
                score += 5.0;
            }
            
            if (score > bestScore)
            {
                bestScore = score;
                bestOption = option;
            }
        }
        
        return bestOption;
    }

    private void processCPUChainReactions()
    {
        // CPU logic should only process one chain reaction at a time to prevent infinite loops
        if (gameLogic.getCurrentChainReaction() != null)
        {
            Frog displacedFrog = gameLogic.getCurrentChainReaction().displacedFrog;
            int displacedPlayerNumber = displacedFrog.getPlayerNumber();
            
            // If it's not a CPU, stop and wait for input.
            if (!CPUPlayer.isCPU(displacedPlayerNumber))
            {
                return;
            }
            
            // Get the CPU player that needs to respond
            CPUPlayer cpu = CPUPlayer.getCPU(displacedPlayerNumber);
            
            if (cpu == null)
            {
                // Fallback in case something goes wrong
                gameLogic.handleDisplacedChoiceClick(gameLogic.getCurrentChainReaction().options.get(0));
                return;
            }
            
            // Let the CPU choose the best option
            Lilipad choice = cpu.chooseDisplacementOption(gameLogic.getCurrentChainReaction().options);
            
            if (choice != null)
            {
                gameLogic.handleDisplacedChoiceClick(choice);
            }
            
            else
            {
                // If no valid choice (shouldn't happen), pick the first option
                gameLogic.handleDisplacedChoiceClick(gameLogic.getCurrentChainReaction().options.get(0));
            }
            
            // Small delay to make the game play more naturally
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}