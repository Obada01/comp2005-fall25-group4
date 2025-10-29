import java.awt.Color;
import java.util.ArrayList;

public class Lilipad {
    private Color color;
    private int indexNumber;
    private boolean isOccupied;
    private ArrayList<Bridge> bridges;
    private boolean isStarterPad;

    public Lilipad(Color color, int indexNumber, boolean isStarterPad){
        this.color=color;
        this.indexNumber=indexNumber;
        this.isOccupied=false;
        this.bridges=new ArrayList<>();
        this.isStarterPad=isStarterPad;
    }

    public void isOccupied(Frog frog){
        this.isOccupied=(frog!=null);
    }

    public void setColor(Color color){
        this.color=color;
    }

    public void addBridge(Bridge bridge){
        if(!bridges.contains(bridge)){
            bridges.add(bridge);
        }
    }

    public void removeBridge(Bridge bridge){
        bridges.remove(bridge);
    }

    public Color getColor(){
        return color;
    }

    public int getIndexNumber(){
        return indexNumber;
    }

    public boolean getIsOccupied(){
        return isOccupied;
    }

    public ArrayList<Bridge> getBridges(){
        return bridges;
    }

    public boolean getIsStarterPad(){
        return isStarterPad;
    }
}

