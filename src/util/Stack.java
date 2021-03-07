package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This Stack class supports the usual push and pop operations, along with methods for peeking at the top item,
 * testing if the stack si empty, and iterating through the stack in LIFO order. (last in first out)
 *
 * Implement with a singly linked list and a static nested class
 */
public class Stack<Item> implements Iterable<Item> {

    private Node<Item> first;           // top of the stack
    private int n;                      // number of items in the stack

    private static class Node<Item> {
        Item item;
        Node<Item> next;
    }

    /**
     * Initialize an empty stack
     */
    public Stack() {
        first = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void push(Item item) {
        Node<Item> oldFirst = first;
        first = new Node<>();
        first.item = item;
        first.next = oldFirst;
        n++;
    }

    /**
     * Removes and returns the mostly added item
     * @return the item mostly added
     */
    public Item pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow.");
        }
        Item item = first.item;
        first = first.next;
        n--;
        return item;
    }

    /**
     * Return but doesn't remove the mostly added item
     * @return the item mostly added
     */
    public Item peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow.");
        }
        return first.item;
    }

    /**
     * 这里可以用this是因为这个类继承了Iterable，所以可以用forEach循环
     * @return the sequence of items in this stack in LIFO order, separated by spaces
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Item item : this) {
            sb.append(item);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

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
                throw new NoSuchElementException("Stack underflow.");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
