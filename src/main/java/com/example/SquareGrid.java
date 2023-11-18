package com.example;

import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;


class SquareGrid {
    private int size;
    private Square[][] grid;
    private Color deadCellColor;

    public SquareGrid(int size, Color deadCellColor) {
        this.size = size;
        this.deadCellColor = deadCellColor;
        initializeGrid();
    }


    public Color getDeadCellColor() {
        return deadCellColor;
    }
    public Square getCell(int i, int j) {
        return isWithinBounds(i, j) ? grid[i][j] : null;
    }
    
    private void initializeGrid() {
        grid = new Square[size][size];

       // Random number generator
        Random random = new Random();

        // Iterate through all squares
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Determine if the square should be alive or dead
                boolean isAlive = random.nextBoolean();
                
                // Create the square and set its state
                grid[i][j] = new Square(i, j, isAlive);
            }
        }
         
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public int getSize() {
        return size;
    }


    public Square getSquare(int i, int j) {
        return isWithinBounds(i, j) ? grid[i][j] : null;
    }

    public void updateGameOfLife() {
        Square[][] newGrid = new Square[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square currentSquare = grid[i][j];
                int liveNeighbors = countLiveNeighbors(i, j);

                // Apply Conway's Game of Life rules
                boolean newState = false;
                //if alive:
                if (currentSquare.getState()) { 
                    // Any live cell with fewer than two live neighbors dies (underpopulation)
                    // Any live cell with two or three live neighbors lives on to the next generation
                    // Any live cell with more than three live neighbors dies (overpopulation)
                    newState = liveNeighbors == 2 || liveNeighbors == 3;

                //if dead:
                } else {
                    // Any dead cell with exactly three live neighbors becomes a live cell (reproduction)
                    newState = liveNeighbors == 3;
                }

                //in any other case it is kaput gemacht()
                newGrid[i][j] = new Square(i, j, newState);
            }
        }

        grid = newGrid;
    
    }   
    private int countLiveNeighbors(int i, int j) {
        int liveNeighbors = 0;
    
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue;
                if (getSquare(i + x, j + y) != null && getSquare(i + x, j + y).getState()) {
                    liveNeighbors++;
                }
            }
        }
    
        return liveNeighbors;
    }
}
