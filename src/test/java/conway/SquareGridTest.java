package conway;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import conway.Main.Grid;
import conway.Main.SquareGrid;

	public class SquareGridTest  {
		@Test
		public void SquareGridTest () {
			SquareGrid  grid = new SquareGrid(5, Color.GREEN, "Default");
			assertEquals(5, grid.getSize());
		}
		
		@Test
	    public void testIsWithinBounds() {
			SquareGrid grid = new SquareGrid(5, Color.BLACK, "Default");
	        assertTrue(grid.isWithinBounds(0, 0));
	        assertTrue(grid.isWithinBounds(4, 4));
	        assertFalse(grid.isWithinBounds(-1, 0));
	        assertFalse(grid.isWithinBounds(0, -1));
		}
		
		@Test
	    public void testUpdateGameOfLife() {
			SquareGrid grid = new SquareGrid(5, Color.BLACK, "Default");
	        grid.updateGameOfLife("Default");
	        assertNotNull(grid.getCell(0, 0).getState());
	        assertNotNull(grid.getCell(0, 1).getState());
	        assertNotNull(grid.getCell(1, 0).getState());
	        assertNotNull(grid.getCell(1, 1).getState());
	    }

		    @Test
		    public void testGetRuleDetails() {
		    	//testing the printouts is a bit tricky, we will just test that it is not null
		        SquareGrid grid = new SquareGrid(5, Color.BLACK, "Default");
		        String result = grid.getRuleDetails();
		        //define the expected result
		        String expectedResult = "The current rule is: Default\n\nThe default ruleset is:\n1. If a cell is alive and it has exactly 2 or 3 live neighbors, it stays alive.\n2. If a cell is dead and it has exactly 3 live neighbors, it becomes alive.\n3. In all other cases, a cell dies or remains dead.\n";

		        assertEquals(expectedResult, result);      
		        SquareGrid grid2 = new SquareGrid(5, Color.BLACK, "High Life");
		        String result2 = grid2.getRuleDetails();
		        assertNotNull(result2);
		        
		        SquareGrid grid3 = new SquareGrid(5, Color.BLACK, "Move");
		        String result3 = grid3.getRuleDetails();
		        assertNotNull(result3);
			    
		    }
		    
		    @Test
		    public void testSaveAndLoadGrid() throws IOException {
		        //create a temporary file
		        File tempFile = File.createTempFile("test", ".tmp");

		        SquareGrid originalGrid = new SquareGrid(5, Color.BLACK, "Default");

		        //save it to the file
		        originalGrid.saveGridToFile(tempFile.getAbsolutePath());

		        //load a new grid from the file
		        SquareGrid loadedGrid = (SquareGrid) Grid.loadGridFromFile(tempFile.getAbsolutePath());

		        //assert that the loadedGrid is equal to the originalGrid
		        //comparing each member of the two grids
		        assertEquals(originalGrid.getSize(), loadedGrid.getSize());
		        assertEquals(originalGrid.getDeadCellColor(), loadedGrid.getDeadCellColor());
		        assertEquals(originalGrid.getselectedRule(), loadedGrid.getselectedRule());

		        //delete the temporary file
		        tempFile.delete();
		    }
		

	}