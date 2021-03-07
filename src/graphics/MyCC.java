package graphics;

import edu.princeton.cs.algs4.In;

/**
 * dfs的下一个直接应用就是找出一幅图的所有连通分量，它能够将所有顶点都切分为等价类（连通分量）
 */

public class MyCC {

    private boolean[] marked;           // marked[v] = has vertex v been marked?
    private int[] id;                   // id[v] = id of connected component containing v
    private int[] size;                 // size[id] = number of vertexes in given component
    private int count;                  // number of connected components

    /**
     * Compute the connected components of the undirected graphic g (非连通图)
     * @param g the undirected graphic
     */
    public MyCC(Graphic g) {
        marked = new boolean[g.V()];
        id = new int[g.V()];
        size = new int[g.V()];
        for (int v = 0; v < g.V(); v++) {
            if (!marked[v]) {
                dfs(g, v);
                count++;
            }
        }
    }

    // depth-first search for a graphic
    private void dfs(Graphic g, int v) {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (int w : g.adj(v)) {
            if (!marked[w]) {
                dfs(g, w);
            }
        }
    }

    /**
     * @param v vertex
     * @return the component id of the connected component containing vertex v
     */
    public int id(int v) {
        validateVertex(v);
        return id[v];
    }

    /**
     * @param v vertex
     * @return the number of vertexes of the connected component containing vertex v
     */
    public int size(int v) {
        validateVertex(v);
        return size[id[v]];
    }

    /**
     * @return the number of connected components in the graphic g
     */
    public int count() {
        return count;
    }

    /**
     * @param v vertex v
     * @param w vertex w
     * @return if the vertex v and w is connected
     */
    public boolean connected(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return id[v] == id[w];
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException(v + " is not legal.");
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graphic g = new Graphic(in);
        MyCC cc = new MyCC(g);

        int m = cc.count();
        System.out.println(m + " components.");
    }
}
