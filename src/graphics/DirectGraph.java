package graphics;


import edu.princeton.cs.algs4.In;
import util.Bag;
import util.Stack;

import java.util.NoSuchElementException;

/**
 * 一条有向边由第一个顶点指出并指向第二个顶点。
 * 在一幅有向图中，一个顶点的出度为由该顶点指出的边的总数；一个顶点的入度为指向该顶点的边的总数
 * v -> w
 *
 * 有向图中会出现双线连接，即既有 v -> w, 又有 w -> v
 *
 * 约定每个顶点都能到达它自己，v能到达w不意味着w也能到达v
 *
 * 处理有向图就如同在一座只有单行道的城市中穿梭，而且这些单行道的方向是杂乱无章的
 *
 * 需要 int[] inDegree 这个变量是因为，顶点的outDegree很好计算，就是adj[v].size()，inDegree就没那么好计算了，所以最好采取一个变量进行记录
 */
public class DirectGraph {

    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;            // the number of vertexes in the graph
    private int E;                  // the number of edges in the graph
    private Bag<Integer>[] adj;     // adj[v] = adjacency list for vertex v
    private int[] inDegree;         // inDegree[v] = inDegree of vertex v

    /**
     * Initialize an empty direct graph with v vertexes.
     * @param v the number of vertexes
     */
    public DirectGraph(int v) {
        if (v < 0) {
            throw new IllegalArgumentException("Number for vertexes in a graph must be non-negative.");
        }
        this.V = v;
        this.E = 0;
        inDegree = new int[V];
        adj = (Bag<Integer>[]) new Bag[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new Bag<>();
        }
    }

    public DirectGraph(In in) {
        if (in == null) {
            throw new IllegalArgumentException("Argument to DirectGraph(In in) is null");
        }
        try {
            this.V = in.readInt();
            if (V < 0) {
                throw new IllegalArgumentException("Number for vertexes in a graph must be non-negative.");
            }
            inDegree = new int[V];
            adj = (Bag<Integer>[]) new Bag[V];
            for (int i = 0; i < V; i++) {
                adj[i] = new Bag<>();
            }

            int e = in.readInt();
            if (e < 0) {
                throw new IllegalArgumentException("Edges must be non-negative.");
            }
            for (int i = 0; i < e; i++) {
                int v = in.readInt();
                int w = in.readInt();
                addEdge(v, w);
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Digraph constructor", e);
        }
    }

    /**
     * deep copy using dg
     * @param dg the direct graph to copy
     */
    public DirectGraph(DirectGraph dg) {
        if (dg == null) {
            throw new IllegalArgumentException("It's not a valid DirectGraph");
        }
        this.V = dg.V();
        this.E = dg.E();
        if (V < 0) {
            throw new IllegalArgumentException("Vertexes must be non-negative.");
        }

        inDegree = new int[V];
        for (int v = 0; v < dg.V(); v++) {
            inDegree[v] = dg.inDegree(v);
        }

        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<>();
        }

        // Use a Stack
        // reverse so that the list could be the original order of dg
        for (int v = 0; v < V; v++) {
            Stack<Integer> reverse = new Stack<>();
            for (int w : dg.adj(v)) {
                reverse.push(w);
            }
            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= this.V) {
            throw new IllegalArgumentException("vertex number v is not valid.");
        }
    }

    /**
     * Add the directed edge v -> w to the direct graph
     * @param v out vertex (the tail vertex)
     * @param w in vertex (the head vertex)
     */
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        inDegree[w]++;
        E++;
    }

    public int V() {
        return this.V;
    }

    public int E() {
        return this.E;
    }

    /**
     * Returns the adjacent vertexes from v in this dg
     * @param v source vertex
     * @return the adjacent vertexes from v in this dg, as an iterable
     */
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int outDegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public int inDegree(int v) {
        validateVertex(v);
        return inDegree[v];
    }

    /**
     *
     * @return the reverse of the direct graph
     */
    public DirectGraph reverse() {
        DirectGraph reverseDg = new DirectGraph(this.V);
        for (int i = 0; i < V; i++) {
            for (int w : adj(i)) {
                reverseDg.addEdge(w, i);
            }
        }
        return reverseDg;
    }

    /**
     *
     * @return the number of vertexes V, followed by the number of edges E, followed by the V adjacency lists
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V).append(" vertexes, ").append(E).append(" edges").append(NEWLINE);
        for (int v = 0; v < V; v++) {
            sb.append(String.format("%d: ", v));
            for (int w : adj[v]) {
                sb.append(String.format("%d ", w));
            }
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        DirectGraph dg = new DirectGraph(in);
        System.out.println(dg);
    }
}
