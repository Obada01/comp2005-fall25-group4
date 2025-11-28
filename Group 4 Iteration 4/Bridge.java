import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Bridge
{
    // Constants
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 5;
    private static final Color DEFAULT_COLOR = new Color(102, 51, 0); // This is brown
    
    // Instance variables
    private final List<Lilipad> connections;
    private final Color bridgeColor;
    private final int width;
    private final int height;
    private boolean isPermanent;

    // Constructor
    public Bridge()
    {
        this.connections = new ArrayList<>(2); // Bridge can connect only two lilipads
        this.bridgeColor = DEFAULT_COLOR;
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.isPermanent = false; // Only needed for homepads
    }


    // Connects two lilipads logically in the bridge's structure
    public void connectLilipads(Lilipad lili1, Lilipad lili2)
    {
        
        // Prevents bridges from being placed on water tiles, or from being placed on the same lilipad
        if ((lili1.getIndexNumber() == 999 || lili2.getIndexNumber() == 999) || (lili1 == lili2)) {
            return;
        }

        connections.clear();
        connections.add(lili1);
        connections.add(lili2);
    }

    // Getter for the connections of a lilipad.
    public List<Lilipad> getConnections()
    {
        return connections;
    }

    // Getter for the bridge color.
    public Color getBridgeColor()
    {
        return bridgeColor;
    }

    // Getter for the bridge width.
    public int getWidth()
    {
        return width;
    }

    // Getter for the bridge height.
    public int getHeight()
    {
        return height;
    }

    // Getter for the permanence of a bridge.
    public boolean isPermanent()
    {
        return isPermanent;
    }

    // Setter for the permanence of a bridge. Primarily used for homepads.
    public void setPermanent(boolean permanent)
    {
        this.isPermanent = permanent;
    }

    // Disconnects a bridge from a lilipad (as long as it isn't a permanent bridge)
    public void disconnect()
    {
        if(!isPermanent)
        {
            connections.clear();
        }
    }
    
    // Checks if a bridge connects the two given lilipads.
    public boolean connects(Lilipad lili1, Lilipad lili2)
    {
        if (lili1 == null || lili2 == null)
        {
            return false;
        }
        return (connections.contains(lili1) && connections.contains(lili2));
    }
}