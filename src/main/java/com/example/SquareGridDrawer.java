package com.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

//class responsible for drawing out our Grid
public class SquareGridDrawer extends JPanel {
    private SquareGrid squareGrid;
    private int squareSize = 30; // default
    private Color deadCellColor;
    private double scaleFactor = 1.0; //for zooming in and out
    private Timer timer;
    private String selectedRule;

    public void setdeadCellColor(Color color){
        this.deadCellColor = color;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
    public int getsquareSize(){
        return squareSize;
    }
    
    public void setsquareSize(int size){
        this.squareSize = size;
    }

    public SquareGridDrawer(SquareGrid squareGrid, String selectedRule) {
        this.squareGrid = squareGrid;
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

    //if the game is too fast or too slow, we can set it here
    public void startGame() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateGrid();
                repaint();
            }
        }, 1000, 1000); // <--- adjust the delay and period as needed
    }

    private void updateGrid() {
        squareGrid.updateGameOfLife(selectedRule);
    }

    @Override
    protected void paintComponent(Graphics g) {
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
