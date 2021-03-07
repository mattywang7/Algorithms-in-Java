package graphics;

/**
 * 要搜索一幅图，只需用一个递归方法来遍历所有顶点。在访问其中一个顶点时：
 * - 将它标记为已访问；
 * - 递归地访问它的所有没有被标记过的邻居顶点。
 * 这种方法称为深度优先搜索
 *
 * 命题：
 * 深度优先搜索标记与起点连通的所有顶点的时间和顶点的度数之和成正比。
 *
 * 深度优先搜索能够处理许多和图有关的任务：
 * - 两个给定的顶点是否连通？
 * - 图中有多少个连通子图？
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class DeepFirstSearch {

    private boolean[] marked;           // marked[v] = is there an s-v path?
    private int count;                  // number of vertices connected to s

    public DeepFirstSearch(Graphic G, int s) {
        marked = new boolean[G.V()];
        validateVertex(s);
        dfs(G, s);
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }
    }

    // depth first search from v
    private void dfs(Graphic G, int v) {
        count++;
        marked[v] = true;
        // 此循环体现了深度优先
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    /**
     * Is there a path between the source vertex s and vertex v?
     * Params: v – the vertex
     * Returns: true if there is a path, false otherwise
     * Throws: IllegalArgumentException – unless 0 <= v < V
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
        Graphic G = new Graphic(in);
        int s = Integer.parseInt(args[1]);
        DeepFirstSearch search = new DeepFirstSearch(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (search.marked(v)) {
                StdOut.print(v + " ");
            }
        }

        StdOut.println();
        if (search.count() != G.V()) {
            StdOut.println("Not connected");
        }
        else {
            StdOut.println("Connected");
        }
    }
}
