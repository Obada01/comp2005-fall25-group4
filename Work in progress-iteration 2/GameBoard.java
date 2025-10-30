import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoard extends JFrame implements ActionListener
{
    private Color color;
    private ArrayList<Integer> playerOrder;
    private Lilipad[][] grid;
    private ArrayList<Lilipad> possibleConnections;
    private boolean gameOver;
    private int playerAmount = 2;
    private boolean computerPlayer, setDone, next0Done, next1Done, next2Done, next3Done, next4Done, tutLoadDone;
    private String difficulty;
    private JButton newGame, loadGame, settings, exit, next0, back0, next1, back1, p2, p4, next2, back2, comY, comN, next3, back3, difE, next4, back4, difH, col0, col1, col2, col3, col4, col5, col6, col7, next5, back5, tutY, tutDone, leaveTut, card0, card1, card2, card3, gameSaveExit, gameExit;
    private JPanel startTopPanel, startMidPanel, startWrapPanel, next0Wrap, back0Wrap, setAskWrap, setTopPanel, setMidPanel, pTopPanel, pMidPanel, next1Wrap, back1Wrap, pAskWrap, comTopPanel, comMidPanel, next2Wrap, back2Wrap, comAskWrap, difTopPanel, difMidPanel, next3Wrap, back3Wrap, difAskWrap, colTopPanel, colMidPanel, next4Wrap, back4Wrap, colAskWrap, tutTopPanel, tutMidPanel, next5Wrap, back5Wrap, tutAskWrap, objectOfWrap, turnInfoGrid, tutTextMidPanel, cardsGrid, leaveTutWrap, tutTextBotPanel, gridPanel;
    private JLabel titleText, setAsk, playerDis, comDis, difDis, colDis, pAsk, comAsk, difAsk, colAsk, tutAsk, objectOf, turnInfoLabel, frogTurnInfo, bridgeTurnInfo, cardTurnInfo, DJcardInfo, PRcardInfo, EBcardInfo, BRcardInfo;
    
    /**
     * Constructor for objects of class GameBoard
     */
    public GameBoard()
    {
        playerOrder = new ArrayList<Integer>();
        possibleConnections = new ArrayList<Lilipad>();
        this.color = new Color(0,255,0);
        this.gameOver = gameOver;
        this.playerAmount = 2;
        this.computerPlayer = true;
        this.difficulty = "Easy";
        this.setDone = false;
        this.next0Done = false;
        this.next1Done = false;
        this.next2Done = false;
        this.next3Done = false;
        this.next4Done = false;
        this.tutLoadDone = false;
        
        startTopPanel = new JPanel(new BorderLayout());
        startMidPanel = new JPanel();
        startMidPanel.setLayout(new BoxLayout(startMidPanel, BoxLayout.Y_AXIS));
        
        newGame = new JButton("New Game");
        newGame.addActionListener(this);
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton loadGame = new JButton("Load Game");
        loadGame.addActionListener(this);
        loadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        settings = new JButton("Settings");
        settings.addActionListener(this);
        settings.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit = new JButton("Exit");
        exit.addActionListener(this);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titleText = new JLabel("Hooop!");
        titleText.setSize(800,800);
        titleText.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
        startWrapPanel = new JPanel(new FlowLayout());
        startWrapPanel.add(titleText);
        
        startTopPanel.add(startWrapPanel);
        startMidPanel.add(newGame);
        startMidPanel.add(loadGame);
        startMidPanel.add(settings);
        startMidPanel.add(exit);
        pack();
        setLocation(500,300);
        setSize(800,600);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(startTopPanel, BorderLayout.NORTH);
        getContentPane().add(startMidPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }
    
    public void setupGame()
    {
        getContentPane().removeAll();
        
        gridPanel = new JPanel(new GridLayout(7,7));
        
        grid = new Lilipad[7][7];
        Color filler = new Color(0,100,255);
        
        int idOffset = 0;
        boolean skipFill = false;
        for (int row = 0; row < 7; row++)
        {
            for (int colm = 0; colm < 7; colm++)
            {
                if (row == 0)
                {
                    if ((colm == 3)&&(playerAmount == 4))
                    {
                        grid [row][colm] = new Lilipad(color, (102), true);
                        grid[row][colm].setSize(40,40);
                        gridPanel.add(grid[row][colm]);
                        //System.out.println("added home2 at " + row + " " + colm); Debugging printers. Don't remove
                        skipFill = true;
                    }
                    if (skipFill == false)
                    {
                        grid [row][colm] = new Lilipad(filler, (0), false);
                        grid[row][colm].setSize(40,40);
                        gridPanel.add(grid[row][colm]);
                        //System.out.println("added filler at " + row + " " + colm);
                    }
                    skipFill = false;
                }
                if ((row != 0)&&(row != 6))
                {
                    if ((colm != 0)&&(colm != 6))
                    {
                        grid[row][colm] = new Lilipad(color, (colm + idOffset), false);
                        grid[row][colm].setSize(40,40);
                        gridPanel.add(grid[row][colm]);
                        //System.out.println("added lilipad " + grid[row][colm].getIndexNumber() + " at " + row + " " + colm);
                    }
                    if ((colm == 0)||(colm == 6))
                    {
                        if ((row == 3)&&(colm == 0))
                        {
                            grid [row][colm] = new Lilipad(color, (100), true);
                            grid[row][colm].setSize(40,40);
                            gridPanel.add(grid[row][colm]);
                            //System.out.println("added home0 at " + row + " " + colm);
                            skipFill = true;
                        }
                        if ((row == 3)&&(colm == 6))
                        {
                            grid [row][colm] = new Lilipad(color, (101), true);
                            grid[row][colm].setSize(40,40);
                            gridPanel.add(grid[row][colm]);
                            //System.out.println("added home1 at " + row + " " + colm);
                            skipFill = true;
                        }
                        if (skipFill == false)
                        {
                            grid [row][colm] = new Lilipad(filler, (0), false);
                            grid[row][colm].setSize(40,40);
                            gridPanel.add(grid[row][colm]);
                            //System.out.println("added filler at " + row + " " + colm);
                        }
                        skipFill = false;
                    }
                }
                if (row == 6)
                {
                    if ((colm == 3)&&(playerAmount == 4))
                    {
                        grid [row][colm] = new Lilipad(color, (103), true);
                        grid[row][colm].setSize(40,40);
                        gridPanel.add(grid[row][colm]);
                        //System.out.println("added home3 at " + row + " " + colm);
                        skipFill = true;
                    }
                    if (skipFill == false)
                    {
                        grid [row][colm] = new Lilipad(filler, (0), false);
                        grid[row][colm].setSize(40,40);
                        gridPanel.add(grid[row][colm]);
                        //System.out.println("added filler at " + row + " " + colm);
                    }
                    skipFill = false;
                }
            }
            if ((row != 0)&&(row != 6))
            {
                idOffset += 5;
            }
        }
        
        JPanel gameTopPanel = new JPanel(new BorderLayout());
        JLabel turnLabel = new JLabel("Player X's turn"); //Should change with turn order. To be properly implemented later
        gameSaveExit = new JButton("Save and Quit");
        gameSaveExit.addActionListener(this);
        gameExit = new JButton("Quit");
        gameExit.addActionListener(this);
        JPanel exitButtons = new JPanel(new FlowLayout());
        exitButtons.add(gameSaveExit);
        exitButtons.add(gameExit);
        
        JPanel gameBotPanel = new JPanel(new BorderLayout());
        JPanel cardsPanel = new JPanel(new FlowLayout());
        card0 = new JButton("Extra Jump");
        cardsPanel.add(card0);
        card1 = new JButton("Parachute");
        cardsPanel.add(card1);
        card2 = new JButton("Extra Bridge");
        cardsPanel.add(card2);
        card3 = new JButton("Bridge Removal");
        cardsPanel.add(card3);
        
        gameTopPanel.add(turnLabel, BorderLayout.WEST);
        gameTopPanel.add(exitButtons, BorderLayout.EAST);
        gameBotPanel.add(cardsPanel, BorderLayout.WEST);
                
        getContentPane().add(gameTopPanel, BorderLayout.NORTH);
        getContentPane().add(gridPanel, BorderLayout.CENTER);
        getContentPane().add(gameBotPanel, BorderLayout.SOUTH);
        
        pack();
        setSize(800,600);
    }
    
    public String colorToString(Color color)
    {
        if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255)
        {
            return("White");
        }
        if (color.getRed() == 255 && color.getGreen() == 0 && color.getBlue() == 0)
        {
            return("Red");
        }
        if (color.getRed() == 0 && color.getGreen() == 255 && color.getBlue() == 0)
        {
            return("Green");
        }
        if (color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 255)
        {
            return("Blue");
        }
        if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 0)
        {
            return("Yellow");
        }
        if (color.getRed() == 255 && color.getGreen() == 100 && color.getBlue() == 0)
        {
            return("Orange");
        }
        if (color.getRed() == 180 && color.getGreen() == 0 && color.getBlue() == 255)
        {
            return("Purple");
        }
        if (color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 0)
        {
            return("Black");
        }
        return("Error");
    }
    
    public void actionPerformed(ActionEvent aevt)
    {
        Object selected = aevt.getSource();
        
        if (selected.equals(exit))
        {
            System.exit(0);
        }
        
        if (selected.equals(newGame))
        {
            setupGame();
        }
        
        if (selected.equals(settings))
        {
            if (setDone == false)
            {
                getContentPane().removeAll();
                
                setAsk = new JLabel("Current settings. Would you like to change them?");
                next0 = new JButton("Yes");
                next0.addActionListener(this);
                back0 = new JButton("No");
                back0.addActionListener(this);
                
                playerDis = new JLabel("Players: " + playerAmount);
                playerDis.setAlignmentX(Component.CENTER_ALIGNMENT);
                comDis = new JLabel("CPU Player?: " + computerPlayer);
                comDis.setAlignmentX(Component.CENTER_ALIGNMENT);
                difDis = new JLabel("CPU Difficulty: " + difficulty);
                difDis.setAlignmentX(Component.CENTER_ALIGNMENT);
                colDis = new JLabel("Color: " + colorToString(color));
                colDis.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                setTopPanel = new JPanel(new BorderLayout());
                next0Wrap = new JPanel(new FlowLayout());
                next0Wrap.add(next0);
                back0Wrap = new JPanel(new FlowLayout());
                back0Wrap.add(back0);
                setAskWrap = new JPanel(new FlowLayout());
                setAskWrap.add(setAsk);
                
                setTopPanel = new JPanel(new BorderLayout());
                setTopPanel.add(next0Wrap, BorderLayout.EAST);
                setTopPanel.add(back0Wrap, BorderLayout.WEST);
                setTopPanel.add(setAskWrap, BorderLayout.CENTER);
                
                setMidPanel = new JPanel();
                setMidPanel.setLayout(new BoxLayout(setMidPanel, BoxLayout.Y_AXIS));
                setMidPanel.add(playerDis);
                setMidPanel.add(comDis);
                setMidPanel.add(difDis);
                setMidPanel.add(colDis);
                
                getContentPane().add(setTopPanel, BorderLayout.NORTH);
                getContentPane().add(setMidPanel, BorderLayout.CENTER);
                pack();
                setSize(800,600);
                setDone = true;
            }
            else
            {
                getContentPane().removeAll();
                playerDis.setText("Players: " + playerAmount);
                comDis.setText("CPU Player?: " + computerPlayer);
                difDis.setText("CPU Difficulty: " + difficulty);
                colDis.setText("Color: " + colorToString(color));
                getContentPane().add(setTopPanel, BorderLayout.NORTH);
                getContentPane().add(setMidPanel, BorderLayout.CENTER);
                pack();
                setSize(800,600);
            }
        }
        
        if (selected.equals(next0))
        {
            if (next0Done == false)
            {
                getContentPane().removeAll();
            
                pAsk = new JLabel("How many players?");
                next1 = new JButton("Next");
                next1.addActionListener(this);
                back1 = new JButton("Back");
                back1.addActionListener(this);
                p2 = new JButton("2");
                p2.addActionListener(this);
                p4 = new JButton("4");
                p4.addActionListener(this);
                pMidPanel = new JPanel(new FlowLayout());
                pMidPanel.add(p2);
                pMidPanel.add(p4);
                
                next1Wrap = new JPanel(new FlowLayout());
                next1Wrap.add(next1);
                back1Wrap = new JPanel(new FlowLayout());
                back1Wrap.add(back1);
                pAskWrap = new JPanel(new FlowLayout());
                pAskWrap.add(pAsk);
                pTopPanel = new JPanel(new BorderLayout());
                pTopPanel.add(next1Wrap, BorderLayout.EAST);
                pTopPanel.add(back1Wrap, BorderLayout.WEST);
                pTopPanel.add(pAskWrap, BorderLayout.CENTER);
                
                getContentPane().add(pMidPanel, BorderLayout.CENTER);
                getContentPane().add(pTopPanel, BorderLayout.NORTH);
                pack();
                setSize(800,600);
                next0Done = true;
            }
            else
            {
                getContentPane().removeAll();
                getContentPane().add(pMidPanel, BorderLayout.CENTER);
                getContentPane().add(pTopPanel, BorderLayout.NORTH);
                pack();
                setSize(800,600);
            }
        }
        
        if (selected.equals(back0))
        {
            getContentPane().removeAll();
            getContentPane().add(startTopPanel, BorderLayout.NORTH);
            getContentPane().add(startMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(next1))
        {
            if (next1Done == false)
            {
                getContentPane().removeAll();

                JLabel comAsk = new JLabel("Multiplayer or Vs Computer?");
                next2 = new JButton("Next");
                next2.addActionListener(this);
                back2 = new JButton("Back");
                back2.addActionListener(this);
                comY = new JButton("Vs Computer");
                comY.addActionListener(this);
                comN = new JButton("Multiplayer");
                comN.addActionListener(this);

                comMidPanel = new JPanel(new FlowLayout());
                comMidPanel.add(comN);
                comMidPanel.add(comY);

                next2Wrap = new JPanel(new FlowLayout());
                next2Wrap.add(next2);
                back2Wrap = new JPanel(new FlowLayout());
                back2Wrap.add(back2);
                comAskWrap = new JPanel(new FlowLayout());
                comAskWrap.add(comAsk);
                comTopPanel = new JPanel(new BorderLayout());
                
                comTopPanel.add(next2Wrap, BorderLayout.EAST);
                comTopPanel.add(back2Wrap, BorderLayout.WEST);
                comTopPanel.add(comAskWrap, BorderLayout.CENTER);
            
                getContentPane().add(comMidPanel, BorderLayout.CENTER);
                getContentPane().add(comTopPanel, BorderLayout.NORTH);
            
                pack();
                setSize(800,600);
                next1Done = true;
            }
            else 
            {
                getContentPane().removeAll();
                getContentPane().add(comTopPanel, BorderLayout.NORTH);
                getContentPane().add(comMidPanel, BorderLayout.CENTER);
                pack();
                setSize(800,600);
            }
        }
        
        if (selected.equals(back1))
        {
            getContentPane().removeAll();
            playerDis.setText("Players: " + playerAmount);
            comDis.setText("CPU Player?: " + computerPlayer);
            difDis.setText("CPU Difficulty: " + difficulty);
            colDis.setText("Color: " + colorToString(color));
            getContentPane().add(setTopPanel, BorderLayout.NORTH);
            getContentPane().add(setMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(p2))
        {
            playerAmount = 2;
            playerOrder.clear();
            playerOrder.add(1);
            playerOrder.add(2);
        }
        
        if (selected.equals(p4))
        {
            playerAmount = 4;
            playerOrder.clear();
            playerOrder.add(1);
            playerOrder.add(2);
            playerOrder.add(3);
            playerOrder.add(4);
        }
        
        if (selected.equals(back2))
        {
            getContentPane().removeAll();
            getContentPane().add(pTopPanel, BorderLayout.NORTH);
            getContentPane().add(pMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(next2))
        {
            if (next2Done == false)
            {
                getContentPane().removeAll();
                
                difAsk = new JLabel("Easy or Hard COM?");
                next3 = new JButton("Next");
                next3.addActionListener(this);
                back3 = new JButton("Back");
                back3.addActionListener(this);
                difE = new JButton("Easy");
                difE.addActionListener(this);
                difH = new JButton("Hard");
                difH.addActionListener(this);
                
                difMidPanel = new JPanel(new FlowLayout());
                difMidPanel.add(difE);
                difMidPanel.add(difH);
            
                next3Wrap = new JPanel(new FlowLayout());
                next3Wrap.add(next3);
                back3Wrap = new JPanel(new FlowLayout());
                back3Wrap.add(back3);
                difAskWrap = new JPanel(new FlowLayout());
                difAskWrap.add(difAsk);
                difTopPanel = new JPanel(new BorderLayout());
                
                difTopPanel.add(next3Wrap, BorderLayout.EAST);
                difTopPanel.add(back3Wrap, BorderLayout.WEST);
                difTopPanel.add(difAskWrap, BorderLayout.CENTER);
            
                getContentPane().add(difMidPanel, BorderLayout.CENTER);
                getContentPane().add(difTopPanel, BorderLayout.NORTH);
            
                pack();
                setSize(800,600);
                next2Done = true;
            }
            else 
            {
                getContentPane().removeAll();
                getContentPane().add(difTopPanel, BorderLayout.NORTH);
                getContentPane().add(difMidPanel, BorderLayout.CENTER);
                pack();
                setSize(800,600);
            }
        }
        
        if (selected.equals(comN))
        {
            computerPlayer = false;
        }
        
        if (selected.equals(comY))
        {
            computerPlayer = true;
        }
        
        if (selected.equals(back3))
        {
            getContentPane().removeAll();
            getContentPane().add(comTopPanel, BorderLayout.NORTH);
            getContentPane().add(comMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(next3))
        {
            if (next3Done == false)
            {
                getContentPane().removeAll();
                
                colAsk = new JLabel("Which Board Color?");
                next4 = new JButton("Next");
                next4.addActionListener(this);
                back4 = new JButton("Back");
                back4.addActionListener(this);
                col0 = new JButton("White");
                col0.addActionListener(this);
                col1 = new JButton("Red");
                col1.addActionListener(this);
                col2 = new JButton("Green");
                col2.addActionListener(this);
                col3 = new JButton("Blue");
                col3.addActionListener(this);
                col4 = new JButton("Yellow");
                col4.addActionListener(this);
                col5 = new JButton("Orange");
                col5.addActionListener(this);
                col6 = new JButton("Purple");
                col6.addActionListener(this);
                col7 = new JButton("Black");
                col7.addActionListener(this);
                
                colMidPanel = new JPanel(new FlowLayout());
                colMidPanel.add(col0);
                colMidPanel.add(col1);
                colMidPanel.add(col2);
                colMidPanel.add(col3);
                colMidPanel.add(col4);
                colMidPanel.add(col5);
                colMidPanel.add(col6);
                colMidPanel.add(col7);
            
                next4Wrap = new JPanel(new FlowLayout());
                next4Wrap.add(next4);
                back4Wrap = new JPanel(new FlowLayout());
                back4Wrap.add(back4);
                colAskWrap = new JPanel(new FlowLayout());
                colAskWrap.add(colAsk);
                colTopPanel = new JPanel(new BorderLayout());
                
                colTopPanel.add(next4Wrap, BorderLayout.EAST);
                colTopPanel.add(back4Wrap, BorderLayout.WEST);
                colTopPanel.add(colAskWrap, BorderLayout.CENTER);
            
                getContentPane().add(colMidPanel, BorderLayout.CENTER);
                getContentPane().add(colTopPanel, BorderLayout.NORTH);
            
                pack();
                setSize(800,600);
                next3Done = true;
            }
            else 
            {
                getContentPane().removeAll();
                getContentPane().add(colTopPanel, BorderLayout.NORTH);
                getContentPane().add(colMidPanel, BorderLayout.CENTER);
                pack();
                setSize(800,600);
            }
        }
        
        if (selected.equals(difE))
        {
            difficulty = "Easy";
        }
        
        if (selected.equals(difH))
        {
            difficulty = "Hard";
        }
        
        if (selected.equals(col0))
        {
            color = new Color(255,255,255);
        }
        
        if (selected.equals(col1))
        {
            color = new Color(255,0,0);
        }
        
        if (selected.equals(col2))
        {
            color = new Color(0,255,0);
        }
        
        if (selected.equals(col3))
        {
            color = new Color(0,0,255);
        }

        if (selected.equals(col4))
        {
            color = new Color(255,255,0);
        }
        
        if (selected.equals(col5))
        {
            color = new Color(255,100,0);
        }
        
        if (selected.equals(col6))
        {
            color = new Color(180,0,255);
        }
        
        if (selected.equals(col7))
        {
            color = new Color(0,0,0);
        }
        
        if (selected.equals(back4))
        {
            getContentPane().removeAll();
            getContentPane().add(difTopPanel, BorderLayout.NORTH);
            getContentPane().add(difMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(next4))
        {
            if (next4Done == false)
            {
                getContentPane().removeAll();
                
                tutAsk = new JLabel("Would you like a tutorial on playing Hooop?");
                back5 = new JButton("Back");
                back5.addActionListener(this);
                next5 = new JButton("Save and Exit");
                next5.addActionListener(this);
                tutY = new JButton("Sure");
                tutY.addActionListener(this);
                
                tutMidPanel = new JPanel(new FlowLayout());
                tutMidPanel.add(tutY);
            
                back5Wrap = new JPanel(new FlowLayout());
                back5Wrap.add(back5);
                next5Wrap = new JPanel(new FlowLayout());
                next5Wrap.add(next5);
                tutAskWrap = new JPanel(new FlowLayout());
                tutAskWrap.add(tutAsk);
                tutTopPanel = new JPanel(new BorderLayout());
                
                tutTopPanel.add(back5Wrap, BorderLayout.WEST);
                tutTopPanel.add(next5Wrap, BorderLayout.EAST);
                tutTopPanel.add(tutAskWrap, BorderLayout.CENTER);
            
                getContentPane().add(tutMidPanel, BorderLayout.CENTER);
                getContentPane().add(tutTopPanel, BorderLayout.NORTH);
            
                pack();
                setSize(800,600);
                next4Done = true;
            }
            else    
            {
                getContentPane().removeAll();
                getContentPane().add(tutTopPanel, BorderLayout.NORTH);
                getContentPane().add(tutMidPanel, BorderLayout.CENTER);
                pack();
                setSize(800,600);
            }
        }
        
        if (selected.equals(tutY))
        {
            if (tutLoadDone == false)
            {
                getContentPane().removeAll();
                
                objectOf = new JLabel("Object of the Game: With two players, each player must get all their frogs to their opponent’s home leaf. With four players, each player must get one of their frogs to each other player’s home leaves.");
                objectOfWrap = new JPanel(new FlowLayout());
                turnInfoLabel = new JLabel("On your turn, you may either move a frog, place a bridge, or use a special card.");
                turnInfoGrid = new JPanel(new GridLayout(3,1));
                frogTurnInfo = new JLabel("Frogs can only move across bridges, and a bridge is removed once a frog has crossed it (unless it is a bridge to a home leaf, which can never be removed). Two frogs cannot occupy the same lilypad. If a frog moves to a lilypad that already has a frog on it, the frog may move again to another connected lilypad as many times as needed to reach an open lilypad.");
                bridgeTurnInfo = new JLabel("Bridges can only be placed on open gaps. You cannot double up bridges or place them diagonally.");
                cardTurnInfo = new JLabel("There are four special cards given to each player, and you may use each special card once. The cards are detailed below:");
                turnInfoGrid.add(frogTurnInfo);
                turnInfoGrid.add(bridgeTurnInfo);
                turnInfoGrid.add(cardTurnInfo);
                
                tutTextMidPanel = new JPanel(new BorderLayout());
                tutTextMidPanel.add(turnInfoLabel, BorderLayout.NORTH);
                tutTextMidPanel.add(turnInfoGrid, BorderLayout.CENTER);
                
                cardsGrid = new JPanel(new GridLayout(4,1));
                DJcardInfo = new JLabel("The “Double Jump” card allows you to select a frog to do two jumps, the first of which must be to an empty lilypad.");
                PRcardInfo = new JLabel("The “Parachute” card allows you to make a jump without needing a bridge.");
                EBcardInfo = new JLabel("The “Extra Bridge” card allows you to play two bridges instead of one.");
                BRcardInfo = new JLabel("The “Bridge Removal” card allows you to remove a bridge anywhere on the board.");
                cardsGrid.add(DJcardInfo);
                cardsGrid.add(PRcardInfo);
                cardsGrid.add(EBcardInfo);
                cardsGrid.add(BRcardInfo);
                leaveTut = new JButton("Exit");
                leaveTut.addActionListener(this);
                leaveTutWrap = new JPanel(new FlowLayout());
                leaveTutWrap.add(leaveTut);
                
                tutTextBotPanel = new JPanel(new BorderLayout());
                tutTextBotPanel.add(cardsGrid, BorderLayout.CENTER);
                tutTextBotPanel.add(leaveTutWrap, BorderLayout.EAST);
                
                getContentPane().add(objectOfWrap, BorderLayout.NORTH);
                getContentPane().add(tutTextMidPanel, BorderLayout.CENTER);
                getContentPane().add(tutTextBotPanel, BorderLayout.SOUTH);
                pack();
                setSize(1200,800);
                tutLoadDone = true;
            }
            else 
            {
                getContentPane().removeAll();
                getContentPane().add(objectOfWrap, BorderLayout.NORTH);
                getContentPane().add(tutTextMidPanel, BorderLayout.CENTER);
                getContentPane().add(tutTextBotPanel, BorderLayout.SOUTH);
                pack();
                setSize(1200,800);
            }
        }
        
        if (selected.equals(leaveTut))
        {
            getContentPane().removeAll();
            getContentPane().add(tutTopPanel, BorderLayout.NORTH);
            getContentPane().add(tutMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(next5))
        {
            getContentPane().removeAll();
            pack();
            getContentPane().add(startTopPanel, BorderLayout.NORTH);
            getContentPane().add(startMidPanel, BorderLayout.CENTER);
            setSize(800,600);
        }
        
        if (selected.equals(back5))
        {
            getContentPane().removeAll();
            getContentPane().add(colTopPanel, BorderLayout.NORTH);
            getContentPane().add(colMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(gameExit))
        {
            getContentPane().removeAll();
            getContentPane().add(startTopPanel, BorderLayout.NORTH);
            getContentPane().add(startMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
            color = new Color(0,255,0);
            playerAmount = 2;
            computerPlayer = true;
            difficulty = "Easy";
        }
        
        if (selected.equals(gameSaveExit))
        {
            
        }
    }
}
