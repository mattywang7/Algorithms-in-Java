package util;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The Bag class represents a bag (or multiset) of generic items. It supports insertion and iterating over
 * the items in arbitrary order.
 *
 * The implementation uses a linked list with a static nested class Node.
 *
 * 这里解释以下静态内部类和非静态内部类的区别：
 * - static nested class:
 *      只是为了降低包的深度，方便类的使用。静态内部类适用于包含于主类当中，但又不依赖于主类，不使用外在类的非静态属性和方法，只是为了方便管理类结构而定义。在创建静态内部类的时候，不需要外部对象的引用。
 *
 * - inner class:
 *      可以自由使用外部类的所有变量和方法
 */
public class Bag<Item> implements Iterable<Item> {

    private Node<Item> first;       // beginning of bag
    private int n;                  // number of items in a bag

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    public Bag() {
        first = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void add(Item item) {
        Node<Item> oldFirst = first;
        first = new Node<>();
        first.item = item;
        first.next = oldFirst;
        n++;
    }

    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    // an iterator, doesn't implement remove() since it's optional. "default" key word means that this method is optional to override
    // 一个类去继承一个泛型接口时是不是不可以再添加泛型了?
    // 答案：这里的Item其实不是指泛型，而是已经指定了泛型类型就是Item这个具体的类型（主类中定义的那个Item)，此时子类如果再添加泛型，那么就是子类自己增加的泛型类型了
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
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
