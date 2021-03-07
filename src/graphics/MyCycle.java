package graphics;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * This class represents a data type for determining whether an undirected graphic has a simple cycle.
 * (排除自环或者平行边，这个图是不是无环图)
 *
 * 自环指的是一条连接一个顶点和该顶点自身的一条边
 * 连接同一对顶点的两条边称为平行边
 *
 * 无环图是一种不包含环的图，树是一种典型的无环图
 */

public class MyCycle {

    private boolean[] marked;
    private int[] edgeTo;
    private Stack<Integer> cycle;

    /**
     * Determine whether the undirected graph g has a cycle and, if so, find such a cycle
     * @param g graphic
     */
    public MyCycle(Graphic g) {
        if (hasSelfLoop(g)) {
            return;
        }
        if (hasParallelEdges(g)) {
            return;
        }
        marked = new boolean[g.V()];
        edgeTo = new int[g.V()];
        for (int v = 0; v < g.V(); v++) {
            if (!marked[v]) {
                dfs(g, -1, v);
            }
        }
    }

    // Does this graph has a self loop?
    // Side effect: initialize cycle to have a self loop
    private boolean hasSelfLoop(Graphic g) {
        for (int v = 0; v < g.V(); v++) {
            for (int w : g.adj(v)) {
                if (v == w) {
                    cycle = new Stack<>();
                    cycle.push(v);
                    cycle.push(v);
                    return true;
                }
            }
        }
        return false;
    }

    // Does this graph has two parallel edges?
    // Side effect: initialize cycle to have two parallel edges
    private boolean hasParallelEdges(Graphic g) {
        marked = new boolean[g.V()];

        // 如果有平行边，i.e. 0-1 1-0， g.adj(0) = {1, 1}
        for (int v = 0; v < g.V(); v++) {
            for (int w : g.adj(v)) {
                if (marked[w]) {
                    cycle = new Stack<>();
                    cycle.push(v);
                    cycle.push(w);
                    cycle.push(v);
                    return true;
                }
                marked[w] = true;
            }

            // reset all the adjacent vertexes to false
            for (int w : g.adj(v)) {
                marked[w] = false;
            }
        }
        return false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }

    private void dfs(Graphic g, int u, int v) {
        marked[v] = true;
        for (int w : g.adj(v)) {

            // short circuit if cycle already found
            if (cycle != null) return;

            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(g, v, w);
            }
            // check for cycle (but disregard reverse of edge leading to v)
            // w != u 意味着v的w与edgeTo[v]不同
            else if (w != u) {
                cycle = new Stack<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graphic g = new Graphic(in);
        MyCycle finder = new MyCycle(g);
        if (finder.hasCycle()) {
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
        else {
            StdOut.println("g is acyclic.");
        }
    }
}
