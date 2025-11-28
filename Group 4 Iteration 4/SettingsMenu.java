import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SettingsMenu extends JPanel implements ActionListener {
    public static final String SCREEN_NAME = "SettingsMenu";
    private JButton next, back, choiceButton0, choiceButton1, choiceButton2, choiceButton3, choiceButton4, choiceButton5, 
                    choiceButton6, choiceButton7;
    private JLabel setAsk, playerDis, comDis, difDis, colDis, gameInfo, cardsInfo;
    private JPanel setTopPanel, setMidPanel, nextWrap, backWrap, setAskWrap, choiceButtonWrap, wrapperPanel, contentPanel;
    private int settingsMenuID = 0;
    public int playerAmount = 2;
    public boolean computerPlayer = true;
    public String difficulty = "Easy";
    public Color color;
    private ArrayList<JButton> choiceButtons;

        public SettingsMenu()
        {
            // Set the layout of the window to a border layout
            setLayout(new BorderLayout());
            
            // Set a default color for the lilipads
            this.color = new Color(0, 255, 0);

            // Create the next and back buttons
            next = new JButton("Next");
            next.addActionListener(this);
            back = new JButton("Back");
            back.addActionListener(this);

            // Create main ask label (default of no text)
            setAsk = new JLabel();
            
            // Labels for the settings confirmation menu (Blank for now)
            playerDis = new JLabel();
            comDis = new JLabel();
            difDis = new JLabel();
            colDis = new JLabel();

            // Centers all labels for settings confirmation menu
            playerDis.setAlignmentX(Component.CENTER_ALIGNMENT);
            comDis.setAlignmentX(Component.CENTER_ALIGNMENT);
            difDis.setAlignmentX(Component.CENTER_ALIGNMENT);
            colDis.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Creates the middle panel for the settings menu.
            setMidPanel = new JPanel();
            setMidPanel.setLayout(new BoxLayout(setMidPanel, BoxLayout.Y_AXIS));
            
            // Settings top panel creation for back button, next button, and prompt for user button selection
            nextWrap = new JPanel(new FlowLayout());
            nextWrap.add(next);
            backWrap = new JPanel(new FlowLayout());
            backWrap.add(back);
            setAskWrap = new JPanel(new FlowLayout());
            setAskWrap.add(setAsk);

            // Creates the top settings panel and adds the components to it
            setTopPanel = new JPanel(new BorderLayout());
            setTopPanel.add(nextWrap, BorderLayout.EAST);
            setTopPanel.add(backWrap, BorderLayout.WEST);
            setTopPanel.add(setAskWrap, BorderLayout.CENTER);

            // Create the choice button arraylist to store all buttons
            choiceButtons = new ArrayList<JButton>();

            // Create all buttons needed for choices (max 8 for color modes)
            choiceButton0 = new JButton();
            choiceButton1 = new JButton();
            choiceButton2 = new JButton();
            choiceButton3 = new JButton();
            choiceButton4 = new JButton();
            choiceButton5 = new JButton();
            choiceButton6 = new JButton();
            choiceButton7 = new JButton();

            // Add all the buttons into the arraylist for easier access
            choiceButtons.add(choiceButton0);
            choiceButtons.add(choiceButton1);
            choiceButtons.add(choiceButton2);
            choiceButtons.add(choiceButton3);
            choiceButtons.add(choiceButton4);
            choiceButtons.add(choiceButton5);
            choiceButtons.add(choiceButton6);
            choiceButtons.add(choiceButton7);
            
            // Creates a panel for all the choice buttons
            choiceButtonWrap = new JPanel(new FlowLayout());

            // For every button in the choice buttons, add an action listener to it, make it invisible, and add it to the wrap.
            for(JButton button : choiceButtons)
            {
                button.addActionListener(this);
                button.setVisible(false);
                choiceButtonWrap.add(button);
            }

            // Adds the top panel to the settings window
            add(setTopPanel, BorderLayout.NORTH);

            // Adds the center panel to the settings window and preps user instructions
            settingsMenuConfirmation();
        }

    // Helper function to convert any color to a string.
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

    // Resets to some base settings in the settings menu
    private void menuReset()
    {
        setAsk.setText("");

        for(JButton button : choiceButtons)
        {
            button.setVisible(false);
        }
    }

    // First page of the settings menu, used to confirm that the user wishes to change their settings
    private void settingsMenuConfirmation()
    {
        // Reset the layout
        setLayout(new BorderLayout());
        
        // Recreate the top panel
        setTopPanel = new JPanel(new BorderLayout());
        nextWrap = new JPanel(new FlowLayout());
        nextWrap.add(next);
        backWrap = new JPanel(new FlowLayout());
        backWrap.add(back);
        setAskWrap = new JPanel(new FlowLayout());
        setAskWrap.add(setAsk);
        
        // Rebuild the top panel
        setTopPanel.add(nextWrap, BorderLayout.EAST);
        setTopPanel.add(backWrap, BorderLayout.WEST);
        setTopPanel.add(setAskWrap, BorderLayout.CENTER);
        
        // Reset the middle panel
        setMidPanel = new JPanel();
        setMidPanel.setLayout(new BoxLayout(setMidPanel, BoxLayout.Y_AXIS));
        
        // Reset the choice button wrap
        choiceButtonWrap = new JPanel(new FlowLayout());
        for (JButton button : choiceButtons) {
            choiceButtonWrap.add(button);
        }
        
        // Reset button texts
        back.setText("Back");
        next.setText("Next");
        setAsk.setText("Current settings. Would you like to change them?");

        // Update the display texts
        playerDis.setText("Players: " + playerAmount);
        comDis.setText("Add CPUs: " + computerPlayer);
        difDis.setText("CPU Difficulty: " + difficulty);
        colDis.setText("Color: " + colorToString(color));

        // Re-add components
        setMidPanel.add(playerDis);
        setMidPanel.add(colDis);
        setMidPanel.add(comDis);

        if (computerPlayer) {
            setMidPanel.add(difDis);
        }

        // Remove all components and re-add them in the correct order
        removeAll();
        add(setTopPanel, BorderLayout.NORTH);
        add(setMidPanel, BorderLayout.CENTER);
        
        // Make sure all components are visible
        back.setVisible(true);
        next.setVisible(true);
        
        // Force a re-layout
        revalidate();
        repaint();
    }

    // Second page of the settings menu, used to check if the user wants to play with 2 or 4 players.
    private void setupPlayerSelect()
    {
        back.setText("Back");
        next.setText("Next");

        setAsk.setText("How many players?");

        choiceButtons.get(0).setText("2");
        choiceButtons.get(1).setText("4");
        
        choiceButtons.get(0).setVisible(true);
        choiceButtons.get(1).setVisible(true);

        remove(setMidPanel); // Removes the settings confirmation panel
        add(choiceButtonWrap, BorderLayout.CENTER); // Adds choice buttons

        // Stops everything from breaking DO NOT REMOVE
        revalidate();
        repaint();
    }

    // Third page of the settings menu, used to ask if the user wants to play with CPUs.
    private void setComputerOptions()
    {
        setAsk.setText("Add CPU players?");

        choiceButtons.get(0).setText("Yes");
        choiceButtons.get(1).setText("No");
        
        choiceButtons.get(0).setVisible(true);
        choiceButtons.get(1).setVisible(true);
    }

    // Fourth page of the settings menu, used to ask if the user for CPU difficulty.
    private void setComputerDifficulty()
    {
        setAsk.setText("Easy or hard CPU?");

        choiceButtons.get(0).setText("Easy");
        choiceButtons.get(1).setText("Hard");
        
        choiceButtons.get(0).setVisible(true);
        choiceButtons.get(1).setVisible(true);
    }

    // Fifth page of the settings menu, used to get the user's preferred board color
    private void setBoardColor()
    {
        next.setVisible(true);
        
        setAsk.setText("Which color of board would you prefer?");

        choiceButtons.get(0).setText("White");
        choiceButtons.get(1).setText("Red");
        choiceButtons.get(2).setText("Green");
        choiceButtons.get(3).setText("Blue");
        choiceButtons.get(4).setText("Yellow");
        choiceButtons.get(5).setText("Orange");
        choiceButtons.get(6).setText("Purple");
        choiceButtons.get(7).setText("Black");

        for(JButton button : choiceButtons)
        {
            button.setVisible(true);
        }
    }

    // Sixth page of the settings menu, offers the user a tutorial on Hooop!
    private void gameTutorialPrompt()
    {
        next.setVisible(false);
        
        setAsk.setText("Would you like to read a brief tutorial on how to play?");

        choiceButtons.get(0).setText("Yes");
        choiceButtons.get(1).setText("No");

        choiceButtons.get(0).setVisible(true);
        choiceButtons.get(1).setVisible(true);
    }

    // Last page of the settings menu, shows tutorial info.
    private void startTutorial()
    {
        menuReset();

        next.setVisible(true);
        next.setText("Done");
        back.setVisible(false);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        wrapperPanel = new JPanel(new GridBagLayout());

        // HTML settings to get tutorial displaying properly.
        gameInfo = new JLabel("<html><div style='text-align: center;'>" + 
            "Object of the Game: With two players, each player must get all their frogs to their opponent's home leaf. " +
            "With four players, each player must get one of their frogs to each other player's home leaves. " +
            "On your turn, you may either move a frog, place a bridge, or use a special card. " +
            "Frogs can only move across bridges, and a bridge is removed once a frog has crossed it " +
            "(unless it is a bridge to a home leaf, which can never be removed). Two frogs cannot occupy the same lilypad. " +
            "If a frog moves to a lilypad that already has a frog on it, the frog may move again to another connected lilypad " +
            "as many times as needed to reach an open lilypad. Bridges can only be placed on open gaps. " +
            "You cannot double up bridges or place them diagonally. There are four special cards given to each player, " +
            "and you may use each special card once. The cards are detailed below:" +
            "</div></html>");

        gameInfo.setHorizontalAlignment(JLabel.CENTER);
        gameInfo.setVerticalAlignment(JLabel.TOP);
        
        setMidPanel.removeAll();
        setMidPanel.setLayout(new GridBagLayout());
        setMidPanel.add(gameInfo);

        // HTML settings to get tutorial displaying properly.
        cardsInfo = new JLabel("<html><div style='text-align: center;'>" +
            "The 'Double Jump' card allows you to select a frog to do two jumps, the first of which must be to an empty lilypad.<br/><br/>" +
            "The 'Parachute' card allows you to make a jump without needing a bridge.<br/><br/>" +
            "The 'Extra Bridge' card allows you to play two bridges instead of one.<br/><br/>" +
            "The 'Bridge Removal' card allows you to remove a bridge anywhere on the board." +
            "</div></html>");
        cardsInfo.setHorizontalAlignment(JLabel.CENTER);
        cardsInfo.setVerticalAlignment(JLabel.TOP);

        // Add components to content panel with some spacing
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(gameInfo);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(cardsInfo);
        contentPanel.add(Box.createVerticalGlue());

        // Add content panel to wrapper with GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        wrapperPanel.add(contentPanel, gbc);

        // Add wrapper to mid panel with padding
        setMidPanel.removeAll();
        setMidPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Add padding
        setMidPanel.setLayout(new BorderLayout());
        setMidPanel.add(wrapperPanel, BorderLayout.CENTER);

        // Add to frame and update
        add(setMidPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Returns a string to represent the settings. For example, 2 players, w/hard CPU and green lilipads is code: 21HG
    public String returnSettings()
    {
        String settings = new String();

        if(playerAmount == 2)
        {
            settings += "2";
        }

        else
        {
            settings += "4";
        }

        if(computerPlayer == true)
        {
            settings += "1";

            if("Easy".equals(difficulty))
            {
                settings += "E";
            }

            else
            {
                settings += "H";
            }
        }

        else
        {
            settings += "00";
        }

        switch(colorToString(color))
        {
            case "White":
            {
                settings += "W";
                break;
            }

            case "Red":
            {
                settings += "R";
                break;
            }

            case "Green":
            {
                settings += "G";
                break;
            }

            case "Blue":
            {
                settings += "B";
                break;
            }

            case "Yellow":
            {
                settings += "Y";
                break;
            }

            case "Orange":
            {
                settings += "O";
                break;
            }

            case "Purple":
            {
                settings += "P";
                break;
            }

            case "Black":
            {
                settings += "b";
                break;
            }
        }

        return settings;
    }
    
    // Code to get buttons actually doing stuff
    @Override
    public void actionPerformed(ActionEvent selected)
    {
        // Checks to see if the back button was selected
        if (selected.getSource() == back)
        {
            // Resets the menu to allow for new stuff to be added
            menuReset();
            
            // Sends the player to the title screen
            if (settingsMenuID == 0)
            {
                BaseFrame.showScreen(TitleScreen.SCREEN_NAME);
                settingsMenuID = 1;
                settingsMenuConfirmation();
            }

            // Sends the player to the settings confirmation screen
            else if (settingsMenuID == 1)
            {
                settingsMenuConfirmation();
            }

            // Sends the player to the player count screen
            else if (settingsMenuID == 2)
            {
                setupPlayerSelect();
            }

            // Sends the player to the CPU confirmation screen
            else if (settingsMenuID == 3)
            {
                setComputerOptions();
            }

            // Sends the player to the CPU difficulty setting screen if they have CPUs, otherwise, sends them to the CPU confirmation screen
            else if (settingsMenuID == 4)
            {
                if(computerPlayer == true)
                {
                    setComputerDifficulty();
                }
                else
                {
                    setComputerOptions();
                    settingsMenuID = 3;
                }
            }

            // Sends the player to the board color settings screen.
            else if(settingsMenuID == 5)
            {
                setBoardColor();
            }

            // Decrements the player's current menu position
            settingsMenuID -= 1;
        }

        else if (selected.getSource() == next)
        {
            // Resets the menu components
            menuReset();

            // Sends the player to the player count screen
            if (settingsMenuID == 0)
            {
                setupPlayerSelect();
            }

            // Sends the player to the CPU confirmation screen
            else if (settingsMenuID == 1)
            {
                setComputerOptions();
            }

            // Sends the player to the CPU difficulty setting screen if they have CPUs, otherwise, sends them to the board color settings screen
            else if (settingsMenuID == 2)
            {
                if(computerPlayer == true)
                {
                    setComputerDifficulty();
                }
                else
                {
                    setBoardColor();
                    settingsMenuID = 3;
                }
            }

            // Sends the player to the board color settings screen.
            else if (settingsMenuID == 3)
            {
                setBoardColor();
            }

            else if (settingsMenuID == 4)
            {
                gameTutorialPrompt();
            }

            // Sends the player to the title screen
            if (settingsMenuID == 5)
            {
                BaseFrame.showScreen(TitleScreen.SCREEN_NAME);
                settingsMenuID = -1;
                settingsMenuConfirmation();
                back.setVisible(true);
            }

            // Increments the player's current menu position.
            settingsMenuID += 1;
        }

        // Covers all possible cases for the first choice button type
        else if(selected.getSource() == choiceButton0)
        {
            String choice = choiceButton0.getText();

            if("Yes".equals(choice))
            {
                if(settingsMenuID == 2)
                {
                    computerPlayer = true;
                }

                else if(settingsMenuID == 5)
                {
                    startTutorial();
                }
            }

            else if("2".equals(choice))
            {
                playerAmount = 2;
            }

            else if("Easy".equals(choice))
            {
                difficulty = "Easy";
            }
            
            else if("White".equals(choice))
            {
                color = new Color(255, 255, 255);
            }
        }

        // Covers all possible cases for the second choice button type
        else if(selected.getSource() == choiceButton1)
        {
            String choice = choiceButton1.getText();

            if("No".equals(choice))
            {
                if(settingsMenuID == 2)
                {
                    computerPlayer = false;
                }

                else if(settingsMenuID == 5)
                {
                    BaseFrame.showScreen(TitleScreen.SCREEN_NAME);
                    settingsMenuID = 0;
                    settingsMenuConfirmation();
                    next.setVisible(true);
                }
            }

            else if("4".equals(choice))
            {
                playerAmount = 4;
            }

            else if("Hard".equals(choice))
            {
                difficulty = "Hard";
            }
            
            else if("Red".equals(choice))
            {
                color = new Color(255, 0, 0);
            }
        }

        // The rest of the buttons only show up in the color menu, so they're simple to set up
        else if(selected.getSource() == choiceButton2)
        {
            color = new Color(0, 255, 0);
        }

        else if(selected.getSource() == choiceButton3)
        {
            color = new Color(0, 0, 255);
        }

        else if(selected.getSource() == choiceButton4)
        {
            color = new Color(255, 255, 0);
        }

        else if(selected.getSource() == choiceButton5)
        {
            color = new Color(255, 100, 0);
        }

        else if(selected.getSource() == choiceButton6)
        {
            color = new Color(180, 0, 255);
        }

        else if(selected.getSource() == choiceButton7)
        {
            color = new Color(0, 0, 0);
        }
    }
}