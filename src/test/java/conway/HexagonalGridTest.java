package conway;
import static org.junit.Assert.*;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import conway.Main.Grid;
import conway.Main.HexagonalGrid;

public class HexagonalGridTest {
	private HexagonalGrid grid;
	
	@Before
	public void setUp() {
		grid = new HexagonalGrid(5, Color.GREEN, "Default");
	}
	
	@Test
	public void HexagonalGridConstructorTest() {
		assertEquals(5, grid.getSize());
	}
	
	@Test
    public void testIsWithinBounds() {
        assertTrue(grid.isWithinBounds(0, 0));
        assertTrue(grid.isWithinBounds(4, 4));
        assertFalse(grid.isWithinBounds(-1, 0));
        assertFalse(grid.isWithinBounds(0, -1));
	}
	
	@Test
    public void testUpdateGameOfLife() {
        grid.updateGameOfLife("Default");
        assertNotNull(grid.getHexagon(0, 0).getState());
        assertNotNull(grid.getHexagon(0, 1).getState());
        assertNotNull(grid.getHexagon(1, 0).getState());
        assertNotNull(grid.getHexagon(1, 1).getState());
    }
	
	@Test
    public void testGetRuleDetails() {
    	//testing the printouts is a bit tricky, we will just test that it is not null
        String result = grid.getRuleDetails();
        
        //let's do one for sure, here we define the expected result
        String expectedResult = "The current rule is: Default\n\nThe default ruleset is:\n1. If a cell is alive and it has exactly 2 or 3 live neighbors, it stays alive.\n2. If a cell is dead and it has exactly 3 live neighbors, it becomes alive.\n3. In all other cases, a cell dies or remains dead.\n";

        assertEquals(expectedResult, result);      
        HexagonalGrid grid2 = new HexagonalGrid(5, Color.BLACK, "High Life");
        String result2 = grid2.getRuleDetails();
        assertNotNull(result2);
        
        HexagonalGrid grid3 = new HexagonalGrid(5, Color.BLACK, "Move");
        String result3 = grid3.getRuleDetails();
        assertNotNull(result3);
	    
    }
	
	@Test
    public void testSaveAndLoadGrid() throws IOException {
        //create a temporary file
        File tempFile = File.createTempFile("test", ".tmp");

        //save it to the file
        grid.saveGridToFile(tempFile.getAbsolutePath());

        //load a new grid from the file
        HexagonalGrid loadedGrid = (HexagonalGrid) Grid.loadGridFromFile(tempFile.getAbsolutePath());

        //assert that the loadedGrid is equal to the originalGrid
        //comparing each member of the two grids
        assertEquals(grid.getSize(), loadedGrid.getSize());
        assertEquals(grid.getDeadCellColor(), loadedGrid.getDeadCellColor());
        assertEquals(grid.getselectedRule(), loadedGrid.getselectedRule());

        //delete the temporary file
        tempFile.delete();
    }
}
