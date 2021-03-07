package graphics;

/**
 * DepthFirstSearch解决了单点连通性的问题，使得用例可以判定其他顶点和给定顶点的起点是否连通。
 *
 * 单点可达性：给定一幅有向图和一个起点s，回答"是否存在一条从s到达给定顶点v的有向路径"
 *
 * 这份API还可以解决一个问题，就是多点可达性：
 * 给定一幅有向图和顶点的集合，回答"是否存在一条从集合中的任意顶点到达给定顶点v的有向路径？"
 *
 * 标记-清除的垃圾收集：
 * 多点可达性的一个重要实际应用是在典型的内存管理系统中，包括许多Java的实现。
 * 在一幅有向图中，一个顶点表示一个对象，一条边则表示一个对象对另一个对象的引用。
 * 这个模型很好地表现了运行中的Java程序的内存使用状况。
 * 在程序执行的任何时候都有某些对象是可以被直接访问的，而不能通过这些对象访问的所有对象都应该被回收以便释放内存。
 * 标记-清除的垃圾回收策略会对每个对象保留一个位做垃圾收集之用。
 * 它会周期性地运行一个类似于DirectDFS的有向图可达性算法来标记所有可以被访问到的对象，然后清理所有对象，回收没有被标记的对象，以腾出内存供新的对象使用。
 */

import edu.princeton.cs.algs4.In;
import util.Bag;

/**
 * The class represents a data type that determines the vertexes reachable from a given source vertex
 * (or a set of source vertexes) in a direct graph.
 */
public class DirectedDFS {
    private boolean[] marked;           // marked[v] = true iff v is reachable from source(s)
    private int count;                  // number of reachable vertexes from source(s)

    /**
     * Computes the vertexes in the direct graph that are reachable from source vertex s
     * @param G the direct graph
     * @param s the source vertex
     */
    public DirectedDFS(DirectGraph G, int s) {
        marked = new boolean[G.V()];
        validateVertex(s);
        dfs(G, s);
    }

    /**
     * Computes the vertexes in the direct graph that are connected to any of the sources
     * @param G the direct graph
     * @param sources the sources
     */
    public DirectedDFS(DirectGraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        validateVertexes(sources);
        for (int v : sources) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
    }

    private void dfs(DirectGraph G, int s) {
        count++;
        marked[s] = true;
        for (int w : G.adj(s)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("Number of vertexes must be non-negative.");
        }
    }

    private void validateVertexes(Iterable<Integer> vertexes) {
        if (vertexes == null) {
            throw new IllegalArgumentException("Argument to validateVertexes() is null.");
        }
        for (Integer v : vertexes) {
            if (v == null) {
                throw new IllegalArgumentException("One vertex in vertexes is null.");
            }
            validateVertex(v);
        }
    }

    /**
     * Is there a directed path between from the source vertex and vertex v?
     * @param v given vertex
     * @return
     */
    public boolean marked(int v) {
        validateVertex(v);
        return marked[v];
    }

    public int count() {
        return count;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        DirectGraph G = new DirectGraph(in);

        Bag<Integer> sources = new Bag<>();
        for (int i = 1; i < args.length; i++) {
            sources.add(Integer.parseInt(args[i]));
        }

        DirectedDFS dfs = new DirectedDFS(G, sources);

        for (int i = 0; i < G.V(); i++) {
            if (dfs.marked(i)) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
}
