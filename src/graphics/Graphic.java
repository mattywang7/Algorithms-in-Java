package graphics;

/**
 * 非稠密图的标准表示成为邻接表的数据结构，它将每个顶点的所有相邻顶点都保存为在该顶点对应的元素所指向的一张链表中。
 * 我们使用这个数组就是为了快速访问给定顶点的邻接顶点列表。
 */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class Graphic {

    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;                    // Number of vertexes
    private int E;                          // Number of edges
    private Bag<Integer>[] adj;             // All the vertexes that are adjacent to vertex V
                                            // This is a Bag[], the number of the array is the same as the number of vertexes

    public Graphic(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative.");
        this.V = V;
        this.E = 0;
        // Each vertex has a Bag<Integer> as its adjacent vertices.
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    public Graphic(In in) {
        if (in == null) throw new IllegalArgumentException("Argument to Graphic() is null.");
        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative.");
            adj = (Bag<Integer>[]) new Bag[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new Bag<Integer>();
            }

            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("Number of edges must be non-negative.");
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                validateVertex(v);
                validateVertex(w);
                addEdge(v, w);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }
    }

    public Graphic(Graphic G) {
        this.V = G.V();
        this.E = G.E();
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative.");

        // update adjacency lists
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }

        for (int v = 0; v < G.V(); v++) {
            Stack<Integer> reverse = new Stack<>();
            for (int w : G.adj[v]) {
                reverse.push(w);
            }

            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }
    }

    /**
     * Add the undirected edge v-w to the graph
     * if v == w, then this edge would be a self loop (自环).
     * @param v - one vertex of this edge
     * @param w - the other vertex of this edge
     */
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }

    /**
     *
     * @return the number of vertexes
     */
    public int V() {
        return V;
    }

    /**
     *
     * @return return the number of edges
     */
    public int E() {
        return E;
    }

    /**
     * Returns the vertices adjacent to vertex v.
     * @param v - vertex
     * @return the vertices adjacent to vertex v
     */
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     *
     * @param v vertex
     * @return the number of adjacent vertexes of vertex v
     */
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(V + " vertices; " + E + " edges " + NEWLINE);

        for (int v = 0; v < V; v++) {
            result.append(v + ": ");
            for (int w : adj[v]) {
                result.append(w + " ");
            }
            result.append(NEWLINE);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        In graphic = new In(args[0]);
        Graphic g = new Graphic(graphic);
        StdOut.println(g);
    }

}
