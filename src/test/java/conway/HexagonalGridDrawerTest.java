package conway;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.image.BufferedImage;



import org.junit.Before;
import org.junit.Test;

import conway.Main.HexagonalGrid;
import conway.Main.HexagonalGridDrawer;



public class HexagonalGridDrawerTest {
	    private HexagonalGridDrawer drawer;
	    private HexagonalGrid grid;
	    private Graphics capturedGraphics;

	    @Before
	    public void setUp() {
	        // Initialize 
	        grid = new HexagonalGrid(5, Color.CYAN, "Default");
	        drawer = new HexagonalGridDrawer(grid, "Default");
	    }

	    @Test
	    public void testSetDeadCellColor() {
	        Color testColor = Color.RED;
	        drawer.setDeadCellColor(testColor);
	        assertEquals("Dead cell color should be set to RED", testColor, drawer.getDeadCellColor());
	    }

	    @Test
	    public void testSetSquareSize() {
	        int testSize = 50;
	        drawer.sethexSize(testSize);
	        assertEquals("Square size should be set to 50", testSize, drawer.gethexSize());
	    }
	    
	    @Test
	    public void testPaintComponent() {
	        //create a Graphics object
	        Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();

	        HexagonalGridDrawer hexa = new HexagonalGridDrawer(grid, "Default") {
	            @Override
	            public void paintComponent(Graphics g) {
	                //capture it
	                capturedGraphics = g;
	                super.paintComponent(g);
	            }
	        };

	        //call the method under test
	        hexa.paintComponent(g);

	        //we just check that it is not null
	        assertNotNull(capturedGraphics);
	    }
	    
	    	
	    
}

