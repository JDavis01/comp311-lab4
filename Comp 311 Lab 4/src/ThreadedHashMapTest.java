import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

/**
 * @author jacob davis
 * @version 7/28/19
 *
 *tests threaded hash map
 */
public class ThreadedHashMapTest {

    /**
     * tests put
     */
    @Test
    public void testPut() {
        ThreadedHashMap<String, Integer> tmap =
                new ThreadedHashMap<String, Integer>();

        // Test is empty
        assertTrue(tmap.isEmpty());

        tmap.put("one", 1);
        tmap.put("two", 2);
        tmap.put("three", 3);
        tmap.put("four", 4);

        assertFalse(tmap.isEmpty());

        assertEquals(Integer.valueOf(3), tmap.get("three"));
        assertEquals(Integer.valueOf(1), tmap.get("one"));
        assertEquals(Integer.valueOf(2), tmap.get("two"));
        assertEquals(Integer.valueOf(4), tmap.get("four"));

        // Test set is changed
        tmap.remove("three");
        Set<Entry<String, Integer>> set =
                new HashSet<Entry<String, Integer>>();
        set = tmap.entrySet();
        assertEquals(3, set.size());

        // Test clear
        tmap.clear();
        assertTrue(tmap.isEmpty());
    }

    /**
     * tests rehash
     */
    @Test
    public void testRehash() {
        ThreadedHashMap<Integer, Integer> tmap =
                new ThreadedHashMap<Integer, Integer>();

        for (int i = 0; i < 20; i++) {
            tmap.put(i, i + 10);
        }

        // Test size
        assertEquals(20, tmap.size());

        assertEquals(Integer.valueOf(21), tmap.get(11));

        // Test capacity of table changed at threshold
        assertEquals(34, tmap.capacity());
    }

    /**
     * tests remove
     */
    @Test
    public void testRemove() {
        ThreadedHashMap<Integer, Integer> tmap =
                new ThreadedHashMap<Integer, Integer>();

        for (int i = 0; i < 20; i++) {
            tmap.put(i, i + 10);
        }

        assertEquals(Integer.valueOf(21), tmap.remove(11));
    }

    /**
     * tests contains
     */
    @Test
    public void testContains() {
        ThreadedHashMap<Integer, Integer> tmap =
                new ThreadedHashMap<Integer, Integer>();

        for (int i = 0; i < 20; i++) {
            tmap.put(i, i + 10);
        }

        assertTrue(tmap.containsKey(13));
        assertTrue(tmap.containsValue(29));
        assertFalse(tmap.containsKey(20));
        assertFalse(tmap.containsValue(30));

    }

    /**
     * tests put all
     */
    @Test
    public void testPutAll() {
        HashMap<Integer, Integer> hmap =
                new HashMap<Integer, Integer>();

        ThreadedHashMap<Integer, Integer> tmap =
                new ThreadedHashMap<Integer, Integer>();

        for (int i = 0; i < 20; i++) {
            hmap.put(i, i + 10);
        }

        tmap.putAll(hmap);

        assertTrue(tmap.containsKey(13));
        assertTrue(tmap.containsValue(29));
        assertFalse(tmap.containsKey(20));
        assertFalse(tmap.containsValue(30));
    }

    /**
     * tests list methods
     */
    @Test
    public void testList() {
        // Test entry list
        ThreadedHashMap<Integer, Integer> tmap =
                new ThreadedHashMap<Integer, Integer>();

        List<Entry<Integer, Integer>> list =
                new LinkedList<Entry<Integer, Integer>>();

        for (int i = 0; i < 20; i++) {
            tmap.put(i, i + 10);
        }

        list = tmap.entryList();

        assertEquals(20, list.size());

        assertEquals(Integer.valueOf(14), list.get(4).getValue());

        // Test key list
        List<Integer> keyList = new LinkedList<Integer>();
        keyList = tmap.keyList();

        assertEquals(Integer.valueOf(15), keyList.get(15));

        // Test value list
        List<Integer> valList = new LinkedList<Integer>();
        valList = tmap.valueList();

        assertEquals(Integer.valueOf(25), valList.get(15));
    }

    /**
     * tests set methods
     */
    @Test
    public void testSet() {
     // Test entry set
        ThreadedHashMap<Integer, Integer> tmap =
                new ThreadedHashMap<Integer, Integer>();

        Set<Entry<Integer, Integer>> set =
                new HashSet<Entry<Integer, Integer>>();

        for (int i = 0; i < 20; i++) {
            tmap.put(i, i + 10);
        }

        set = tmap.entrySet();

        assertEquals(20, set.size());
        Iterator<Entry<Integer, Integer>> sItr =
                set.iterator();
        Map.Entry<Integer, Integer> entry =
                sItr.next();
        assertTrue(tmap.containsKey(entry.getKey()));
        entry = sItr.next();
        assertTrue(tmap.containsValue(entry.getValue()));

        // Test key set
        Set<Integer> kSet =
                new HashSet<Integer>();

        kSet = tmap.keySet();
        Iterator<Integer> kItr = kSet.iterator();
        Integer k = kItr.next();
        assertTrue(tmap.containsKey(k));

        //Test value set
        Set<Integer> vSet =
                new HashSet<Integer>();

        vSet = tmap.values();
        Iterator<Integer> vItr = vSet.iterator();
        Integer v = vItr.next();
        assertTrue(tmap.containsValue(v));
    }
}
