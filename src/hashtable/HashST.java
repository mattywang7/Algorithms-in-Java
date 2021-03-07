package hashtable;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SequentialSearchST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 使用散列的查找算法分为两步
 * - 用散列函数将被查找的键转化为数组的一个索引，理想情况下，不同的键都能转化为不同的索引值
 * - 处理碰撞冲突的过程：拉链法和线性探测法
 *
 * 散列表是算法在时间和空间上做出权衡的经典例子
 * 如果没有内存限制，我们可以直接将键作为（可能是一个超大的）数组的索引，那么所有查找操作只需访问内存一次即可完成。
 * 但这种理想情况不会经常出现，因为当键很多时需要的内存太大。
 *
 * 使用散列表，你可以实现在一般应用中拥有（均摊后）常数级别的查找和插入操作的符号表。
 * 很多情况下实现简单符号表的最佳选择
 *
 * 散列函数与键的类型有关。对于每种类型的键，我们都需要一个与之对应的散列函数
 *
 * 将整数散列最常用的方法是除留余数法
 * 选择大小为素数M的数组，对于任意正整数k，计算k % M的值。
 * 这个函数的计算非常容易，并且能够将键散布在0到M - 1之间
 *
 * 每种数据类型都需要相应的散列函数，于是Java令所有数据类型都继承了一个能够返回一个32比特整数的hashCode()方法。
 * 每一个数据类型的hashCode()都必须和equals()一样
 * 如果两个对象的hashCode()返回值相同，不足以说明这两个对象就相同，还需要equals()进行判断，
 * 如果你要为自定义的类型定义散列函数，就需要重写equals()和hashCode()函数
 * 默认的散列函数会返回对象的地址
 *
 * 软缓存：
 * 如果散列值的计算很耗时，那么我们或许可以将每个键的散列值缓存起来，即在每个键中使用一个hash变量来保存它的hashCode()的返回值。
 * 第一次调用hashCode()方法时，我们需要计算对象的散列值，但之后对hashCode()方法的调用会直接返回hash变量的值。
 * Java的String对象的hashCode()就使用了这个方法来减少计算量
 *
 * 处理碰撞：
 * 大小为M的数组中的每个元素指向一条链表；
 * 选择足够大的M，是每条链表都尽可能短
 */

public class HashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;

    private int n;                                  // number of key-value pairs
    private int m;                                  // hash table size
    private SequentialSearchST<Key, Value>[] st;    // array of linked-list symbol tables

    public HashST() {
        this(INIT_CAPACITY);
    }

    public HashST(int m) {
        this.m = m;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[m];
        for (int i = 0; i < m; i++) {
            st[i] = new SequentialSearchST<>();
        }
    }

    // Hash function for keys - returns values between 0 - (m-1)
    private int hashTextbook(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    // hash function for keys - returns value between 0 and m-1 (assumes m is a power of 2)
    // (from Java 7 implementation, protects against poor quality hashCode() implementations)
    private int hash(Key key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12) ^ (h >>> 7) ^ (h >>> 4);
        return h & (m-1);
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        int i = hash(key);
        return st[i].get(key);
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    // resize the hash table to have the given number of chains
    // rehash all of the keys
    private void resize(int chains) {
        HashST<Key, Value> temp = new HashST<>(chains);
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.m = temp.m;
        this.n = temp.n;
        this.st = temp.st;
    }

    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("argument to put() is null");
        if (value == null) {
            delete(key);
            return;
        }

        // Double the table size if the average length of list >= 10
        if (n >= 10 * m) {
            resize(2 * m);
        }

        int i = hash(key);
        if (!st[i].contains(key)) {
            n++;
        }
        st[i].put(key, value);
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");

        int i = hash(key);
        if (st[i].contains(key)) {
            n--;
        }
        st[i].delete(key);

        // halve the table size if the average length of list <= 2
        if (m > INIT_CAPACITY && n <= 2 * m) {
            resize(m / 2);
        }
    }

    // return keys in the symbol table as an Iterable
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<>();
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys()) {
                queue.enqueue(key);
            }
        }
        return queue;
    }

    public static void main(String[] args) {
        HashST<String, Integer> hst = new HashST<>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            hst.put(key, i);
        }

        for (String key : hst.keys()) {
            StdOut.println(key + " " + hst.get(key));
        }
    }
}
