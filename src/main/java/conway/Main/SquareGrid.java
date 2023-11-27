package conway.Main;
import java.awt.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
	
	//class responsible for storing the individual living squares
	public class SquareGrid extends Grid{
	  private Square[][] grid;
	
	  public SquareGrid(int size, Color deadCellColor, String selectedRule) {
		super(size, deadCellColor, selectedRule);
        initializeGrid();
	  }
	  
	  public Square getCell(int i, int j) {
	      return isWithinBounds(i, j) ? grid[i][j] : null;
	  }
	  
	  @Override
	  public void initializeGrid() {
	      grid = new Square[size][size];
	
	     //random number generator
	      Random random = new Random();
	
	      //iterate through all squares
	      for (int i = 0; i < size; i++) {
	          for (int j = 0; j < size; j++) {
	              // Determine if the square should be alive or dead
	              boolean isAlive = random.nextBoolean();
	              
	              // Create the square and set its state
	              grid[i][j] = new Square(i, j, isAlive);
	          }
	      }
	       
	  }
	
	  @Override
	  public boolean isWithinBounds(int x, int y) {
	      return x >= 0 && x < size && y >= 0 && y < size;
	  }
	
	  //the core of our program, the main point of everything is in here, where Game of Life is playing :)
	  public void updateGameOfLife(String selectedRule) {
	      Square[][] newGrid = new Square[size][size];
	      Map<String, Integer> liveNeighborCounts = new HashMap<>();
	
	      //first, count the live neighbors for each cell
	      for (int i = 0; i < size; i++) {
	          for (int j = 0; j < size; j++) {
	              int liveNeighbors = countLiveNeighbors(i, j);
	              liveNeighborCounts.put(i + "," + j, liveNeighbors);
	          }
	      }
	
	      //then, update the state of each cell based on the count of live neighbors
	      for (int i = 0; i < size; i++) {
	          for (int j = 0; j < size; j++) {
	              Square currentSquare = grid[i][j];
	              int liveNeighbors = liveNeighborCounts.getOrDefault(i + "," + j, 0);
	
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
	
	  //necessary to determine the next state of each square
	  public int countLiveNeighbors(int i, int j) {
	      int liveNeighbors = 0;
	  
	      for (int x = -1; x <= 1; x++) {
	          for (int y = -1; y <= 1; y++) {
	              if (x == 0 && y == 0) continue;
	              if (getCell(i + x, j + y) != null && getCell(i + x, j + y).getState()) {
	                  liveNeighbors++;
	              }
	          }
	      }
	      return liveNeighbors;
	  }

	
	}
	
