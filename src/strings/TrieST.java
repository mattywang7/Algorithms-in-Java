package strings;

import edu.princeton.cs.algs4.StdIn;
import util.Queue;

/**
 * 单词查找树的性能：
 * - 查找命中所需的时间与被查找的键的长度成正比
 * - 查找未命中只需检查若干个字符
 *
 * 单词查找树的性质：
 * 单词查找树也是由链接的结点所组成的数据结构，这些链接可能为空，也可能指向其它结点。
 * 每个结点都只可能有一个指向它的结点，称为它的父结点（根结点没有父结点）。
 * 每个结点都含有R条链接，其中R为字母表的大小。
 * 单词查找树一般会含有大量的空链接，绘制一棵单词查找树时一般会忽略空链接。
 * 每条链接都对应着一个字符——因为每条链接都只能指向一个结点
 * 每个结点含有一个相应的值，可以是空也可以是符号表中的某个键所关联的值。
 * 具体来说，我们将每个键所关联的值保存在该键的最后一个字母所对应的结点中。
 *
 * 查找：
 * 在所有的情况中，执行查找的方式就是在单词查找树中从根结点开始检查某条路径上的所有结点
 *
 * 插入：
 * 插入之前要进行一次查找，会有两种情况：
 * - 在到达键的尾字符之前就遇到了一个空链接。在这种情况下，单词查找树中不存在与键的尾字符对应的结点，
 * 因此需要为键中还未被检查的每个字符创建一个对应的结点并将键的值保存到最后一个字符的结点中。
 *
 * - 在遇到空链接之前就到达了键的尾字符。在这种情况下，将该点的值设为键所对应的值（无论该值是否为空）
 *
 * 在单词查找树中，键是由从根结点到含有非空值的结点的路径所隐式表示的。
 */

public class TrieST<Value> {

    private static final int R = 256;       // extended ASCII

    private Node root;
    private int n;                          // number of keys in trie

    // R-way trie node
    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

    public TrieST() {}

    /**
     * Returns the value associated with the given key.
     * @param key
     * @return
     */
    public Value get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null.");
        Node x = get(root, key, 0);
        if (x == null) {
            return null;
        }
        return (Value) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null.");
        }
        return get(key) != null;
    }

    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("argument to put() is null.");
        }
        if (val == null) {
            delete(key);
        }
        else {
            root = put(root, key, val, 0);
        }
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            if (x.val == null) {
                n++;
            }
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d + 1);
        // 一步步向上传递，最后return的就是已经查找完毕的root结点
        return x;
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns all the keys in the symbol table.
     * @return
     */
    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    /**
     * Returns all of the keys in the set that start with prefix.
     * @param prefix
     * @return
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) {
            return;
        }
        if (x.val != null) {
            results.enqueue(prefix.toString());
        }
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns all of the keys in the symbol table that match pattern, where . symbol is treated as a wildcard character.
     * @param pattern
     * @return
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new Queue<>();
        collect(root, new StringBuilder(), pattern, results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
        if (x == null) {
            return;
        }
        int d = prefix.length();
        if (d == pattern.length() && x.val != null) {
            results.enqueue(prefix.toString());
        }
        if (d == pattern.length()) {
            return;
        }
        char c = pattern.charAt(d);
        if (c == '.') {
            // 循环是因为每个ch都能和 . 匹配
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of query, or null, if no such string.
     * {"she", "sells", "sea", "shells", "by" "the" "shore"}
     * longestPrefixOf("shell") -> "she"
     * longestPrefixOf("shellsort") -> "shells"
     */
    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("argument to longestPrefixOf() is null.");
        }
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) {
            return null;
        }
        else {
            return query.substring(0, length);
        }
    }

    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) {
            return length;
        }
        if (x.val != null) {
            length = d;
        }
        if (d == query.length()) {
            return length;
        }
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d + 1, length);
    }

    /**
     * Removes the key in the set if the key is present
     * @param key
     */
    public void delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to delete() is null.");
        }
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            if (x.val != null) {
                n--;
            }
            x.val = null;
        }
        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        if (x.val != null) {
            return x;
        }
        for (int c = 0; c < R; c++) {
            if (x.next[c] != null) {
                return x;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        TrieST<Integer> st = new TrieST<>();
        String[] testStrings = {"she", "sells", "sea", "shells", "by", "the", "sea", "shore"};
        for (int i = 0; i < testStrings.length; i++) {
            st.put(testStrings[i], i);
        }

        if (st.size() < 100) {
            System.out.println("keys(\"\"):");
            for (String key : st.keys()) {
                System.out.println(key + " " + st.get(key));
            }
        }

        System.out.println("-----------------");
        System.out.println("longestPrefixOf(\"shellsort\")");
        System.out.println(st.longestPrefixOf("shellsort"));
    }
}
