package playful;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class MyLinkedListTest {

	// TODO: Make tests for MyLinkedList class

	MyLinkedList<String> list;
	// an array of Strings to test the list (LN = line)
	private static String[] LN;

	@BeforeClass
	public static void setupLines() {
		LN = new String[50];
		for (int k = 1; k < LN.length; k++) {
			LN[k] = "Line " + k;
		}
	}

	@Before
	public void resetList() {
		list = new MyLinkedList<String>();
	}

	@Test
	public void isCorrectSize() {
		int s = 0;
		assertTrue("Initial size should be " + s, s == list.getSize());
		for (int k = 1; k <= 10; k++) {
			list.addLast(LN[k]);
			s++;
			assertTrue("Size should be " + s, s == list.getSize());
		}
		for (int m = 9; m > 0; m--) {
			list.deleteFirst();
			s--;
			assertTrue("Size should be " + s, s == list.getSize());
		}
	}

	@Test
	public void checkCurrentData() {
		assertNull("Current should be null at start", list.getCurrentData());
		assertTrue("Current position should be 0", 0 == list.getCurrentPos());
		assertNull("Node at any index before anything added should be null",
				list.getDataAt(1));
		assertNull("Node at any index before anything added should be null",
				list.getDataAt(8));

		assertTrue("Size should be 0", 0 == list.getSize());

		list.addFirst("Line 1");
		assertNotNull("Current should not be null anymore", list
				.getCurrentData());
		assertTrue("Current position should be 1", 1 == list.getCurrentPos());
		assertEquals("Current should be \"Line 1\"", "Line 1", list
				.getCurrentData());
		assertEquals("Data at id 1 should be \"Line 1\"", "Line 1", list
				.getDataAt(1));
		assertTrue("Size should be 1", 1 == list.getSize());

		list.addLast("Line 2");
		assertEquals("Current should be Line 2", "Line 2", list
				.getCurrentData());
	}

	@Test(
			expected = IllegalArgumentException.class)
	public void getDataAtSmallIndex() {
		list.addFirst("Line 1");
		String d = list.getDataAt(0);
	}

	@Test(
			expected = IllegalArgumentException.class)
	public void getDataAtLargeIndex() {
		list.addFirst("Line 1");
		String d = list.getDataAt(8);
	}

	@Test
	public void checkGetDataAt() {
		assertNull("Nothing should be at 1", list.getDataAt(1));
		assertNull("Nothing should be at 0", list.getDataAt(0));
		assertNull("Nothing should be at 8", list.getDataAt(1));
		list.addLast("Line 1");
		list.addLast("Line 2");
		list.addLast("Line 3");
		assertEquals("Should be 3 lines", 3, list.getSize());
		assertEquals("Data at 1 should be Line 1", "Line 1", list.getDataAt(1));
		assertEquals("Data at 2 should be Line 2", "Line 2", list.getDataAt(2));
		assertEquals("Data at 3 should be Line 3", "Line 3", list.getDataAt(3));
	}

	@Test
	public void checkAddFirst() {
		assertNull("Current should be null", list.getCurrentData());
		// list.addFirst("Line 20");
		// list.addFirst("Line 21");
		// assertEquals("First line should be " + LINES[21], LINES[21], list
		// .getDataAt(1));
		for (int k = 1; k <= 10; k++) {
			list.addFirst(LN[k]);
			assertEquals("Curent should be " + LN[1], LN[1], list
					.getCurrentData());
			assertTrue("Size should be " + k, k == list.getSize());
		}
		assertEquals("First line should be " + LN[10], LN[10], list
				.getDataAt(1));
		assertEquals("Last line should be " + LN[1], LN[1], list
				.getDataAt(list.getSize()));
	}

	@Test
	public void checkAddLast() {
		assertNull("Current should be null", list.getCurrentData());
		for (int k = 1; k <= 10; k++) {
			list.addLast(LN[k]);
			assertEquals("Curent should be " + LN[k], LN[k], list
					.getCurrentData());
			assertTrue("Size should be " + k, k == list.getSize());
		}
		assertEquals("First line should be " + LN[1], LN[1], list
				.getDataAt(1));
		assertEquals("Last line should be " + LN[10], LN[10], list
				.getDataAt(list.getSize()));
	}

	@Test
	public void checkAddAfterCurrent() {
		assertNull("Current should be null", list.getCurrentData());
		for (int k = 1; k <= 10; k++) {
			list.addAfterCurrent(LN[k]);
			assertEquals("Current should be " + LN[k], LN[k], list
					.getCurrentData());
			assertTrue("Size should be " + k, k == list.getSize());
		}
		assertEquals("First line should be " + LN[1], LN[1], list.getDataAt(1));
		assertEquals("Last line should be " + LN[10], LN[10], list
				.getDataAt(list.getSize()));
	}

	@Test
	public void checkAddBeforeCurrent() {
		assertNull("Current should be null", list.getCurrentData());
		for (int k = 1; k <= 10; k++) {
			list.addBeforeCurrent(LN[k]);
			assertEquals("Current should be " + LN[1], LN[1], list
					.getCurrentData());
			assertTrue("Size should be " + k, k == list.getSize());
		}
		assertEquals("First line should be " + LN[2], LN[2], list
				.getDataAt(1));
		assertEquals("Last line should be " + LN[1], LN[1], list
				.getDataAt(list.getSize()));
		assertEquals("Current should be " + LN[1], LN[1], list.getCurrentData());
	}

	@Test
	public void addListBeforeCurrent() {
		assertNull("Current should be null", list.getCurrentData());
		assertTrue("Size should be 0", 0 == list.getSize());
		MyLinkedList<String> list2 = new MyLinkedList<String>();
		MyLinkedList<String> list3 = new MyLinkedList<String>();
		for (int k = 1; k <= 5; k++) {
			list2.addLast(LN[k]);
			list3.addLast(LN[k + 10]);
		}
		assertTrue("List2 size should be 5", 5 == list2.getSize());

		list.addListBeforeCurrent(list2);
		assertTrue("List size should be List2 size",
				list2.getSize() == list.getSize());
		assertEquals("Current should be List2's tail (Line 5)",
				LN[5], list.getCurrentData());

		list.addListBeforeCurrent(list3);
		assertTrue("List size should be List2 size + List3 size", list2
				.getSize() + list3.getSize() == list.getSize());
		assertEquals("Current should still be Line 5", LN[5], list
				.getCurrentData());
		assertEquals("First line should be Line 1", LN[1], list.getDataAt(1));
		assertEquals("Second to last line should be Line 15", LN[15], list
				.getDataAt(list.getSize() - 1));
	}

	@Test
	public void checkDeleteFirst() {
		assertNull("Current should be null", list.getCurrentData());
		list.addFirst(LN[1]);
		assertEquals("Current should be " + LN[1], LN[1], list.getCurrentData());
		list.addLast(LN[2]);
		assertEquals("Current should be " + LN[2], LN[2], list.getCurrentData());
		list.deleteFirst();
		assertTrue("Size should now be 1", 1 == list.getSize());
		assertEquals("Current should be " + LN[2], LN[2], list.getCurrentData());
		assertEquals("Data at pos 1 should be " + LN[2], LN[2], list
				.getDataAt(1));
	}

	@Test
	public void checkDeleteLast() {
		assertNull("Current should be null", list.getCurrentData());
		for (int k = 1; k <= 5; k++) {
			list.addLast(LN[k]);
		}
		for (int j = 5; j >= 1; j--) {
			assertTrue("Size should be " + j, j == list.getSize());
			assertEquals("Current should be " + LN[j], LN[j], list
					.getCurrentData());
			list.deleteLast();
		}
		assertTrue("Size should be 0", 0 == list.getSize());
		assertNull("Current should be null", list.getCurrentData());
	}

	@Test
	public void checkContains() {
		assertFalse("List shouldn't contain Line 1 yet", list
				.contains("Line 1"));
		for (int k = 1; k <= 10; k++) {
			list.addLast(LN[k]);
			assertTrue("List should contain " + LN[k], list.contains(LN[k]));
		}
	}

	@Test
	public void checkMoveCurrentPos() {
		assertNull("Current should be null", list.getCurrentData());
		assertTrue("Current position should be 0", 0 == list.getCurrentPos());
		list.moveCurrentPos(2);
		assertTrue("Current pos should still be 0", 0 == list.getCurrentPos());
		for (int k = 1; k <= 8; k++) {
			list.addLast(LN[k]);
		}
		list.moveCurrentPos(4);
		assertTrue("Current pos should still be 8", 8 == list.getCurrentPos());
		int[] moves = { 4, -1, -5, -10, 1, 3, 12 };
		int curr = 8;
		for (int m : moves) {
			list.moveCurrentPos(m);
			curr += m;
			if (curr > list.getSize())
				curr = list.getSize();
			if (curr <= 0)
				curr = 1;
			assertTrue("Current pos should be " + curr, curr == list
					.getCurrentPos());
		}
	}

	@Test(
			expected = IllegalArgumentException.class)
	public void deleteNodesNegative() {
		list.addLast(LN[1]);
		list.deleteNodes(-1);
	}

	@Test
	public void deleteNodes() {
		assertTrue("Starting size should be 0", 0 == list.getSize());
		assertTrue("Current pos should be 0", 0 == list.getCurrentPos());
		for (int k = 1; k <= 20; k++) {
			list.addLast(LN[k]);
		}
		assertEquals("Current should be " + LN[20], LN[20], list
				.getCurrentData());
		assertTrue("Size should be 20", 20 == list.getSize());
		list.deleteNodes(1);
		assertEquals("Current should be " + LN[19], LN[19], list
				.getCurrentData());
		assertTrue("Size should be 19", 19 == list.getSize());

		list.deleteNodes(5);
		assertEquals("Current should be " + LN[18], LN[18], list
				.getCurrentData());
		assertTrue("Size should be 18", 18 == list.getSize());

		list.moveCurrentPos(-10);
		assertEquals("Current should be " + LN[8], LN[8], list.getCurrentData());
		list.deleteNodes(8);
		assertTrue("Size should be 10", 10 == list.getSize());
		assertEquals("Current should be " + LN[16], LN[16], list
				.getCurrentData());

		list.moveCurrentPos(-50);
		assertTrue("Current pos should be 1", 1 == list.getCurrentPos());
		list.deleteNodes(50);
		assertTrue("Size should now be 0", 0 == list.getSize());
		assertNull("Current should be null", list.getCurrentData());
		assertTrue("Current pos should be 0", 0 == list.getCurrentPos());
	}

	@Test
	public void extractMiddle() {
		assertTrue("Initial size should be 0", 0 == list.getSize());
		for (int k = 1; k <= 6; k++) {
			list.addLast(LN[k]);
		}
		assertTrue("Size should be 6", 6 == list.getSize());
		MyLinkedList<String> extd1 = list.extract(2, 4);
		assertTrue("List size should be 3", 3 == list.getSize());
		assertTrue("Extracted list 1 size should be 3", 3 == extd1
				.getSize());
		assertEquals("Current should be " + LN[5], LN[5], list.getCurrentData());
		assertEquals("Extracted line 1 should be " + LN[2], LN[2], extd1
				.getDataAt(1));
		assertEquals("Extracted line 2 should be " + LN[3], LN[3], extd1
				.getDataAt(2));
		assertEquals("Extracted line 3 should be " + LN[4], LN[4], extd1
				.getDataAt(3));
	}

	@Test
	public void extractStart() {
		assertTrue("Initial size should be 0", 0 == list.getSize());
		for (int k = 1; k <= 7; k++) {
			list.addLast(LN[k]);
		}
		assertTrue("Size should be 7", 7 == list.getSize());
		MyLinkedList<String> extd1 = list.extract(-4, 3);
		assertTrue("List size should be 4", 4 == list.getSize());
		assertTrue("Extracted list 1 size should be 3", 3 == extd1.getSize());
		assertEquals("Current should be " + LN[4], LN[4],
				list.getCurrentData());
		assertEquals("Extracted line 1 should be " + LN[1], LN[1], extd1
				.getDataAt(1));
		assertEquals("Extracted line 2 should be " + LN[2], LN[2], extd1
				.getDataAt(2));
		assertEquals("Extracted line 3 should be " + LN[3], LN[3], extd1
				.getDataAt(3));
	}

	@Test
	public void extractEnd() {
		assertTrue("Initial size should be 0", 0 == list.getSize());
		for (int k = 1; k <= 7; k++) {
			list.addLast(LN[k]);
		}
		assertTrue("Size should be 7", 7 == list.getSize());
		MyLinkedList<String> extd = list.extract(4, 10);
		assertTrue("List size should be 3", 3 == list.getSize());
		assertTrue("Extracted list size should be 4", 4 == extd.getSize());
		assertEquals("Current line should be " + LN[3], LN[3], list
				.getCurrentData());
		assertEquals("Extracted line 1 should be " + LN[4], LN[4], extd
				.getDataAt(1));
		assertEquals("Extracted line 2 should be " + LN[5], LN[5], extd
				.getDataAt(2));
		assertEquals("Extracted line 3 should be " + LN[6], LN[6], extd
				.getDataAt(3));
		assertEquals("Extracted line 4 should be " + LN[7], LN[7], extd
				.getDataAt(4));
	}

}
