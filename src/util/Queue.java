package util;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The Queue class represents a first-in-first-out (FIFO) queue of generic items.
 * It supports the usual enqueue and dequeue operations, along with methods for peeking at the first item,
 * testing if the queue is empty, and iterating through the items in FIFO order.
 *
 * enqueue to last; dequeue from first
 */
public class Queue<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int n;              // number of elements on queue

    private static class Node<Item> {
        Item item;
        Node<Item> next;
    }

    public Queue() {
        first = null;
        last = null;
        n = 0;
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @return the least recently added item to this queue
     */
    public Item peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow.");
        }
        return first.item;
    }

    /**
     * Add an item to this queue
     * @param item the added item
     */
    public void enqueue(Item item) {
        Node<Item> oldLast = last;
        last = new Node<>();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            first = last;
        }
        else {
            oldLast.next = last;
        }
        n++;
    }

    /**
     * Removes and returns the least recently added item to this queue
     * @return the least recently added item to this queue
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow.");
        }
        Item item = first.item;
        first = first.next;
        n--;
        // 这里要加一个删除时仅有一个元素的判断，因为 n-- 后 last 还引用着最后一个元素
        if (isEmpty()) {
            last = null;
        }
        return item;
    }

    /**
     * Iterate this queue in FIFO order
     * @return a LinkedIterator
     */
    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Queue underflow.");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Queue<String> queue = new Queue<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                queue.enqueue(item);
            }
            else if (!queue.isEmpty()) {
                System.out.print(queue.dequeue() + " ");
            }
        }
        System.out.println();
        System.out.println("(" + queue.size() + " items left on queue.)");
    }
}
