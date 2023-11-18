package com.example;
import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.Timer;


public class HexagonalGridDrawer extends JPanel {
    private HexagonalGrid hexagonalGrid;
    private int hexSize = 30; // default
    private Color deadCellColor;


    public int gethexSize(){
        return hexSize;
    }
    
    public void sethexSize(int size){
        this.hexSize = size;
    }

    public HexagonalGridDrawer(HexagonalGrid hexagonalGrid, Color deadCellColor) {
        this.hexagonalGrid = hexagonalGrid;
        this.deadCellColor = deadCellColor;
    }

    public void startGame() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateGrid();
                repaint();
            }
        }, 1000, 1000); 
    }

    private void updateGrid() {
        hexagonalGrid.updateGameOfLife();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    
        int xOffset = getWidth() / 2;
        int yOffset = getHeight() / 2;
    
        for (int q = -hexagonalGrid.getSize() + 1; q < hexagonalGrid.getSize(); q++) {
            for (int r = -hexagonalGrid.getSize() + 1; r < hexagonalGrid.getSize(); r++) {
                int[] hexCoords = hexToPixel(q, r);
                int x = hexCoords[0] + xOffset;
                int y = hexCoords[1] + yOffset;
                boolean isAlive = hexagonalGrid.getHexagon(q, r).getState();
                drawHexagon(g2d, x, y, isAlive);
            }
        }
    }
    private int[] hexToPixel(int q, int r) {
        int x = (int) (hexSize * 3.0 / 2 * q);
        int y = (int) (hexSize * Math.sqrt(3) * (r + 0.5 * (q % 2)));
        return new int[]{x, y};
    }

    private void drawHexagon(Graphics2D g2d, int x, int y, boolean isAlive) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];

        for (int i = 0; i < 6; i++) {
            double angle = 2.0 * Math.PI / 6 * i;
            xPoints[i] = (int) (x + hexSize * Math.cos(angle));
            yPoints[i] = (int) (y + hexSize * Math.sin(angle));
        }

        Polygon hexagon = new Polygon(xPoints, yPoints, 6);
        
        if (isAlive) {
            g2d.setColor(Color.BLACK);
            g2d.fill(hexagon);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.draw(hexagon);

            g2d.setColor(deadCellColor);
            g2d.fill(hexagon);
        }
    }
}