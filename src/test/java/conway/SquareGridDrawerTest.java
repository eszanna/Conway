package conway;

import org.junit.Before;
import org.junit.Test;

import conway.Main.SquareGrid;
import conway.Main.SquareGridDrawer;

import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SquareGridDrawerTest {
    private SquareGridDrawer drawer;
    private SquareGrid grid;
    private Graphics capturedGraphics;

    @Before
    public void setUp() {
        //initialize 
        grid = new SquareGrid(5, Color.CYAN, "Default");
        drawer = new SquareGridDrawer(grid, "Default");
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
        drawer.setsquareSize(testSize);
        assertEquals("Square size should be set to 50", testSize, drawer.getsquareSize());
    }
    
    @Test
    public void testPaintComponent() {
        //create a Graphics object
        Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
        SquareGridDrawer squares = new SquareGridDrawer(grid, "Default") {
            @Override
            public void paintComponent(Graphics g) {
                //capture it
                capturedGraphics = g;
                super.paintComponent(g);
            }
        };

        //call the method under test
        squares.paintComponent(g);

        //we just check that it is not null
        assertNotNull(capturedGraphics);
    }
}