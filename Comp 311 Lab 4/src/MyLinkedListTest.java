import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jacob davis
 * @version 7/28/19
 *
 * tests my linked list class
 */
public class MyLinkedListTest {
    private MyLinkedList<Integer> list;
    private ListIterator<Integer> itr;

    /**
     * sets up list
     * @throws Exception an exception
     */
    @Before
    public void setUp() throws Exception {
        list = new MyLinkedList<Integer>();
        itr = list.listIterator(0);
    }

    /**
     * tests an empty list
     */
    @Test
    public void testEmptyList() {
        assertEquals(0, list.size());

        // test index less than 0
        boolean caught = false;
        try {
            ListIterator<Integer> badItr = list.listIterator(-1);
            badItr.next();
        }
        catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

        // test index greater than size
        caught = false;
        try {
            ListIterator<Integer> badItr = list.listIterator(1);
            badItr.next();
        }
        catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

        // test next on empty
        caught = false;
        try {
            itr.next();
        }
        catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

     // test previous on empty
        caught = false;
        try {
            itr.previous();
        }
        catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

        // test next index == size
        assertEquals(0, itr.nextIndex());

        // test previous index = -1;
        assertEquals(-1, itr.previousIndex());
    }

    /**
     * tests list
     */
    @Test
    public void testList() {
        itr.add(1);
        itr.add(2);
        itr.add(3);
        itr.add(4);
        itr.add(5);

        ListIterator<Integer> myItr = list.listIterator(0);

        assertEquals(Integer.valueOf(1), myItr.next());
        assertEquals(Integer.valueOf(2), myItr.next());
        assertEquals(Integer.valueOf(3), myItr.previous());
        assertEquals(Integer.valueOf(2), myItr.next());
        assertEquals(Integer.valueOf(3), myItr.next());
        assertEquals(Integer.valueOf(4), myItr.next());
        assertEquals(Integer.valueOf(5), myItr.next());

        assertEquals(5, myItr.nextIndex());
        assertEquals(4, myItr.previousIndex());

        myItr.previous();

        assertEquals(4, myItr.nextIndex());
        assertEquals(3, myItr.previousIndex());
    }

    /**
     * tests remove
     */
    @Test
    public void testRemove() {
        // test remove when next or previous not called
        boolean caught = false;
        try {
            itr.remove();
        }
        catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

        itr.add(1);
        itr.add(2);
        itr.add(3);
        itr.add(4);
        itr.add(5);

        ListIterator<Integer> rItr = list.listIterator(0);

        assertEquals(Integer.valueOf(1), rItr.next());

        rItr.remove();

        assertEquals(Integer.valueOf(2), rItr.next());
        assertEquals(Integer.valueOf(3), rItr.previous());
        assertEquals(Integer.valueOf(2), rItr.next());

        rItr.next();
        rItr.next();
        rItr.next();

        rItr.remove();

        assertEquals(Integer.valueOf(4), rItr.previous());

        rItr.remove();

        assertEquals(Integer.valueOf(3), rItr.next());

        rItr.remove();
        assertEquals(Integer.valueOf(2), rItr.next());
    }

    /**
     * tests set
     */
    @Test
    public void testSet() {
        itr.add(1);
        itr.add(2);
        itr.add(3);
        itr.add(4);
        itr.add(5);

        ListIterator<Integer> rItr = list.listIterator(0);

        rItr.next();
        rItr.next();
        rItr.next();

        rItr.set(33);

        rItr.previous();

        assertEquals(Integer.valueOf(33), rItr.previous());

        ListIterator<Integer> itr2 = list.listIterator(4);

        assertEquals(Integer.valueOf(5), itr2.previous());
        assertEquals(Integer.valueOf(4), itr2.previous());
        assertEquals(Integer.valueOf(33), itr2.previous());
        assertEquals(Integer.valueOf(2), itr2.previous());
        assertEquals(Integer.valueOf(1), itr2.next());
    }

    /**
     * test get
     */
    @Test
    public void testGet() {
        itr.add(1);
        itr.add(2);
        itr.add(3);
        itr.add(4);
        itr.add(5);

        assertEquals(Integer.valueOf(4), list.get(3));
    }
}
