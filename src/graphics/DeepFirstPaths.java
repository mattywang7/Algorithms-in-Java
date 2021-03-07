package graphics;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * 单点路径：
 * 给定一幅图和一个起点s，回答"从s到给定目的顶点v是否存在一条路径？如果有，找出这条路径。"
 *
 * 命题：使用深度优先搜索得到从给定起点到任意标记顶点的路径所需的时间与路径的长度成正比
 */

public class DeepFirstPaths {

    private int s;                  // source vertex
    private boolean[] marked;       // marked[v] - is there an s-v path?
    private int[] edgeTo;           // edgeTo[v] - last edge on s-v path

    public DeepFirstPaths(Graphic G, int s) {
        this.s = s;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        validateVertex(s);
        dfs(G, s);
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }
    }

    private void dfs(Graphic G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

    /**
     *
     * @param v vertex v
     * @return true if there is a path between vertex source s and vertex v
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) {
            return null;
        }

        Stack<Integer> path = new Stack<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graphic G = new Graphic(in);
        int s = Integer.parseInt(args[1]);
        DeepFirstPaths dfp = new DeepFirstPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (dfp.hasPathTo(v)) {
                StdOut.printf("%d to %d: ", s, v);
                for (int x : dfp.pathTo(v)) {
                    if (x == s) {
                        StdOut.print(x);
                    }
                    else {
                        StdOut.print(" - " + x);
                    }
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d not connected.", s, v);
            }
        }
    }
}
