import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BaseFrame extends JFrame
{
    // Variable declarations
    private static BaseFrame instance;
    private JPanel cards;
    private CardLayout cardLayout;
    private Map<String, JPanel> screens;
    
    // Constructor
    public BaseFrame(String title)
    {
        super(title);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        this.cardLayout = new CardLayout();
        this.cards = new JPanel(cardLayout);
        this.screens = new HashMap<>();
        
        add(cards, BorderLayout.CENTER);
        instance = this;
    }
    
    // Method for adding a screen to the layout
    public static void addScreen(String name, JPanel panel)
    {
        instance.screens.put(name, panel);
        instance.cards.add(panel, name);
    }

    // Method for showing a screen that's been added to the layout
    public static void showScreen(String name)
    {
        instance.cardLayout.show(instance.cards, name);
        instance.revalidate();
        instance.repaint();
    }
}
