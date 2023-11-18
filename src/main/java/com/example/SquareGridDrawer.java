package com.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class SquareGridDrawer extends JPanel {
    private SquareGrid squareGrid;
    private int squareSize = 30; // default
    private Color deadCellColor;

    public int getsquareSize(){
        return squareSize;
    }
    
    public void setsquareSize(int size){
        this.squareSize = size;
    }

    public SquareGridDrawer(SquareGrid squareGrid, Color deadCellColor) {
        this.squareGrid = squareGrid;
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
        }, 1000, 1000); // Adjust the delay and period as needed
    }

    private void updateGrid() {
        squareGrid.updateGameOfLife();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

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
