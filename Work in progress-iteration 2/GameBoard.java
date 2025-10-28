
/**
 * Write a description of class GameBoard here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoard extends JFrame implements ActionListener
{
    private String color;
    private ArrayList<Integer> playerOrder;
    private ArrayList<Lilipad> grid;
    private ArrayList<Lilipad> possibleConnections;
    private boolean gameOver;
    private int playerAmount;
    private boolean computerPlayer = false;
    private String difficulty;
    private JButton newGame, loadGame, exit, next1, back1, p2, p4, next2, back2, comY, comN, next3, back3, difE, next4, back4, difH, col0, col1, col2, col3, col4, col5, col6, col7, back5, tutY, tutN, tutDone, leaveTut;
    private JPanel startTopPanel, startMidPanel, startWrapPanel, pTopPanel, pMidPanel, next1Wrap, back1Wrap, pAskWrap, comTopPanel, comMidPanel, next2Wrap, back2Wrap, comAskWrap, difTopPanel, difMidPanel, next3Wrap, back3Wrap, difAskWrap, colTopPanel, colMidPanel, next4Wrap, back4Wrap, colAskWrap, tutTopPanel, tutMidPanel, back5Wrap, tutAskWrap, objectOfWrap, turnInfoGrid, tutTextMidPanel, cardsGrid, leaveTutWrap, tutTextBotPanel;
    private JLabel titleText, pAsk, comAsk, difAsk, colAsk, tutAsk, objectOf, turnInfoLabel, frogTurnInfo, bridgeTurnInfo, cardTurnInfo, DJcardInfo, PRcardInfo, EBcardInfo, BRcardInfo;
    
    /**
     * Constructor for objects of class GameBoard
     */
    public GameBoard()
    {
        playerOrder = new ArrayList<Integer>();
        grid = new ArrayList<Lilipad>();
        possibleConnections = new ArrayList<Lilipad>();
        this.color = color;
        this.gameOver = gameOver;
        this.playerAmount = playerAmount;
        this.computerPlayer = computerPlayer;
        this.difficulty = difficulty;
        
        startTopPanel = new JPanel(new BorderLayout());
        startMidPanel = new JPanel();
        startMidPanel.setLayout(new BoxLayout(startMidPanel, BoxLayout.Y_AXIS));
        
        newGame = new JButton("New Game");
    newGame.addActionListener(this);
    newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
    JButton loadGame = new JButton("Load Game");
    loadGame.addActionListener(this);
    loadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        
        if (selected.equals(next1))
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
        }
        
        if (selected.equals(back1))
        {
            getContentPane().removeAll();
            getContentPane().add(startTopPanel, BorderLayout.NORTH);
            getContentPane().add(startMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(p2))
        {
            playerAmount = 2;
        }
        
        if (selected.equals(p4))
        {
            playerAmount = 4;
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
            getContentPane().removeAll();
            
            colAsk = new JLabel("Which Board Color?");
            next4 = new JButton("Next");
            next4.addActionListener(this);
            back4 = new JButton("Back");
            back4.addActionListener(this);
            col0 = new JButton("Color0");
            col0.addActionListener(this);
            col1 = new JButton("Color1");
            col1.addActionListener(this);
            col2 = new JButton("Color2");
            col2.addActionListener(this);
            col3 = new JButton("Color3");
            col3.addActionListener(this);
            col4 = new JButton("Color4");
            col4.addActionListener(this);
            col5 = new JButton("Color5");
            col5.addActionListener(this);
            col6 = new JButton("Color6");
            col6.addActionListener(this);
            col7 = new JButton("Color7");
            col7.addActionListener(this);
            
            colMidPanel = new JPanel(new GridLayout(2,4));
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
            color = "Color 0";
        }
        
        if (selected.equals(col1))
        {
            color = "Color 1";
        }
        
        if (selected.equals(col2))
        {
            color = "Color 2";
        }
        
        if (selected.equals(col3))
        {
            color = "Color 3";
        }

        if (selected.equals(col4))
        {
            color = "Color 4";
        }
        
        if (selected.equals(col5))
        {
            color = "Color 5";
        }
        
        if (selected.equals(col6))
        {
            color = "Color 6";
        }
        
        if (selected.equals(col7))
        {
            color = "Color 7";
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
            getContentPane().removeAll();
            
            tutAsk = new JLabel("Would you like a tutorial on playing Hooop?");
            back5 = new JButton("Back");
            back5.addActionListener(this);
            tutY = new JButton("Yes");
            tutY.addActionListener(this);
            tutN = new JButton("No. Start!");
            tutN.addActionListener(this);
            
            tutMidPanel = new JPanel(new FlowLayout());
            tutMidPanel.add(tutY);
            tutMidPanel.add(tutN);
        
            back5Wrap = new JPanel(new FlowLayout());
            back5Wrap.add(back5);
            tutAskWrap = new JPanel(new FlowLayout());
            tutAskWrap.add(tutAsk);
            tutTopPanel = new JPanel(new BorderLayout());
            
            tutTopPanel.add(back5Wrap, BorderLayout.WEST);
            tutTopPanel.add(tutAskWrap, BorderLayout.CENTER);
        
            getContentPane().add(tutMidPanel, BorderLayout.CENTER);
            getContentPane().add(tutTopPanel, BorderLayout.NORTH);
        
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(tutY))
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
        }
        
        if (selected.equals(leaveTut))
        {
            getContentPane().removeAll();
            getContentPane().add(tutTopPanel, BorderLayout.NORTH);
            getContentPane().add(tutMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
        
        if (selected.equals(tutN))
        {
            //this is where you would draw the actual game board
            //or call a method that does
            //code here is just to test if setting work
            System.out.println("Players: " + playerAmount);
            System.out.println("Com?: " + computerPlayer);
            System.out.println("Difficulty: " + difficulty);
            System.out.println("Color: " + color);
        }
        
        if (selected.equals(back5))
        {
            getContentPane().removeAll();
            getContentPane().add(colTopPanel, BorderLayout.NORTH);
            getContentPane().add(colMidPanel, BorderLayout.CENTER);
            pack();
            setSize(800,600);
        }
    }
}
