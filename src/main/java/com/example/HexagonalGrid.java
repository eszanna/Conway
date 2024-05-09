package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.awt.*;


public class HexagonalGrid extends Grid{
    private Hexagon[][] grid;

    public HexagonalGrid(int size, Color deadCellColor, String selectedRule) {
        super(size, deadCellColor, selectedRule);
        initializeGrid();
    }

    @Override
	public void initializeGrid() {
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
    @Override
    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < 2 * size - 1 && y >= 0 && y < 2 * size - 1;
    }

    //since our coordinates are a bit tricky, we always need to convert it from 'x' to 'q' and 'y' to 'r'
    public Hexagon getHexagon(int q, int r) {
        int x = q + size - 1;
        int y = r + size - 1;
        return isWithinBounds(x, y) ? grid[x][y] : null;
    }

    //the core of the program, the most important function, the Game of Life itself
    @Override
    public void updateGameOfLife(String selectedRule) {
        Hexagon[][] newGrid = new Hexagon[2 * size - 1][2 * size - 1];
        Map<String, Integer> liveNeighborCounts = new HashMap<>();

        //first, count the live neighbors for each cell
        for (int q = -size + 1; q < size; q++) {
            for (int r = -size + 1; r < size; r++) {
                int x = q + size - 1;
                int y = r + size - 1;

                if (isWithinBounds(x, y)) {
                    int liveNeighbors = countLiveNeighbors(q, r);
                    liveNeighborCounts.put(q + "," + r, liveNeighbors);
                }
            }
        }

        //then, update the state of each cell based on the count of live neighbors
        for (int q = -size + 1; q < size; q++) {
            for (int r = -size + 1; r < size; r++) {
                int x = q + size - 1;
                int y = r + size - 1;

                if (isWithinBounds(x, y)) {
                    Hexagon currentHexagon = grid[x][y];
                    int liveNeighbors = liveNeighborCounts.getOrDefault(q + "," + r, 0);

                    //apply Conway's Game of Life rules
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
}
