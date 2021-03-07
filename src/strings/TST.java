package strings;

import util.Queue;

/**
 * 避免R向单词查找树的过度空间消耗
 * 在三向单词查找树中，每个结点都含有一个字符、三条链接和一个值
 * 这三条链接分别对应着当前字母小于、等于和大于结点字母的所有键
 *
 * 查找和插入：
 * 首先比较键的首字母和根结点的字母。如果键的首字母较小，就选择左链接；键的首字母较大 -> 右链接；相等 -> 中间链接
 */

public class TST<Value> {

    private int n;
    private Node<Value> root;

    private static class Node<Value> {
        private char c;
        private Node<Value> left, mid, right;
        private Value val;
    }

    public TST() {}

    public int size() {
        return n;
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null.");
        }
        return get(key) != null;
    }

    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null.");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("key.length() must ≥ 1");
        }
        Node<Value> x = get(root, key, 0);
        if (x == null) {
            return null;
        }
        return x.val;
    }

    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("key.length() must ≥ 1");
        }
        char c = key.charAt(d);
        if (c < x.c) {
            return get(x.left, key, d);
        }
        else if (c > x.c) {
            return get(x.right, key, d);
        }
        else if (d < key.length() - 1) {
            return get(x.mid, key, d + 1);
        }
        else {
            return x;
        }
    }

    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (!contains(key)) {
            n++;
        }
        else if (val == null) {
            n--;
        }
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<>();
            x.c = c;
        }
        if (c < x.c) {
            x.left = put(x.left, key, val, d);
        }
        else if (c > x.c) {
            x.right = put(x.right, key, val, d);
        }
        else if (d < key.length() - 1) {
            x.mid = put(x.mid, key, val, d + 1);
        }
        else {
            x.val = val;
        }
        return x;
    }

    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        }
        if (query.length() == 0) {
            return null;
        }
        int length = 0;
        int i = 0;
        Node<Value> x = root;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if (c < x.c) {
                x = x.left;
            }
            else if (c > x.c) {
                x = x.right;
            }
            else {
                i++;
                if (x.val != null) {
                    length = i;
                }
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }

    public Iterable<String> keys() {
        Queue<String> queue = new Queue<>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    private void collect(Node<Value> x, StringBuilder sb, Queue<String> queue) {
        if (x == null) {
            return;
        }
        collect(x.left, sb, queue);
        if (x.val != null) {
            queue.enqueue(sb.toString() + x.c);
        }
        collect(x.mid, sb.append(x.c), queue);
        sb.deleteCharAt(sb.length() - 1);
        collect(x.right, sb, queue);
    }

    public static void main(String[] args) {
        String[] keys = {"abc", "cde"};
        TST<Integer> tst = new TST<>();
        for (int i = 0; i < 2; i++) {
            tst.put(keys[i], i);
        }
        tst.keys();
    }
}
