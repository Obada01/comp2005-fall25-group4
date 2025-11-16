import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.Color;

public class Bridge {
    private ArrayList<Lilipad> connections;
    private Color bridgeColor;
    private int width;
    private int height;
    private boolean isAPermanentBridge;

    public Bridge() {
        this.connections= new ArrayList<>(2); // bridge can connect only two lilipads
        this.bridgeColor= new Color(102,51,0);
        this.width= 10;
        this.height= 5;
        this.isAPermanentBridge= false;
    }

    public void connectLiliPads(Lilipad lPad1, Lilipad lPad2) {
        connections.add(lPad1);
        connections.add(lPad2);
    }

    public ArrayList<Lilipad> getConnections() {
        return connections;
    }

    public Color getBridgeColor() {
        return bridgeColor;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isAPermanentBridge() {
        return isAPermanentBridge;
    }

    // setters for making bridges permanent. Specially for the bridges connecting to the home lilipads
    public void setAsPermanentBridge(boolean permanentBridge) {
        this.isAPermanentBridge= permanentBridge;
    }
}
