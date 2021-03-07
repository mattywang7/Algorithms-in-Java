package graphics;

import edu.princeton.cs.algs4.In;
import util.Queue;
import util.Stack;

/**
 * The DepthFirstOrder class represents a data type for determining depth-first search ordering of the vertices in a
 * digraph or edge-weighted digraph, including preorder, postorder, and reverse postorder.
 */

public class DepthFirstOrder {
    private boolean[] marked;               // marked[v] = has v been marked in dfs
    private int[] pre;                      // pre[v] = preorder number of v
    private int[] post;                     // post[v] = postorder number of v
    private Queue<Integer> preorder;        // vertexes in preorder
    private Queue<Integer> postorder;       // vertexes in postorder
    private int preCounter;                 // counter for preorder numbering
    private int postCounter;                // counter for postorder numbering

    public DepthFirstOrder(DirectGraph G) {
        marked = new boolean[G.V()];
        pre = new int[G.V()];
        post = new int[G.V()];
        preorder = new Queue<>();
        postorder = new Queue<>();
        preCounter = 0;
        postCounter = 0;
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }

        assert check();
    }

    private void dfs(DirectGraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preorder.enqueue(v);
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        post[v] = postCounter++;
        postorder.enqueue(v);
    }

    /**
     *
     * @param v the vertex
     * @return the preorder number of vertex v
     */
    public int pre(int v) {
        validateVertex(v);
        return pre[v];
    }

    public int post(int v) {
        validateVertex(v);
        return post[v];
    }

    public Iterable<Integer> pre() {
        return preorder;
    }

    public Iterable<Integer> post() {
        return postorder;
    }

    /**
     * Why need this function?
     * @return the vertexes in reverse postorder
     */
    public Iterable<Integer> reversePost() {
        Stack<Integer> reverse = new Stack<>();
        for (Integer vertex : postorder) {
            reverse.push(vertex);
        }
        return reverse;
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("Vertex number must be non-negative.");
        }
    }

    /**
     * Check that if pre() and post() are consistent with pre(v) and post(v)
     * @return
     */
    private boolean check() {
        int r = 0;
        // check for pre() and pre(v)
        for (int v : pre()) {
            if (pre(v) != r++) {
                System.out.println("pre() and pre(v) are not consistent.");
                return false;
            }
        }

        r = 0;
        // check for post() and post(v)
        for (int v : post()) {
            if (post(v) != r++) {
                System.out.println("post() and post(v) are not consistent.");
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        DirectGraph G = new DirectGraph(in);

        DepthFirstOrder dfo = new DepthFirstOrder(G);
        System.out.println("   v  pre post");
        System.out.println("--------------");
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%4d %4d %4d\n", v, dfo.pre(v), dfo.post(v));
        }

        System.out.print("Preorder: ");
        for (int v : dfo.pre()) {
            System.out.print(v + " ");
        }
        System.out.println();

        System.out.print("Postorder: ");
        for (int v : dfo.post()) {
            System.out.print(v + " ");
        }
        System.out.println();

        System.out.print("Reverse postorder: ");
        for (int v : dfo.reversePost()) {
            System.out.print(v + " ");
        }
        System.out.println();
    }
}
