import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class GameFrame extends JPanel implements ActionListener {
    public static final String SCREEN_NAME = "GameFrame";
    private static final int GRID_SIZE = 7;
    private static final int GAP_SIZE = 15;
    
    private GameLogic gameLogic;
    private Color color;
    private JButton[] cardButtons;
    private JButton gameSaveExit, gameExit, endTurnButton, colorblindButton;
    private boolean colorblindMode = false;
    private JPanel gridPanel;
    private JLabel turnLabel;
    private final SettingsMenu settingsMenu;

    // Constructor
    public GameFrame(SettingsMenu settingsMenu)
    {
        setLayout(new BorderLayout());
    
        this.settingsMenu = settingsMenu;

        // Initialize game color based on settings
        String settings = settingsMenu.returnSettings();
        this.color = determineColor(settings.charAt(3));
        
        // Initialize game logic with color and player count
        gameLogic = new GameLogic(color, Character.getNumericValue(settings.charAt(0)));
        
        // Initialize CPU players if setting is enabled
        if (settingsMenu.computerPlayer) 
        {
            for (int i = 2; i <= gameLogic.getPlayerCount(); i++)
            {
                gameLogic.registerCPU(i, settingsMenu.difficulty);
            }
        }

        // Set up the game UI
        setupGame();
        
        // Force a repaint to ensure everything is displayed correctly
        revalidate();
        repaint();
    }

    // Load game constructor for GameFrame
    public GameFrame(SettingsMenu settingsMenu, GameLogic restoredLogic)
    {
        setLayout(new BorderLayout());
        
        this.settingsMenu = settingsMenu;
        this.color = settingsMenu.color;
        this.gameLogic = restoredLogic;

        if (settingsMenu.computerPlayer)
        {
            for (int i = 2; i <= gameLogic.getPlayerCount(); i++)
            {
                if (!gameLogic.isCPU(i))
                {
                    gameLogic.registerCPU(i, settingsMenu.difficulty);
                }
            }
        }

        // Set up the game UI with the restored state.
        setupGame();

        // Clean up for paint components.
        validate();
        repaint();
    }
    
    private void setupGame()
    {
        removeAll();
        
        gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, GAP_SIZE, GAP_SIZE))
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                
                // Draw bridges first (behind lilipads)
                for (Bridge bridge : gameLogic.getBridges())
                {      
                    if (bridge.getConnections().size() == 2)
                    {
                        Lilipad lili1 = bridge.getConnections().get(0);
                        Lilipad lili2 = bridge.getConnections().get(1);

                        if (lili1.getParent() == this && lili2.getParent() == this)
                        {
                            Point p1 = lili1.getLocation();
                            p1.x += lili1.getWidth() / 2;
                            p1.y += lili1.getHeight() / 2;
                            
                            Point p2 = lili2.getLocation();
                            p2.x += lili2.getWidth() / 2;
                            p2.y += lili2.getHeight() / 2;

                            g2.setColor(bridge.getBridgeColor());
                            g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                        }
                    }
                }
                g2.dispose();
            }
        };
        
        // Add all lilipads to the grid panel
        Lilipad[][] grid = gameLogic.getGrid();
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int col = 0; col < GRID_SIZE; col++)
            {
                if (grid[row][col] != null)
                {
                    Lilipad lilipad = grid[row][col];
                    lilipad.addActionListener(this);
                    lilipad.setOpaque(true);
                    lilipad.setBorderPainted(false);
                    
                    Dimension minSize = new Dimension(40, 40);
                    lilipad.setMinimumSize(minSize);
                    lilipad.setPreferredSize(minSize);
                    
                    gridPanel.add(lilipad);
                }
                
                else
                {
                    JPanel emptyPanel = new JPanel();
                    emptyPanel.setOpaque(false);
                    gridPanel.add(emptyPanel);
                }
            }
        }

        JPanel gameTopPanel = createTopPanel();
        JPanel gameBotPanel = createBottomPanel();
        
        // Add a colorblind mode button to top panel
        colorblindButton = new JButton("Colorblind: OFF");
        colorblindButton.addActionListener(e -> toggleColorblindMode());
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);
        topRightPanel.add(colorblindButton);
        
        JPanel topPanelWrapper = new JPanel(new BorderLayout());
        topPanelWrapper.add(gameTopPanel, BorderLayout.CENTER);
        topPanelWrapper.add(topRightPanel, BorderLayout.EAST);
        
        // Assemble the UI
        add(topPanelWrapper, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(gameBotPanel, BorderLayout.SOUTH);
        
        // Ensure the grid panel is properly updated
        gridPanel.revalidate();
        gridPanel.repaint();
        
        setSize(800, 600);
    }

    private void resetBoardState()
    {
        gameLogic.resetBoardState();
        setupGame();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if (source instanceof Lilipad)
        {
            gameLogic.handleLilipadClick((Lilipad) source);
            repaint();
            return;
        }
        
        if (source == cardButtons[0])
        {
            CardHandler.handleExtraJump(gameLogic, this);
        }
        else if (source == cardButtons[1])
        {
            CardHandler.handleParachute(gameLogic, this);
        }
        else if (source == cardButtons[2])
        {
            CardHandler.handleTwoBridge(gameLogic, this);
        } 
        else if (source == cardButtons[3])
        {
            CardHandler.handleRemoveBridge(gameLogic, this);
        }
        else if (source == endTurnButton)
        {
            endTurn();
        }
        else if (source == gameSaveExit)
        {
            handleSaveAndExit();
        }
        else if (source == gameExit)
        {
            handleExit();
        }
    }

    private void handleExit()
    {
        resetBoardState();
        BaseFrame.showScreen(TitleScreen.SCREEN_NAME);
    }
    
    private void handleSaveAndExit()
    {
        GameState state = SaveManager.createGameState(settingsMenu, gameLogic);
        boolean success = SaveManager.saveGame(state);
        
        if (success)
        {
            JOptionPane.showMessageDialog(this, 
                "Game saved successfully!", 
                "Save Game", 
                JOptionPane.INFORMATION_MESSAGE);
            resetBoardState();
            BaseFrame.showScreen(TitleScreen.SCREEN_NAME);
        }
        
        else
        {
            JOptionPane.showMessageDialog(this, 
                "Failed to save game. Please try again.", 
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void endTurn()
    {
        TurnLogic turnLogic = gameLogic.getTurnLogic();
        int currentPlayer = turnLogic.getCurrentPlayer();
        int placement = gameLogic.getFinishedPlayers().size() + 1;

        if (gameLogic.checkWinCondition(currentPlayer))
        {
            showPlayerFinished(currentPlayer, placement);
        }
        
        if (gameLogic.isGameOver())
        {
            showGameOver();
            return;
        }
        
        do
        {
            turnLogic.nextTurn();
            currentPlayer = turnLogic.getCurrentPlayer();
        } while (gameLogic.isPlayerFinished(currentPlayer) && !gameLogic.isGameOver());
        
        gameLogic.resetCardState();
        updateTurnDisplay();
        
        if (gameLogic.isGameOver())
        {
            showGameOver();
        }

        if (gameLogic.isCPU(currentPlayer))
        {
            handleCPUTurn(currentPlayer);
        }
    }

    private void updateTurnDisplay()
    {
        int currentPlayer = gameLogic.getTurnLogic().getCurrentPlayer();
        turnLabel.setText("Player " + currentPlayer + "'s Turn");
        turnLabel.setForeground(gameLogic.getTurnLogic().getCurrentPlayerColor());
    }
    
    private void showPlayerFinished(int playerNumber, int placement)
    {
        String message = String.format("Player %d has finished in %s place!", playerNumber, getPlacementText(placement));
        JOptionPane.showMessageDialog(this, message, "Player Finished!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String getPlacementText(int placement)
    {
        return switch (placement)
        {
            case 1 -> "1st";
            case 2 -> "2nd";
            case 3 -> "3rd";
            default -> placement + "th";
        };
    }
    
    private void showGameOver()
    {
        StringBuilder message = new StringBuilder("Game Over!\n\nFinal Standings:\n");
        List<Integer> finishedPlayers = gameLogic.getFinishedPlayers();
        
        for (int i = 0; i < finishedPlayers.size(); i++)
        {
            int player = finishedPlayers.get(i);
            message.append(getPlacementText(i + 1)).append(": Player ").append(player).append("\n");
        }
        
        JOptionPane.showMessageDialog(this, message.toString(), "Game Over!", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Turn label to show current player
        turnLabel = new JLabel("Player " + gameLogic.getTurnLogic().getCurrentPlayer() + "'s Turn");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        updateTurnDisplay();
        
        // Initialize and configure buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton placeBridgeButton = new JButton("Place Bridge");
        placeBridgeButton.addActionListener(e -> handlePlaceBridge());
        
        endTurnButton = new JButton("End Turn");
        endTurnButton.addActionListener(e -> endTurn());
        
        gameSaveExit = new JButton("Save and Quit");
        gameSaveExit.addActionListener(this);
        
        gameExit = new JButton("Quit");
        gameExit.addActionListener(this);
        
        buttonPanel.add(placeBridgeButton);
        buttonPanel.add(endTurnButton);
        buttonPanel.add(gameSaveExit);
        buttonPanel.add(gameExit);
        
        panel.add(turnLabel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void handlePlaceBridge()
    {
        if (gameLogic.startBridgePlacement())
        {
            JOptionPane.showMessageDialog(this, "Select two adjacent lilipads to place a bridge between them.");
        }
        else
        {
            JOptionPane.showMessageDialog(this, "You can only place one bridge per turn, and you can't have already moved.");
        }
    }

    private JPanel createBottomPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel cardsPanel = new JPanel(new FlowLayout());
        
        // Initialize card buttons
        String[] cardNames = {"Extra Jump", "Parachute", "Extra Bridge", "Bridge Removal"};
        cardButtons = new JButton[cardNames.length];
        
        for (int i = 0; i < cardNames.length; i++)
        {
            cardButtons[i] = new JButton(cardNames[i]);
            cardButtons[i].addActionListener(this);
            cardsPanel.add(cardButtons[i]);
        }
        
        panel.add(cardsPanel, BorderLayout.WEST);
        return panel;
    }

    private void handleCPUTurn(int playerNumber)
    {
        setUIEnabled(false);
        
        Timer cpuTimer = new Timer(1000, e ->
        {
            CPUPlayer cpu = gameLogic.getCPU(playerNumber);
            
            if (cpu != null)
            {
                @SuppressWarnings("unused") // Stop annoying me about it IDE it's important
                boolean moveMade = cpu.makeMove();
                
                gridPanel.repaint();
                repaint();

                setUIEnabled(true);
                
                endTurn();
            }
        });
        
        cpuTimer.setRepeats(false);
        cpuTimer.start();
    }

    private void setUIEnabled(boolean enabled)
    {
        if (endTurnButton != null)
        {
            endTurnButton.setEnabled(enabled);
        }
        if (gameSaveExit != null)
        {
            gameSaveExit.setEnabled(enabled);
        }
        if (cardButtons != null)
        {
            for (JButton btn : cardButtons)
            {
                if (btn != null)
                {
                    btn.setEnabled(enabled);
                }
            }
        }
    }

    public static Color determineColor(char colorCode)
    {
        return switch (colorCode)
        {
            case 'W' -> Color.WHITE;
            case 'R' -> Color.RED;
            case 'B' -> Color.BLUE;
            case 'Y' -> Color.YELLOW;
            case 'O' -> new Color(255, 100, 0);
            case 'P' -> new Color(180, 0, 255);
            case 'b' -> Color.BLACK;
            default -> Color.GREEN;
        };
    }

    public void toggleColorblindMode()
    {
        colorblindMode = !colorblindMode;
        colorblindButton.setText(colorblindMode ? "Colorblind: ON" : "Colorblind: OFF");
        repaint();
    }
    
    public boolean isColorblindMode()
    {
        return colorblindMode;
    }
}