import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author jacob davis
 * @version 7/28/19
 *
 * @param <K> the key
 * @param <V> the value
 */
public class ThreadedHashMap<K, V> implements Map<K, V>
{
    /** The table */
    private ArrayList<LinkedList<MyEntry<K, V>>> table;
    /** The insertion order */
    private MyLinkedList<MyEntry<K, V>> inOrder;
    /** The inOrder iterator */
    private ListIterator<MyEntry<K, V>> itr;
    /** The set */
    private Set<Entry<K, V>> set;
    /** The number of keys */
    private int numKeys;
    /** The number of slots in table */
    private int numSlots;
    /** The capacity */
    private static final int CAPACITY = 17;
    /** The maximum load factor */
    private static final double LOAD_THRESHOLD = .75;

    /**
     * constructs the map
     */
    public ThreadedHashMap() {
        table = new ArrayList<LinkedList<MyEntry<K, V>>>(CAPACITY);
        inOrder = new MyLinkedList<MyEntry<K, V>>();
        itr = inOrder.listIterator(0);
        set = new HashSet<Entry<K, V>>();
        numKeys = 0;
        numSlots = CAPACITY;
        for (int i = 0; i < numSlots; i++)
            table.add(null);
    }

    /**
     * puts key and value into table
     * @param key the key
     * @param value the value
     * @return V the value of key
     */
    @Override
    public V put(K key, V value)
    {
        int index = find(key);
        if (table.get(index) == null) {
            // Create a new linked list at table[index].
            table.set(index, new LinkedList<MyEntry<K, V>>());
        }

        // Search the list at table[index] to find the key.
        for (MyEntry<K, V> nextItem : table.get(index)) {
            // If the search is successful, replace the old value.
            if (nextItem.getKey().equals(key)) {
                // Replace value for this key.
                V oldVal = nextItem.getValue();
                nextItem.setValue(value);
                return oldVal;
            }
        }
        // assert: key is not in the table, add new item.
        itr.add(new MyEntry<>(key, value));
        set.add(new MyEntry<>(key, value));
        table.get(index).add(new MyEntry<>(key, value));
        numKeys++;
        double loadFactor =
                (double) numKeys / numSlots;
        if (loadFactor > LOAD_THRESHOLD) {
            rehash();
            return null;
        }
        return null;
    }

    /**
     * rehashes table at threshhold
     */
    private void rehash() {
        // Save a reference to oldTable.
        ArrayList<LinkedList<MyEntry<K, V>>> oldTable = table;
        // Double capacity of this table.
        table = new ArrayList<LinkedList<MyEntry<K, V>>>(2 * numSlots);
        numSlots = numSlots * 2;
        for (int i = 0; i < numSlots; i++) {
            table.add(null);
        }

        // Reinsert all items in oldTable into expanded table.
        for (int i = 0; i < oldTable.size(); i++) {
            if ((oldTable.get(i) != null)) {
                // Insert entry in expanded table
                table.set(i, oldTable.get(i));
            }
        }
    }

    /**
     * returns number of entries
     * @return int size of table
     */
    @Override
    public int size()
    {
        return numKeys;
    }

    /**
     * amount of slots in table
     * @return int current capacity
     */
    public int capacity() {
        return table.size();
    }

