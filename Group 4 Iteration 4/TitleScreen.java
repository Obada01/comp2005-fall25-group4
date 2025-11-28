import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TitleScreen extends JPanel implements ActionListener {
    // Variable declarations
    public static final String SCREEN_NAME = "TitleScreen";
    private JPanel startTopPanel, startMidPanel, startWrapPanel;
    private JButton newGame, loadGame, settings, exit;
    private JLabel titleText;
    private SettingsMenu settingsMenu;

    // Constructor
    public TitleScreen(SettingsMenu settingsMenu) {
        this.settingsMenu = settingsMenu;
        // Border layout for title screen
        setLayout(new BorderLayout());
        
        startTopPanel = new JPanel(new BorderLayout());
        startMidPanel = new JPanel();
        startMidPanel.setLayout(new BoxLayout(startMidPanel, BoxLayout.Y_AXIS));

        // Create buttons
        newGame = new JButton("New Game");
        newGame.addActionListener(this);
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        loadGame = new JButton("Load Game");
        loadGame.addActionListener(this);
        loadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        settings = new JButton("Settings");
        settings.addActionListener(this);
        settings.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        exit = new JButton("Exit");
        exit.addActionListener(this);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titleText = new JLabel("Hooop!");
        titleText.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
        
        startWrapPanel = new JPanel(new FlowLayout());
        startWrapPanel.add(titleText);
        
        startTopPanel.add(startWrapPanel);
        startMidPanel.add(newGame);
        startMidPanel.add(loadGame);
        startMidPanel.add(settings);
        startMidPanel.add(exit);
        
        // Add panels to main panel
        add(startTopPanel, BorderLayout.NORTH);
        add(startMidPanel, BorderLayout.CENTER);
        
        // Add some padding around the main panel
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == newGame)
        {
            // Create a new GameFrame with the current settings
            GameFrame gameFrame = new GameFrame(settingsMenu);
            BaseFrame.addScreen(GameFrame.SCREEN_NAME, gameFrame);
            BaseFrame.showScreen(GameFrame.SCREEN_NAME);
        }

        else if (e.getSource() == loadGame)
        {
            // Check if a save file exists
            if (!SaveManager.saveFileExists())
            {
                JOptionPane.showMessageDialog(this, "No saved game found.", "Load Game", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Load the game state
            GameState state = SaveManager.loadGame();
            
            // Restore the game
            GameLogic restoredLogic = SaveManager.restoreGameState(state, settingsMenu);
            GameFrame gameFrame = new GameFrame(settingsMenu, restoredLogic);
            BaseFrame.addScreen(GameFrame.SCREEN_NAME, gameFrame);
            BaseFrame.showScreen(GameFrame.SCREEN_NAME);
        }

        else if (e.getSource() == settings)
        {
            // Show the settings screen
            BaseFrame.showScreen(SettingsMenu.SCREEN_NAME);
        }
        else if(e.getSource() == exit)
        {
            // Exit the game
            System.exit(0);
        }
    }
}