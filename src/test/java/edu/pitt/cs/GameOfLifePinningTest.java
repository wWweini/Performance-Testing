package edu.pitt.cs;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameOfLifePinningTest {
	/*
	 * READ ME: You may need to write pinning tests for methods from multiple
	 * classes, if you decide to refactor methods from multiple classes.
	 * 
	 * In general, a pinning test doesn't necessarily have to be a unit test; it can
	 * be an end-to-end test that spans multiple classes that you slap on quickly
	 * for the purposes of refactoring. The end-to-end pinning test is gradually
	 * refined into more high quality unit tests. Sometimes this is necessary
	 * because writing unit tests itself requires refactoring to make the code more
	 * testable (e.g. dependency injection), and you need a temporary end-to-end
	 * pinning test to protect the code base meanwhile.
	 * 
	 * For this deliverable, there is no reason you cannot write unit tests for
	 * pinning tests as the dependency injection(s) has already been done for you.
	 * You are required to localize each pinning unit test within the tested class
	 * as we did for Deliverable 2 (meaning it should not exercise any code from
	 * external classes). You will have to use Mockito mock objects to achieve this.
	 * 
	 * Also, you may have to use behavior verification instead of state verification
	 * to test some methods because the state change happens within a mocked
	 * external object. Remember that you can use behavior verification only on
	 * mocked objects (technically, you can use Mockito.verify on real objects too
	 * using something called a Spy, but you wouldn't need to go to that length for
	 * this deliverable).
	 */

	/* TODO: Declare all variables required for the test fixture. */
	MainPanel newPanel;

	@Before
	public void setUp() {
		/*
		 * TODO: initialize the text fixture. For the initial pattern, use the "blinker"
		 * pattern shown in:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Examples_of_patterns
		 * The actual pattern GIF is at:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#/media/File:Game_of_life_blinker.gif
		 * Start from the vertical bar on a 5X5 matrix as shown in the GIF.
		 */
		Cell[][] tempData = new Cell[5][5];
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				tempData[x][y] = Mockito.mock(Cell.class); 
				//Blinker pattern
				if (x == 2 && y > 0 && y < 4) {  
					//Marked as alive
					Mockito.when(tempData[x][y].getAlive()).thenReturn(true); 
				} else {
					//Marked as dead
					Mockito.when(tempData[x][y].getAlive()).thenReturn(false); 
				}
			}
		}
		newPanel  = new MainPanel(tempData);
	}

	/* TODO: Write the three pinning unit tests for the three optimized methods */
	
	/* Cell.toString()
	 * MainPanel.calculateNextIteration()
	 * MainPanel.iterateCell(int,int)
	 */

	/**
	 * Test case that a live cell returns "X".
	 * Preconditions: Cell is marked as alive.
	 * Execution steps: Call toString method on the cell
	 * Postconditions: toString method should return "X"
	 */
	@Test
	public void testAliveCellToString(){

		Cell aliveCell = new Cell();
		aliveCell.setAlive(true);

		assertEquals("X", aliveCell.toString());
	}

	/**
	 * Test case that a dead cell returns ".".
	 * Preconditions: Cell is marked as dead.
	 * Execution steps: Call toString method on the cell
	 * Postconditions: toString method should return "."
	 */
	@Test
	public void testDeadCellToString(){

		Cell deadCell = new Cell();
		deadCell.setAlive(false);

		assertEquals(".", deadCell.toString());
	}

	/**
	 * Test case for calculate the next iteration
	 * Preconditions: The newPanel is in a 5x5 vertical blinker pattern
	 * Execution steps: Run newPanel.calculateNextIteration()
	 * Postconditions: Verify that the board is in a 5x5 horizontal blinker pattern
	 */
	@Test
	public void calculateNextIterationTest(){

		// Run the next iteration
		newPanel.calculateNextIteration();

		// Get the cells
		Cell[][] cells = newPanel.getCells();

		for (int x = 0; x < newPanel.getCellsSize(); x++) {
			for (int y = 0; y < newPanel.getCellsSize(); y++) {
				// Horizontal blinker pattern in the next iteration
				if (y == 2 && x > 0 && x < 4) { 
					Mockito.verify(cells[x][y], times(1)).setAlive(true); 
				} else {
					Mockito.verify(cells[x][y], times(1)).setAlive(false); 
				}
			}
		}
	}

	/**
	 * Test case for a living cell that should die after iteration
	 * Preconditions: The newPanel is in a 5x5 vertical blinker pattern
	 * Execution steps: Call newPanel.iterateCell(2, 1);
	 * Postconditions: Cell is dead
	 */
	@Test
	public void iterateCellAliveToDeadTest() {

		Cell cell = newPanel.getCells()[2][1];
		// The cell is alive
		assertTrue(cell.getAlive());

		boolean result = newPanel.iterateCell(2, 1);

		// Living cell should die after iteration
		assertFalse(result);
	}

	/**
	 * Test case for a dead cell that should become living cell after iteration
	 * Preconditions: The newPanel is in a 5x5 vertical blinker pattern
	 * Execution steps: Call newPanel.iterateCell(3, 2);
	 * Postconditions: Cell is alive
	 */
	@Test
	public void iterateCellDeadToAliveTest() {

		Cell cell = newPanel.getCells()[3][2];

		assertFalse(cell.getAlive());

		boolean result = newPanel.iterateCell(3, 2);

		// Dead cell should live after iteration
		assertTrue(result);
		
	}

	/**
	 * Test case for a living cell that should not die after iteration
	 * Preconditions: The newPanel is in a 5x5 vertical blinker pattern
	 * Execution steps: Call newPanel.iterateCell(2, 2);
	 * Postconditions: Cell is stll alive
	 */
	@Test
	public void iterateCellTheSameAliveTest() {

		Cell cell = newPanel.getCells()[2][2];
		// The cell is alive
		assertTrue(cell.getAlive());

		boolean result = newPanel.iterateCell(2, 2);
		// Living cell that should still live after iteration
		assertTrue(result);
	}

	/**
	 * Test case for a dead cell that should still be dead after iteration
	 * Preconditions: The newPanel is in a 5x5 vertical blinker pattern
	 * Execution steps: Call newPanel.iterateCell(1, 4);
	 * Postconditions: Cell is stll alive
	 */
	@Test
	public void iterateCellTheSameDeadTest() {

		Cell cell = newPanel.getCells()[1][4];
		// The cell is dead
		assertFalse(cell.getAlive());

		boolean result = newPanel.iterateCell(1, 4);
		// Living cell that should still live after iteration
		assertFalse(result);
	}











	


}
