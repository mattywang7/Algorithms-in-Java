package util;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 许多应用程序都需要处理有序的元素，但不一定要求它们全部有序，或是不一定要一次就将它们排序
 * 在这种情况下，一个合适的数据结构应该支持两种操作：删除最大元素和插入元素
 *
 * 通过插入一列元素然后一个个地删掉其中最小的元素，我们可以用优先队列实现排序算法，这种算法叫堆排序
 *
 * 将实现了Comparable接口的数据的类型作为key
 *
 * 数据结构二叉堆能够很好地实现优先队列的基本操作
 * 在二叉堆的数组中，每个元素都要保证大于等于另两个特定位置的元素
 *
 * 定义：当一棵二叉树的每个结点都大于等于它的两个子结点时，它被称为堆有序。根结点是堆有序的二叉树中的最大结点
 *
 * 完全二叉树只用数组而不用指针就可以表示（不使用数组的第一个位置）
 * 由a[k]向上一层就是a[k/2], 由a[k]向下一层就是a[2 * k]和a[2 * k + 1]
 * 利用在数组中无需指针即可沿树上下移动的便利和以下性质，算法保证了对数复杂度的性能
 *
 * 堆的操作会首先进行一些简单的改动，打破堆的状态，然后再遍历堆并按照要求将堆的状态恢复，这个过程叫做堆的有序化
 * 有序化的过程我们会遇到两种情况：
 * - 某个结点的优先级上升（或是在堆底加入一个新的元素）时，我们需要由下至上恢复堆的状态 (swim)
 * swim() 方法中的循环可以保证只有位置k上的结点大于它的父结点时堆的有序状态才会被打破
 * - 某个结点的优先级下降（例如，将根结点替换成一个较小的元素）时，我们需要由上至下恢复堆的状态 (sink)
 * 父结点跟子结点中的较大者进行交换, 直到子结点都比小小或是到达了堆的底部
 */

public class MaxPQ<Key> implements Iterable<Key> {

    private Key[] pq;                       // store items from indices 1 to n
    private int n;                          // number of elements in the pq
    private Comparator<Key> comparator;     // optional comparator

    // + 1 是为了给pq[0]一个位置
    public MaxPQ(int capacity) {
        pq = (Key[]) new Object[capacity + 1];
        n = 0;
    }

    public MaxPQ() {
        this(1);
    }

    public MaxPQ(int capacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[capacity + 1];
        n = 0;
    }

    public MaxPQ(Comparator<Key> comparator) {
        this(1, comparator);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public Key max() {
        if (isEmpty()) {
            throw new IllegalArgumentException("Priority queue underflow.");
        }
        return pq[1];
    }

    private void resize(int capacity) {
        assert capacity > n;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    public void insert(Key key) {
        if (n == pq.length - 1) {
            resize(2 * pq.length);
        }
        pq[++n] = key;
        swim(n);
        assert isMaxHeap();
    }

    public Key delMax() {
        if (isEmpty()) {
            throw new IllegalArgumentException("Priority queue underflow.");
        }
        Key result = pq[1];
        exchange(1, n);
        pq[n--] = null;
        sink(1);
        if (n > 0 && n == (pq.length - 1) / 4) {
            resize(pq.length / 2);
        }
        assert isMaxHeap();
        return result;
    }

    private void swim(int k) {
        while (k > 1 && less(k / 2, k)) {
            exchange(k / 2, k);
            k /= 2;
        }
    }

    private void sink(int k) {
       while (2 * k <= n) {
           int j = 2 * k;
           // 当j == n， 说明这个堆的最后一个元素是一个左子结点，也就是说只有一个结点
           if (j < n && less(j, j + 1)) {
               j++;
           }
           if (!less(k, j)) {
               break;
           }
           exchange(k, j);
           k = j;
       }
    }

    private boolean less(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) < 0;
        }
        else {
            return comparator.compare(pq[i], pq[j]) < 0;
        }
    }

    private void exchange(int i, int j) {
        Key temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    private boolean isMaxHeap() {
        for (int i = 1; i <= n; i++) {
            if (pq[i] == null) {
                return false;
            }
        }
        for (int i = n + 1; i < pq.length; i++) {
            if (pq[i] != null) {
                return false;
            }
        }
        if (pq[0] != null) {
            return false;
        }
        return isMaxHeapOrdered(1);
    }

    private boolean isMaxHeapOrdered(int k) {
        if (k > n) return true;
        int left = 2 * k;
        int right = 2 * k + 1;
        if (left <= n && less(k, left)) {
            return false;
        }
        if (right <= n && less(k, right)) {
            return false;
        }
        return isMaxHeapOrdered(left) && isMaxHeapOrdered(right);
    }

    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {
        private MaxPQ<Key> copy;

        public HeapIterator() {
            if (comparator == null) {
                copy = new MaxPQ<>(size());
            }
            else {
                copy = new MaxPQ<>(size(), comparator);
            }
            if (!isEmpty()) {
                for (int i = 1; i <= n; i++) {
                    copy.insert(pq[i]);
                }
            }
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Key next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return copy.delMax();
        }
    }

    public static void main(String[] args) {
        MaxPQ<String> pq = new MaxPQ<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }
        StdOut.println("(" + pq.size() + " left on pq)");
    }
}
