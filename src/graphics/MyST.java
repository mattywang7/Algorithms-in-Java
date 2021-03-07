package graphics;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class MyST<Key extends Comparable<Key>, Value> implements Iterable<Key> {

    private TreeMap<Key, Value> st;

    public MyST() {
        st = new TreeMap<>();
    }

    /**
     * @param key - the key
     * @return the value associated with the given key in the symbol table; null if the key is not in
     *          the symbol table
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("calls get() with null key.");
        return st.get(key);
    }

    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("calls put() with null key.");
        if (value == null) {
            st.remove(key);
        }
        else {
            st.put(key, value);
        }
    }

    /**
     * Remove the key and the associated value from this symbol table (if the key is in this symbol table)
     * @param key - the key
     */
    public void remove(Key key) {
        if (key == null) throw new IllegalArgumentException("calls remove() with null key.");
        st.remove(key);
    }

    public boolean containsKey(Key key) {
        if (key == null) throw new IllegalArgumentException("calls containsKey() with null key.");
        return st.containsKey(key);
    }

    public int size() {
        return st.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @return all keys in the symbol table
     */
    public Iterable<Key> keys() {
        return st.keySet();
    }

    @Deprecated
    public Iterator<Key> iterator() {
        return st.keySet().iterator();
    }

    /**
     * @return the smallest key in the symbol table
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table.");
        return st.firstKey();
    }

    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table.");
        return st.lastKey();
    }

    /**
     * Return the smallest key in this symbol table greater than or equal to the key
     * @param key- the key
     * @return the smallest key in this symbol table greater than or equal to the key
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("calls ceiling() with null key.");
        Key k = st.ceilingKey(key);
        if (k == null) throw new NoSuchElementException("argument to ceiling() is too large");
        return k;
    }

    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("calls floor() with null key.");
        Key k = st.floorKey(key);
        if (k == null) throw new NoSuchElementException("argument to floor() is too small.");
        return k;
    }

    public static void main(String[] args) {
        MyST<String, Integer> myST = new MyST<>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            myST.put(key, i);
        }

        for (String s : myST.keys()) {
            StdOut.println(s + " " + myST.get(s));
        }
    }
}
