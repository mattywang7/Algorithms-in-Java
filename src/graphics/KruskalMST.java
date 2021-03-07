package graphics;

import edu.princeton.cs.algs4.In;
import util.MinPQ;
import util.Queue;
import util.UF;

/**
 * 按照边的权重顺序（从小到大）处理它们
 * 将边加入最小生成树中，加入的边不会与已经加入的边构成环，直到树中含有 V - 1 条边为止
 *
 * 加入的边不会与已有的边构成环这一点是关键
 *
 * 我们从一片由V棵单顶点的树构成的森林开始并不断地将两棵树合并（用可以找到的最短边）直到只剩下一棵树，这棵树就是最小生成树
 *
 * 用一个union-find数据结构来识别会形成环的边
 */

public class KruskalMST {

    private static final double FLOAT_POINT_EPSILON = 1E-12;

    private double weight;
    private Queue<Edge> mst = new Queue<>();

    public KruskalMST(EdgeWeightedGraph G) {
        MinPQ<Edge> pq = new MinPQ<>();
        for (Edge e : G.edges()) {
            pq.insert(e);
        }
        UF uf = new UF(G.V());
        // loop will end when mst.size() == G.V() - 1
        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            Edge e = pq.delMin();
            int v = e.either();
            int w = e.other(v);
            if (uf.find(v) == uf.find(w)) {
                continue;
            }
            uf.union(v, w);
            mst.enqueue(e);
            weight += e.weight();
        }
    }

    public double weight() {
        return weight;
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        KruskalMST kMST = new KruskalMST(G);
        for (Edge e : kMST.edges()) {
            System.out.println(e);
        }
        System.out.printf("%.5f\n", kMST.weight());
    }
}
