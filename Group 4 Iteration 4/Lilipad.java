import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.swing.*;
import java.util.ArrayList;

public class Lilipad extends JButton
{
    private int indexNumber;
    private ArrayList<Frog> frogs;
    private ArrayList<Bridge> bridges;
    private boolean isStarterPad;
    private boolean highlighted;

    public Lilipad(Color color, int indexNumber, boolean isStarterPad)
    {
        this.indexNumber=indexNumber;
        this.frogs = new ArrayList<>();
        this.bridges=new ArrayList<>();
        this.isStarterPad=isStarterPad;
        this.highlighted = false;
        this.setBackground(color);
    }

    public void addFrogDirectly(Frog frog)
    {
        if (frog != null && (isStarterPad || !isOccupied()))
        {
            if (!frogs.contains(frog))
            {
                this.frogs.add(frog);
                repaint();
            }
        }
    }
    
    public void addFrog(Frog frog)
    {
        if (frog != null)
        {
            if (frogs.contains(frog))
            {
                return;
            }
            
            if (isStarterPad || !isOccupied())
            {
                Lilipad currentPad = frog.getCurrentLilipad();
                if (currentPad != null)
                {
                    currentPad.removeFrog(frog);
                }
                
                addFrogDirectly(frog);
                frog.moveTo(this);
                repaint();
            }
        }
    }
    
    public void removeFrog(Frog frog)
    {
        if (frog != null) {
            this.frogs.remove(frog);
            repaint();
        }
    }

    public void addBridge(Bridge bridge)
    {
        if(!bridges.contains(bridge))
        {
            bridges.add(bridge);
        }
    }

    public void removeBridge(Bridge bridge)
    {
        bridges.remove(bridge);
    }
    
    public Bridge getBridgeTo(Lilipad other)
    {
        if (other == null)
        {
            return null;
        }

        for (Bridge bridge : bridges)
        {
            if (bridge.connects(this, other))
            {
                return bridge;
            }
        }
        return null;
    }
    
    public boolean isOccupied()
    {
        if (isStarterPad)
        {
            return frogs.size() >= 3;
        }
        return !frogs.isEmpty();
    }
    
    public ArrayList<Frog> getFrogs()
    {
        return new ArrayList<>(frogs);
    }

    public int getIndexNumber()
    {
        return indexNumber;
    }

    public ArrayList<Frog> getOccupants()
    {
        return new ArrayList<>(frogs);
    }

    public ArrayList<Bridge> getBridges()
    {
        return bridges;
    }

    public boolean getIsStarterPad()
    {
        return isStarterPad;
    }
    
    public void setHighlighted(boolean highlighted)
    {
        this.highlighted = highlighted;
        repaint();
    }
    
    public boolean isHighlighted()
    {
        return highlighted;
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        setText("");
        
        if (highlighted)
        {
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth();
            int h = getHeight();
            int size = Math.min(w, h) - 6;
            int x = (w - size) / 2;
            int y = (h - size) / 2;
            g2.setColor(Color.WHITE);
            g2.setStroke(new java.awt.BasicStroke(3f));
            g2.drawOval(x, y, size, size);
            g2.dispose();
        }

        if (!frogs.isEmpty())
        {
            int frogCount = frogs.size();
            int width = getWidth();
            int height = getHeight();
            int size = Math.min(width, height) * 2 / 3;
            
            if (isStarterPad && frogCount > 1)
            {
                size = Math.min(width, height) * 1 / 2;
            }
            
            for (int i = 0; i < frogCount; i++)
            {
                Frog frog = frogs.get(i);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(frog.getColor());
                
                double offsetX = 0;
                double offsetY = 0;
                
                if (isStarterPad && frogCount > 1)
                {
                    double angle = 2 * Math.PI * i / frogCount;
                    double radius = size * 0.4;
                    offsetX = Math.cos(angle) * radius;
                    offsetY = Math.sin(angle) * radius;
                }
                
                int centerX = width / 2 + (int)offsetX;
                int centerY = height / 2 + (int)offsetY;
                double triangleHeight = (Math.sqrt(3) / 2) * size;

                int[] xPoints = {centerX, (int)(centerX - size / 2.0), (int)(centerX + size / 2.0)};
                
                int[] yPoints = {(int)(centerY - triangleHeight / 2.0), (int)(centerY + triangleHeight / 2.0), 
                    (int)(centerY + triangleHeight / 2.0)};
                
                Polygon triangle = new Polygon(xPoints, yPoints, 3);
                
                g2d.fill(triangle);
                
                Color originalColor = g2d.getColor();
                java.awt.Stroke originalStroke = g2d.getStroke();
                Font originalFont = g2d.getFont();
                
                if (frog.isSelected())
                {
                    Color highlight = frog.getColor().brighter().brighter();
                    g2d.setColor(highlight);
                    g2d.setStroke(new java.awt.BasicStroke(4f));
                    g2d.drawPolygon(triangle);
                }
                
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new java.awt.BasicStroke(2f));
                g2d.drawPolygon(triangle);
                
                if (getParent() != null && getParent().getParent() instanceof GameFrame)
                {
                    GameFrame gameFrame = (GameFrame) getParent().getParent();
                    
                    if (gameFrame.isColorblindMode())
                    {
                        int playerNum = frog.getPlayerNumber();
                        String playerLetter = String.valueOf((char)('A' + playerNum - 1));
                        Font font = new Font("Arial", Font.BOLD, size / 2);
                        g2d.setFont(font);
                    
                        java.awt.geom.Rectangle2D textBounds = g2d.getFontMetrics().getStringBounds(playerLetter, g2d);
                        int textX = centerX - (int)(textBounds.getWidth() / 2);
                        int textY = centerY + (int)(textBounds.getHeight() / 4);
                                               
                        int padding = 2;
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(textX - padding, textY - (int)textBounds.getHeight() + padding, 
                        (int)textBounds.getWidth() + padding*2, (int)textBounds.getHeight());
                        
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(playerLetter, textX, textY);
                    }
                }
                g2d.setColor(originalColor);
                g2d.setStroke(originalStroke);
                g2d.setFont(originalFont);
                g2d.dispose();
            }
        }
    }
}