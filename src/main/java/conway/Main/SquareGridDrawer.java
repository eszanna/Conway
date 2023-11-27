
package conway.Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.util.Timer;
import java.util.TimerTask;


//class responsible for drawing out our Grid
public class SquareGridDrawer extends Drawer{
    private SquareGrid squareGrid;
    private int squareSize = 30; // default

    public int getsquareSize(){
        return squareSize;
    }
    
    public void setsquareSize(int size){
        this.squareSize = size;
    }

    public SquareGridDrawer(SquareGrid squareGrid, String selectedRule) {
        super(selectedRule);
        this.squareGrid = squareGrid;
    }

    //if the game is too fast or too slow, we can set it here
    public void startGame() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateGrid();
                repaint();
            }
        }, 25, 25); // <--- adjust the delay and period as needed
    }

    @Override
    public void updateGrid() {
        squareGrid.updateGameOfLife(selectedRule);
    }

    @Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(scaleFactor, scaleFactor);

        //to center it in the middle
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int totalGridSize = squareGrid.getSize() * squareSize;
        int startX = (panelWidth - totalGridSize) / 2;
        int startY = (panelHeight - totalGridSize) / 2;

        for (int i = 0; i < squareGrid.getSize(); i++) {
            for (int j = 0; j < squareGrid.getSize(); j++) {
                boolean isAlive = squareGrid.getCell(i, j).getState();
                drawSquare(g2d, startX + i * squareSize, startY + j * squareSize, isAlive);
            }
        }
    }

    //and this is the unique function which is responsible to draw the squares one by one
    private void drawSquare(Graphics2D g2d, int x, int y, boolean isAlive) {
        Rectangle square = new Rectangle(x, y, squareSize, squareSize);

        if (isAlive) {
            g2d.setColor(Color.BLACK);
            g2d.fill(square);
        } else {
            g2d.setColor(deadCellColor);
            g2d.fill(square);
            g2d.setColor(Color.BLACK);
            g2d.draw(square);            
        }
    }
}
