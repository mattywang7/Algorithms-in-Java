package graphics;

import edu.princeton.cs.algs4.In;
import util.Bag;
import util.MattyRandom;
import util.Stack;

import java.util.NoSuchElementException;

/**
 * 最小生成树：给定一幅加权无向图，找到它的一棵最小生成树
 *
 * 图的生成树：是它的一棵含有其所有顶点的无环连通子图。
 *              一幅加权图的最小生成树（MST）是它的一棵权值（树中所有边的权值之和）最小的生成树
 *
 * 我们对生成树的定义意味着最小生成树只可能存在于连通图中
 * 所有边的权重都各不相同。如果不同边的权重可以相同，那么最小生成树就不一定唯一了
 *
 * 树的两个性质：
 * - 用一条边连接树中的任意两个顶点都会产生一个新的环；
 * - 从树中删去一条边将会得到两颗独立的树
 *
 * 图的一种切分是将图的所有顶点分为两个非空且不重叠的两个集合。横切边是一条连接两个属于不同集合的顶点的边
 * 切分定理：在一幅加权图中，给定任意的切分，它的横切边的权重最小者必然属于图的最小生成树
 *
 * 贪心算法：
 * 使用切分定理找到最小生成树的一条边，不断重复直到找到最小生成树的所有边
 *
 * 最小生成树的贪心算法：
 * 初始状态下所有边均为灰色，找到一种切分，它产生的横切边均不为黑色。将它权重最小的横切边标记为黑色。反复，直到标记了V-1条黑色边为止
 */

public class EdgeWeightedGraph {

    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Bag<Edge>[] adj;

    public EdgeWeightedGraph(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Vertex number must be non-negative.");
        }
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<>();
        }
    }

    /**
     * Initializes a random edge-weighted graph with V vertices and E edges.
     * @param V number of vertices
     * @param E number of edges
     */
    public EdgeWeightedGraph(int V, int E) {
        this(V);
        if (E <= 0) {
            throw new IllegalArgumentException("Argument E to constructor must be positive: " + E);
        }
        for (int i = 0; i < E; i++) {
            int v = MattyRandom.uniform(V);
            int w = MattyRandom.uniform(V);
            double weight = Math.round(100 * MattyRandom.uniform()) / 100.0;
            Edge edge = new Edge(v, w, weight);
            addEdge(edge);
        }
    }

    public EdgeWeightedGraph(In in) {
        if (in == null) {
            throw new IllegalArgumentException("Argument In to constructor is null.");
        }

        try {
            this.V = in.readInt();
            adj = (Bag<Edge>[]) new Bag[V];
            for (int i = 0; i < V; i++) {
                adj[i] = new Bag<>();
            }

            int E = in.readInt();
            if (E < 0) {
                throw new IllegalArgumentException("Edges read is not positive.");
            }
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                validateVertex(v);
                validateVertex(w);
                double weight = in.readDouble();
                Edge edge = new Edge(v, w, weight);
                addEdge(edge);
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format for EdgeWeightedGraph constructor", e);
        }
    }

    /**
     * Initialize a new edge-weighted graph deep copy to G
     * @param G another EdgeWeightedGraph
     */
    public EdgeWeightedGraph(EdgeWeightedGraph G) {
        this(G.V());
        this.E = G.E();
        for (int i = 0; i < V; i++) {
            Stack<Edge> reverse = new Stack<>();
            for (Edge e : G.adj(i)) {
                reverse.push(e);
            }
            for (Edge e : reverse) {
                adj[i].add(e);
            }
        }
    }

    /**
     * Add the undirected edge to graph
     * @param edge an undirected edge
     */
    public void addEdge(Edge edge) {
        int v = edge.either();
        int w = edge.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v].add(edge);
        adj[w].add(edge);
        E++;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    /**
     *
     * @param v the vertex
     * @return Returns the edges incident on vertex v.
     */
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Edge e : adj[v]) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) {
                        list.add(e);
                    }
                    selfLoops++;
                }
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V).append(" ").append(E).append(NEWLINE);
        for (int i = 0; i < V; i++) {
            s.append(i).append(": ");
            for (Edge e : adj[i]) {
                s.append(e).append("  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        System.out.println(G);
    }
}
