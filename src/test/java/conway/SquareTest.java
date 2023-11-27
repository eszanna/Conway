package conway;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import conway.Main.Square;


public class SquareTest {
	private Square square;
	
	@Before
	public void setUp() {
		square = new Square(1, 2, true);
	}
	
	@Test
	public void testSquare() {
        assertTrue(square.getState());
        square.setState(false);
        assertFalse(square.getState());
    }
	
}
