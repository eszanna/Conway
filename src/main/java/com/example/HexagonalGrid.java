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

    public HexagonalGrid(int size, Color deadCellColor) {
        this.size = size;
        this.deadCellColor = deadCellColor;
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


    public Hexagon getHexagon(int q, int r) {
        int x = q + size - 1;
        int y = r + size - 1;
        return isWithinBounds(x, y) ? grid[x][y] : null;
    }

    public void updateGameOfLife() {

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
                    //if alive:
                    if (currentHexagon.getState()) { 
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
            int size = Integer.parseInt(scanner.nextLine());
            hexagonalGrid = new HexagonalGrid(size);
    
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
}
