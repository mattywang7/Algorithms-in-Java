package graphics;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 在典型应用中，图都是通过文件或者网页定义的，使用的是字符串而非整数来表示顶点。
 *
 * MyST的内部实现是TreeMap, 也就是红黑树
 *
 * 这个测试用例是一个电影和演员的二分图 （二分图：电影顶点之间或者演员节点之间都没有边），边只存在于电影和演员之间或者演员和电影之间。
 * 用户可以输入一个演员的名字来查看数据库中他所演出的影片列表；
 * 输入一部电影的名字来得到它的演员列表，这不过是在照搬文件中对应行数据，但输入演员的名字来得到影片的列表则相当于查找反响索引。
 * 尽管数据库的构造是为了将电影名连接到演员，二分图模型同时也意味着将演员连接到电影名。
 *
 * 二分图的性质自动完成了反响索引
 */

public class SymbolGraphic {
    private MyST<String, Integer> st;           // String -> index
    private String[] keys;                      // index -> String
    private Graphic graph;                      // the underlying graph

    /**
     * Initializes a graph from a file using the specified delimiter. Each line in the file contains the name of a
     * vertex, followed by a list of the names of the vertices adjacent to that vertex, separated by the delimiter.
     * @param filename - the name of the file
     * @param delimiter - the delimiter between fields
     */
    public SymbolGraphic(String filename, String delimiter) {
        st = new MyST<>();

        // First pass builds the index by reading strings to associate
        // distinct strings with an index
        In in = new In(filename);
        while (!in.isEmpty()) {
            String[] a = in.readLine().split(delimiter);
            for (int i = 0; i < a.length; i++) {
                if (!st.containsKey(a[i])) {
                    st.put(a[i], st.size());
                }
            }
        }

        // inverted index to get string keys in an array
        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        // second pass builds the graph by connecting first vertex on each
        // line to all others
        graph = new Graphic(st.size());
        in = new In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            int v = st.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                int w = st.get(a[i]);
                graph.addEdge(v, w);
            }
        }
    }

    /**
     * Does the graph contains the vertex named s?
     * @param s - the name
     * @return true if s is the name of a vertex, and false otherwise
     */
    public boolean contains(String s) {
        return st.containsKey(s);
    }

    /**
     * @param s - the name of a vertex
     * @return the integer (between 0 and V - 1) associated with vertex named s
     */
    public int indexOf(String s) {
        return st.get(s);
    }

    /**
     * @param v the vertex
     * @return the name associated with the vertex
     */
    public String nameOf(int v) {
        return keys[v];
    }

    /**
     * @return the graph associated with the symbol graph.
     */
    public Graphic graphic() {
        return graph;
    }

    private void validateVertex(int v) {
        int V = graph.V();
        if (v < 0 || v >= V) throw new IllegalArgumentException(v + " is not between 0 and V - 1");
    }

    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        SymbolGraphic sg = new SymbolGraphic(filename, delimiter);
        Graphic g = sg.graphic();
        while (StdIn.hasNextLine()) {
            String source = StdIn.readLine();
            if (sg.contains(source)) {
                int v = sg.indexOf(source);
                for (int w : g.adj(v)) {
                    StdOut.println("    " + sg.nameOf(w));
                }
            }
            else {
                StdOut.println("input not contain '" + source + "'.");
            }
        }
    }
}
