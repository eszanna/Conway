package conway;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import conway.Main.Hexagon;

public class HexagonTest {
	@Before
	public void setUp() {
		Hexagon hexagon = new Hexagon(1, 2, true);
	}
	
	@Test
	public void testHexagon() {
        Hexagon hexagon = new Hexagon(1, 2, true);
        
        // Test getQ method
        assertEquals(0, hexagon.getQ());
        
        // Test getR method
        assertEquals(2, hexagon.getR());
        
        // Test getState method
        assertTrue(hexagon.getState());
        
        // Test setState method
        hexagon.setState(false);
        assertFalse(hexagon.getState());
    }
	
}
