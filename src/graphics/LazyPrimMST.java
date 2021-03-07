package graphics;

import edu.princeton.cs.algs4.In;
import util.MinPQ;
import util.Queue;

/**
 * 每一步都会为一棵生长中的树添加一条边
 * 一开始这棵树只有一个顶点，然后会向它添加V - 1条边，每次总是将下一条连接树中的顶点与不在树中的顶点且权重最小的边加入树中
 *
 * 顶点：
 * marked[v] = true if v is in the tree
 *
 * 横切边：
 * 使用MinPQ<Edge>来根据权重比较所有边
 */

public class LazyPrimMST {

    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private double weight;                                  // total weight of MST
    private Queue<Edge> mst;                                // edges in the MST
    private boolean[] marked;                               // marked[v] = true iff v on tree
    private MinPQ<Edge> pq;                                 // edges with one endpoint in tree

    public LazyPrimMST(EdgeWeightedGraph G) {
        mst = new Queue<>();
        marked = new boolean[G.V()];
        pq = new MinPQ<>();
        weight = 0;
        // 感觉这个循环不是很必要
        for (int i = 0; i < G.V(); i++) {
            if (!marked[i]) {
                prim(G, i);
            }
        }
    }

    // add all edges e incident to v onto pq if the other endpoint has not yet been scanned
    private void scan(EdgeWeightedGraph G, int v) {
        assert !marked[v];
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            if (!marked[e.other(v)]) {
                pq.insert(e);
            }
        }
    }

    // run Prim's algorithm
    private void prim(EdgeWeightedGraph G, int s) {
        scan(G, s);
        while (!pq.isEmpty()) {
            Edge e = pq.delMin();
            int v = e.either();
            int w = e.other(v);
            assert marked[v] || marked[w];
            if (marked[v] && marked[w]) {
                continue;
            }
            mst.enqueue(e);
            weight += e.weight();
            if (!marked[v]) {
                scan(G, v);
            }
            if (!marked[w]) {
                scan(G, w);
            }
        }
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
        return weight;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        LazyPrimMST mst = new LazyPrimMST(G);
        for (Edge e : mst.edges()) {
            System.out.println(e);
        }
        System.out.printf("%.5f\n", mst.weight());
    }
}
