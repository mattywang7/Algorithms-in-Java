package hashtable;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 用大小为M的数组保存N个键值对， 其中M > N
 * 我们需要依靠数组中的空位解决碰撞问题。基于这种策略的所有方法统称为开放地址散列表
 *
 * 线性探测法：当冲突产生时，我们直接检查数组的下一位索引 (index++)
 * 我们使用并行数组，一条保存键，一条保存值，并使用散列函数产生访问数据所需的数组索引
 */

public class LPHashST<Key, Value> {

    private static final int INIT_CAPACITY = 4;

    private int n;
    private int m;
    private Key[] keys;
    private Value[] values;

    public LPHashST() {
        this(INIT_CAPACITY);
    }

    public LPHashST(int capacity) {
        m = capacity;
        n = 0;
        keys = (Key[]) new Object[m];
        values = (Value[]) new Object[m];
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private int hashText(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    private int hash(Key key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12) ^ (h >>> 7) ^ (h >>> 4);
        return h & (m-1);
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        for (int i = hash(key); keys[i] != null; i = (i + 1) % m) {
            if (keys[i].equals(key)) {
                return values[i];
            }
        }
        return null;
    }

    private void resize(int capacity) {
        LPHashST<Key, Value> temp = new LPHashST<>(capacity);
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], values[i]);
            }
        }
        keys = temp.keys;
        values = temp.values;
        m = temp.m;
    }

    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("argument to put() is null");

        if (value == null) {
            delete(key);
            return;
        }

        if (n >= m / 2) {
            resize(2 * m);
        }

        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % m) {
            if (keys[i].equals(key)) {
                values[i] = value;
            }
        }
        keys[i]     = key;
        values[i]   = value;
        n++;
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;

        int i = hash(key);
        while (!keys[i].equals(key)) {
            i = (i + 1) % m;
        }

        keys[i]      = null;
        values[i]    = null;

        // rehash all keys in the same cluster
        i = (i + 1) % m;
        while (keys[i] != null) {
            Key keyToRehash = keys[i];
            Value valueToRehash = values[i];
            keys[i] = null;
            values[i] = null;
            n--;
            put(keyToRehash, valueToRehash);
            i = (i + 1) % m;
        }

        n--;

        if (n > 0 && n <= m / 8) resize(m / 2);

        assert check();
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<>();
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) queue.enqueue(keys[i]);
        }
        return queue;
    }

    // Integrity check - don't check after put() because
    // integrity not maintained during a delete()
    private boolean check() {

        // check that the hash table is at most 50% full
        if (m < n * 2) {
            System.err.println("Hash table size m = " + m + "; array size n = " + n);
            return false;
        }

        // check that each key in the table can be found by get()
        for (int i = 0; i < m; i++) {
            if (keys[i] == null) continue;
            else if (get(keys[i]) != values[i]) {
                System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; values[i] = " + values[i]);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        LPHashST<String, Integer> hst = new LPHashST<>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            hst.put(key, i);
        }

        for (String s : hst.keys()) {
            StdOut.println(s + " " + hst.get(s));
        }
    }
}
