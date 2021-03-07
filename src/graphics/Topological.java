package graphics;

import edu.princeton.cs.algs4.SymbolDigraph;

/**
 * 优先级限制下的调度问题等价于计算有向无环图中的所有顶点的拓扑顺序
 *
 * 命题：
 * 当且仅当一幅有向图是无环图时它才能进行拓扑排序
 *
 * The Topological class represents a data type for determining a topological order of a directed acyclic graph (DAG).
 * A digraph has a topological order if and only if it is a DAG.
 * The hasOrder operation determines whether the digraph has a topological order, and if so, the order operation returns one.
 *
 * 解决任务调度类应用通常需要以下3步：
 * - 指明任务和优先级条件；
 * - 不断检测并去除有向图中的所有环，以确保存在可行方案；
 * - 使用拓扑排序解决调度问题
 */

public class Topological {
    private Iterable<Integer> order;        // topological order
    private int[] rank;                     // rank[v] = rank of vertex v in order

    public Topological(DirectGraph G) {
        DirectedCycle finder = new DirectedCycle(G);
        if (!finder.hasCycle()) {
            DepthFirstOrder dfo = new DepthFirstOrder(G);
            order = dfo.reversePost();
            rank = new int[G.V()];
            int i = 0;
            for (int v : order) {
                rank[v] = i++;
            }
        }
    }

    public Iterable<Integer> order() {
        return order;
    }

    /**
     * Does this DirectGraph have a topological order?
     * @return true if it has
     */
    public boolean hasOrder() {
        return order != null;
    }

    /**
     *
     * @param v the vertex number
     * @return the rank of the vertex v in the topological order; -1 if the DirectGraph is not a DAG
     */
    public int rank(int v) {
        validateVertex(v);
        if (hasOrder()) {
            return rank[v];
        }
        else {
            return -1;
        }
    }

    private void validateVertex(int v) {
        int V = rank.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("The vertex number is not valid.");
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        SymbolDigraph sd = new SymbolDigraph(filename, delimiter);
        edu.princeton.cs.algs4.Topological t = new edu.princeton.cs.algs4.Topological(sd.digraph());
        for (int v : t.order()) {
            System.out.println(sd.nameOf(v));
        }
    }
}
