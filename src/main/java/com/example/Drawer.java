package conway.Main;
import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;


public abstract class Drawer extends JPanel {
    protected int size; // default
    protected Color deadCellColor;
    protected double scaleFactor = 1.0;
    protected Timer timer;
    protected String selectedRule;

    public Drawer(String selectedRule) {
        this.selectedRule = selectedRule;

        //to make it zoom in and out if the grid is too big, 
        //or the size of the shapes makes it not fit the screen
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    scaleFactor *= 1.1; // Zoom in
                } else {
                    scaleFactor /= 1.1; // Zoom out
                }
                repaint();
            }
        });
    }

    public void setDeadCellColor(Color color){
        this.deadCellColor = color;
    }
    
    public Color getDeadCellColor(){
       return this.deadCellColor;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public abstract void startGame();
    public abstract void updateGrid();
}
