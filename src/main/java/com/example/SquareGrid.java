package com.example;

import java.util.Random;
import java.util.Scanner;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;


class SquareGrid implements Serializable{
    private int size;
    private Square[][] grid;
    private Color deadCellColor;
    private String selectedRule;

    public SquareGrid(int size, Color deadCellColor, String selectedRule) {
        this.size = size;
        this.deadCellColor = deadCellColor;
        this.selectedRule = selectedRule;
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

    public void updateGameOfLife(String selectedRule) {
        Square[][] newGrid = new Square[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square currentSquare = grid[i][j];
                int liveNeighbors = countLiveNeighbors(i, j);

                // Apply Conway's Game of Life rules
                boolean newState = false;
                //if alive:
                switch (selectedRule) {
                    case "Default":
                        // Default rules
                        newState = currentSquare.getState() ? liveNeighbors == 2 || liveNeighbors == 3 : liveNeighbors == 3;
                        break;
                    case "High Life":
                        // High Life rules
                        newState = currentSquare.getState() ? liveNeighbors == 2 || liveNeighbors == 3 : liveNeighbors == 3 || liveNeighbors == 6;
                        break;
                    case "Move":
                        // Move rules
                        newState = currentSquare.getState() ? liveNeighbors == 2 || liveNeighbors == 4 || liveNeighbors == 5 : liveNeighbors == 3 || liveNeighbors == 6 || liveNeighbors == 7 || liveNeighbors == 8;
                        break;
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

    public void saveSquareGridToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            writer.println(size);
            writer.println(deadCellColor.getRed() + "," + deadCellColor.getGreen() + "," + deadCellColor.getBlue());
            writer.println(selectedRule);
    
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Square square = grid[i][j];
                    writer.println(i + "," + j + "," + (square.getState() ? "alive" : "dead"));
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving the grid to a file.");
            e.printStackTrace();
        }
    }

    public static SquareGrid loadSquareGridFromFile(String filename) {
        SquareGrid squareGrid = null;
        try (Scanner scanner = new Scanner(new File(filename))) {
            int size = Integer.parseInt(scanner.nextLine());
            String[] colorComponents = scanner.nextLine().split(",");
            int  red = Integer.parseInt(colorComponents[0]); System.out.println("Red " + red);
            int  green = Integer.parseInt(colorComponents[1]); System.out.println("Green " + green);
            int  blue = Integer.parseInt(colorComponents[2]); System.out.println("Blue " + blue);
            Color selectedColor = new Color(red, green, blue);
            //loading the ruleset
            String rule = scanner.nextLine();
            squareGrid = new SquareGrid(size, selectedColor, rule);
        
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    squareGrid.grid[i][j].setState(false);
                }
            }
    
            // Update cells according to the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int i = Integer.parseInt(parts[0]);
                int j = Integer.parseInt(parts[1]);
                boolean isAlive = parts[2].equals("alive");
            
                squareGrid.grid[i][j].setState(isAlive);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while loading the grid from a file.");
            e.printStackTrace();
        }
        return squareGrid;
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

