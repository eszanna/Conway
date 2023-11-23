package com.example;

import java.util.Random;
import java.awt.*;
import java.io.Serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

class HexagonalGrid implements Serializable{
    private int size;
    private Hexagon[][] grid;
    private Color deadCellColor;
    private String selectedRule;

    public HexagonalGrid(int size, Color deadCellColor, String selectedRule) {
        this.size = size;
        this.deadCellColor = deadCellColor;
        this.selectedRule = selectedRule;
        initializeGrid();
    }

    public Color getDeadCellColor() {
        return deadCellColor;
    }

    private void initializeGrid() {
        grid = new Hexagon[2 * size - 1][2 * size - 1];

        // Calculate the center of the grid
        int centerX = size - 1;
        int centerY = size - 1;

       // Random number generator
        Random random = new Random();

        // Iterate through all hexagons
        for (int q = -size + 1; q < size; q++) {
            for (int r = -size + 1; r < size; r++) {
                int x = q + centerX;
                int y = r + centerY;

                // Determine if the hexagon should be alive or dead
                boolean isAlive = random.nextBoolean();
                
                // Create the hexagon and set its state
                grid[x][y] = new Hexagon(q, r, isAlive);
            }
        }
         
    }

    //to make sure we never have issues with overindexing
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < 2 * size - 1 && y >= 0 && y < 2 * size - 1;
    }

    public int getSize() {
        return size;
    }

    //since our coordinates are a bit tricky, we always need to convert it from 'x' to 'q' and 'y' to 'r'
    public Hexagon getHexagon(int q, int r) {
        int x = q + size - 1;
        int y = r + size - 1;
        return isWithinBounds(x, y) ? grid[x][y] : null;
    }

    //the core of the program, the most important function, the Game of Life itself
    public void updateGameOfLife(String selectedRule) {
        Hexagon[][] newGrid = new Hexagon[2 * size - 1][2 * size - 1];

        for (int q = -size + 1; q < size; q++) {
            for (int r = -size + 1; r < size; r++) {
                int x = q + size - 1;
                int y = r + size - 1;

                if (isWithinBounds(x, y)) {
                    Hexagon currentHexagon = grid[x][y];
                    int liveNeighbors = countLiveNeighbors(q, r);

                    // Apply Conway's Game of Life rules
                    boolean newState = false;
                    switch (selectedRule) {
                        case "Default":
                            // Default rules
                            newState = currentHexagon.getState() ? liveNeighbors == 2 || liveNeighbors == 3 : liveNeighbors == 3;
                            break;
                        case "High Life":
                            // High Life rules
                            newState = currentHexagon.getState() ? liveNeighbors == 2 || liveNeighbors == 3 : liveNeighbors == 3 || liveNeighbors == 6;
                            break;
                        case "Move":
                            // Move rules
                            newState = currentHexagon.getState() ? liveNeighbors == 2 || liveNeighbors == 4 || liveNeighbors == 5 : liveNeighbors == 3 || liveNeighbors == 6 || liveNeighbors == 7 || liveNeighbors == 8;
                            break;
                    }

                    //in any other case it is kaput gemacht()
                    newGrid[x][y] = new Hexagon(q, r, newState);
                }
            }
        }
        grid = newGrid;
    }
    
    //needed to determine the next state of the hexagon, because it depends on its neighbors
    private int countLiveNeighbors(int q, int r) {
        int liveNeighbors = 0;
    
        for (int i = 0; i < 6; i++) {
            int neighborQ = q + HexagonConstants.NEIGHBOR_OFFSETS_Q[i];
            int neighborR = r + HexagonConstants.NEIGHBOR_OFFSETS_R[i];
    
            if (getHexagon(neighborQ, neighborR) != null && getHexagon(neighborQ, neighborR).getState()) {
                liveNeighbors++;
            }
        }
        return liveNeighbors;
    }


    public void saveGridToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the grid to a file.");
            e.printStackTrace();
        }
    }

    public static HexagonalGrid loadGridFromFile(String filename) {
        HexagonalGrid hexagonalGrid = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            hexagonalGrid = (HexagonalGrid) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("An error occurred while loading the grid from a file.");
            e.printStackTrace();
        }
        return hexagonalGrid;
    }


    //this is just a bit of explanation of the rules on the game screen
    public String getRuleDetails(){StringBuilder ruleDetails = new StringBuilder();
        ruleDetails.append("The current rule is: ").append(this.selectedRule).append("\n\n");
    
        //details about the rule
        if (this.selectedRule.equals("Default")) {
            ruleDetails.append("The default ruleset is:\n");
            ruleDetails.append("1. If a cell is alive and it has exactly 2 or 3 live neighbors, it stays alive.\n");
            ruleDetails.append("2. If a cell is dead and it has exactly 3 live neighbors, it becomes alive.\n");
            ruleDetails.append("3. In all other cases, a cell dies or remains dead.\n");

        } else if (this.selectedRule.equals("High Life")) {
            ruleDetails.append("High Life ruleset is defined as follows:\n");
            ruleDetails.append("1. A dead cell comes to life if it has exactly 3 or 6 live neighbors.\n");
            ruleDetails.append("2. Survival: A live cell stays alive if it has 2 or 3 live neighbors.");
            ruleDetails.append("3. In all other cases, a cell dies or remains dead.\n");
        } else if (this.selectedRule.equals("Move")) {
            ruleDetails.append("High Life ruleset is defined as follows:\n");
            ruleDetails.append("1. Births: A dead cell comes to life if it has 3, 6, 7, or 8 live neighbors.\n");
            ruleDetails.append("Survival: A live cell stays alive if it has 2, 4, or 5 live neighbors.");
            ruleDetails.append("3. In all other cases, a cell dies or remains dead.\n");
        }
    
        return ruleDetails.toString();}
}
