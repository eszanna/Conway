package com.example;

import java.util.Random;
import java.awt.*;
import java.io.Serializable;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


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

    public HexagonalGrid(int size) {
        this.size = size;
        this.deadCellColor = Color.WHITE;
        initializeGridforFile();
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

    //Because we will not store the chosen color in the file, it is white by default
        private void initializeGridforFile() {
        grid = new Hexagon[2 * size - 1][2 * size - 1];

        // Calculate the center of the grid
        int centerX = size - 1;
        int centerY = size - 1;

        // Iterate through all hexagons
        for (int q = -size + 1; q < size; q++) {
            for (int r = -size + 1; r < size; r++) {
                int x = q + centerX;
                int y = r + centerY;

                boolean isAlive = false;
                grid[x][y] = new Hexagon(q, r, isAlive);
            }
        }
         
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < 2 * size - 1 && y >= 0 && y < 2 * size - 1;
    }

    public int getSize() {
        return size;
    }

    //since our coordinates are a bit tricky, we always need to convert it from x to q and y to r
    public Hexagon getHexagon(int q, int r) {
        int x = q + size - 1;
        int y = r + size - 1;
        return isWithinBounds(x, y) ? grid[x][y] : null;
    }

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
        try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            writer.println(size);
            // Save the color as three separate integers
            writer.println(deadCellColor.getRed() + "," + deadCellColor.getGreen() + "," + deadCellColor.getBlue());
            writer.println(selectedRule);
    
            for (int q = -size + 1; q < size; q++) {
                for (int r = -size + 1; r < size; r++) {
                    int x = q + size - 1;
                    int y = r + size - 1;
                    if (isWithinBounds(x, y)) {
                        Hexagon hexagon = grid[x][y];
                        writer.println(q + "," + r + "," + (hexagon.getState() ? "alive" : "dead"));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving the grid to a file.");
            e.printStackTrace();
        }
    }
    public static HexagonalGrid loadGridFromFile(String filename) {
        HexagonalGrid hexagonalGrid = null;
        try (Scanner scanner = new Scanner(new File(filename))) {
            //reading the size
            int size = Integer.parseInt(scanner.nextLine());
            //reading the color
            String[] colorComponents = scanner.nextLine().split(",");
            int  red = Integer.parseInt(colorComponents[0]); System.out.println("Red" + red);
            int  green = Integer.parseInt(colorComponents[1]); System.out.println("Green" + green);
            int  blue = Integer.parseInt(colorComponents[2]); System.out.println("Blue" + blue);
            Color selectedColor = new Color(red, green, blue);
            //loading the ruleset
            String rule = scanner.nextLine();
            hexagonalGrid = new HexagonalGrid(size, selectedColor, rule);
    
            //reading the state of each cell in their positions
            for (int q = -size + 1; q < size; q++) {
                for (int r = -size + 1; r < size; r++) {
                    int x = q + size - 1;
                    int y = r + size - 1;
                    hexagonalGrid.grid[x][y].setState(false);
                }
            }
    
            // Update cells according to the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int q = Integer.parseInt(parts[0]);
                int r = Integer.parseInt(parts[1]);
                boolean isAlive = parts[2].equals("alive");
            
                int x = q + size - 1;
                int y = r + size - 1;
            
                hexagonalGrid.grid[x][y].setState(isAlive);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while loading the grid from a file.");
            e.printStackTrace();
        }
        return hexagonalGrid;
    }

    public String getRuleDetails(){StringBuilder ruleDetails = new StringBuilder();
        ruleDetails.append("The current rule is: ").append(this.selectedRule).append("\n\n");
    
        // Add details about the rule
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