    /**
     * checks if table is empty
     * @return boolean true if empty
     */
    @Override
    public boolean isEmpty()
    {
        for (LinkedList list : table) {
            if (list != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * starts this back to construction
     */
    @Override
    public void clear()
    {
        table = new ArrayList<LinkedList<MyEntry<K, V>>>(CAPACITY);
        inOrder = new MyLinkedList<MyEntry<K, V>>();
        itr = inOrder.listIterator(0);
        set = new HashSet<Entry<K, V>>();
        numKeys = 0;
        numSlots = CAPACITY;
        for (int i = 0; i < numSlots; i++)
            table.add(null);
    }

    /**
     * finds index of key
     * @param key location searched for
     * @return int the index
     */
    private int find(Object key) {
        int index = key.hashCode() % numSlots;
        if (index < 0) {
            index += numSlots;
        }
        return index;
    }

    /**
     * gets the value from the key
     * @param key the key searched for
     * @return V the value of key
     */
    @Override
    public V get(Object key)
    {
        int index = find(key);
        if (table.get(index) == null) {
            return null;  // key is not in the table.
        }
        // Search the list at table[index] to find the key.
        for (MyEntry<K, V> nextItem : table.get(index)) {
            if (nextItem.getKey().equals(key))
                return nextItem.getValue();
        }
        // assert: key is not in the table.
        return null;
    }

    /**
     * removes map entry from table and set
     * @param key search for
     * @return V value of entry removed
     */
    @Override
    public V remove(Object key)
    {
        V removed = null;
        int index = find(key);
        if (table.get(index) == null) {
            return null;  // key is not in the table.
        }
        boolean inTable = false;
        for (Entry<K, V> nextItem : table.get(index)) {
            if (nextItem.getKey().equals(key)) {
                removed = nextItem.getValue();
                table.get(index).remove(nextItem);
                inTable = true;
                numKeys--;
            }
        }
        if (inTable) {
            Iterator<Entry<K, V>> sItr = this.set.iterator();
            while (sItr.hasNext()) {
                Entry<K, V> entry = sItr.next();
                if (entry.getKey().equals(key)) {
                    sItr.remove();
                }
            }
        }
        return removed;
    }

    /**
     * checks if this contains key
     * @param key searched for
     * @return boolean true if this has key
     */
    @Override
    public boolean containsKey(Object key)
    {
        int index = find(key);
        if (table.get(index) == null) {
            return false;  // key is not in the table.
        }
        for (MyEntry<K, V> nextItem : table.get(index)) {
            return nextItem.getKey().equals(key);
        }
        return false;
    }

    /**
     * checks if this has value
     * @param value searched for
     * @return true if this has value
     */
    @Override
    public boolean containsValue(Object value)
    {
        ListIterator<MyEntry<K, V>> vItr = inOrder.listIterator(0);
        while (vItr.hasNext()) {
            if (vItr.next().getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * puts all of collection in this
     * @param map the collection to put in this
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> map)
    {
        Iterator sItr = map.entrySet().iterator();
        while (sItr.hasNext()) {
            Map.Entry<K, V> entry = (Entry<K, V>) sItr.next();
            this.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * returns entries as set
     * @return Set<Entry<K, V>> the set
     */
    @Override
    public Set<Entry<K, V>> entrySet()
    {
        return this.set;
    }

    /**
     * returns entries as list
     * @return List<Entry<K, V>> the list
     */
    public List<Entry<K, V>> entryList() {

        return (List) inOrder;
    }

    /**
     * returns keys as a set
     * @return Set<K> the set of keys
     */
    @Override
    public Set<K> keySet()
    {
        Iterator<Entry<K, V>> kItr = this.set.iterator();
        Set<K> kSet = new HashSet<K>();
        while (kItr.hasNext()) {
            kSet.add(kItr.next().getKey());
        }
        return kSet;
    }

    /**
     * returns keys as list
     * @return List<K> the list of keys
     */
    public List<K> keyList() {
        ListIterator<MyEntry<K, V>> oItr = inOrder.listIterator(0);
        List<K> keyList = new LinkedList<K>();
        while (oItr.hasNext()) {
            keyList.add(oItr.next().getKey());
        }
        return keyList;
    }

    /**
     * returns the values as set
     * @return Set<V> set of values
     */
    @Override
    public Set<V> values()
    {
        Iterator<Entry<K, V>> vItr = this.set.iterator();
        Set<V> vSet = new HashSet<V>();
        while (vItr.hasNext()) {
            vSet.add(vItr.next().getValue());
        }
        return vSet;
    }

    /**
     * returns values as a list
     * @return List<V> list of values
     */
    public List<V> valueList()
    {
        ListIterator<MyEntry<K, V>> oItr = inOrder.listIterator(0);
        List<V> valList = new LinkedList<V>();
        while (oItr.hasNext()) {
            valList.add(oItr.next().getValue());
        }
        return valList;
    }

    /**
     *
     * @author jacob davis
     * @version 7/28/19
     *
     * an entry class for a map made up of keys and values
     *
     * @param <K> keys
     * @param <V> values
     */
    private static class MyEntry<K, V> implements Map.Entry<K, V> {

        /** The key */
        private K key;
        /** The value */
        private V value;

        /** Creates a new key-value pair.
            @param key The key
            @param value The value
         */
        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /** Retrieves the key.
            @return The key
         */
        @Override
        public K getKey() {
            return key;
        }

        /** Retrieves the value.
            @return The value
         */
        @Override
        public V getValue() {
            return value;
        }

        /** Sets the value.
            @param val The new value
            @return The old value
         */
        @Override
        public V setValue(Object val) {
            V oldVal = value;
            value = (V) val;
            return oldVal;
        }

    }
}
