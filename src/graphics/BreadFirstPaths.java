package graphics;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * 广度优先搜索：
 * 深度优先搜索得到的路径不仅取决于图的结构，还取决于图的表示和递归调用的性质
 * 我们很自然地还对如下内容感兴趣：
 * 单点最短路径：给定一幅图和一个起点s，回答"从s到给定目的顶点v是否存在一条路径？如果有，找出其中最短的那条"
 *
 * 要找到从s到v的最短路径，从s开始，在所有由一条边就可以到达的顶点中查找v，
 * 如果找不到我们就继续在与s距离两条边的所有顶点中查找v，如此一直进行。
 * 广度优先搜索就好像是一组人在一起朝各个方向走迷宫。
 *
 * 不像深度优先搜索的dfs使用了递归从而进行隐式的栈调用，bfs没有使用递归而是使用了一个显式的队列
 * 到所有顶点的最短路径的长度在构造函数里已经计算好了，存放在distTo里面
 *
 * 这段用例找出图中从构造函数得到的起点s到与其他所有顶点的最短路径
 * 对于从s可达的任意顶点v，广度优先搜索都能找到一条从s到v的最短路径（没有其他从s到v的路径所含的边比这条路径更少）
 *
 * 广度优先搜索所需的时间在最坏情况下和V + E成正比
 *
 * 在搜索中我们都会将起点加入数据结构中，然后重复以下步骤直到数据结构被清空：
 * - 取其中的下一个顶点并标记它；
 * - 将v的所有相邻但又未被标记的顶点加入数据结构
 *
 * 深度优先搜索不断深入图中并在栈中保存了所有分叉的顶点；
 * 广度优先搜索则像扇面一般扫描图，用一个队列保存访问过的最前端的顶点。
 *
 * dfs探索一幅图的方式是寻找离起点更远的顶点，只有在碰到死胡同时才访问近处的顶点；
 * bfs则会首先覆盖起点附近的顶点，只在临近的所有顶点都被访问了之后才会前进。
 */

public class BreadFirstPaths {

    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;
    private int[] edgeTo;                   // edgeTo[v] - previous edge on shortest s-v path
    private int[] distTo;                   // distTo[v] - number of edges shortest s-v path

    public BreadFirstPaths(Graphic G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        validateVertex(s);
        bfs(G, s);
    }

    /**
     * Computes the shortest path between any one of the source vertices in sources and every other vertex in Graphic G
     * @param G the graphic
     * @param sources the source vertices
     */
    public BreadFirstPaths(Graphic G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = INFINITY;
        }
        validateVertices(sources);
        bfs(G, sources);
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }
    }

    /**
     * iteratively call validateVertex(int v)
     * @param vertices
     */
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) throw new IllegalArgumentException("argument is null");
        for (Integer vertex : vertices) {
            if (vertex == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            validateVertex(vertex);
        }
    }

    /**
     * breadth-first search from a single source
     * @param G the graphic
     * @param s the source vertex
     */
    private void bfs(Graphic G, int s) {
        Queue<Integer> q = new Queue<>();
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = INFINITY;
        }
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    private void bfs(Graphic G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<>();
        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
        }

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    /**
     * Is there a path between the source vertex s (or sources) and vertex v?
     * @param v the vertex
     * @return true if there is a path, and false otherwise
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    /**
     *
     * @param v the vertex
     * @return the number of edges in a shortest path
     */
    public int distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<Integer> path = new Stack<>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(x);
        return path;
    }

    // check optimality conditions for single source
    private boolean check(Graphic G, int s) {
        // check that the distance of s = 0
        if (distTo[s] != 0) {
            StdOut.println("distance of source " + s + " to itself = " + distTo[s]);
            return false;
        }

        // check that for each edge v-w dist[w] <= dist[v] + 1
        // provided v is reachable from s
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (hasPathTo(v) != hasPathTo(w)) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("hasPathTo(" + v + ") = " + hasPathTo(v));
                    StdOut.println("hasPathTo(" + w + ") = " + hasPathTo(w));
                    return false;
                }

                if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("distTo[" + v + "] = " + distTo[v]);
                    StdOut.println("distTo[" + w + "] = " + distTo[w]);
                    return false;
                }
            }
        }

        for (int w = 0; w < G.V(); w++) {
            if (!hasPathTo(w) || w == s) continue;
            int v = edgeTo[w];
            if (distTo[w] != distTo[v] + 1) {
                StdOut.println("shortest path edge " + v + "-" + w);
                StdOut.println("distTo[" + v + "] = " + distTo[v]);
                StdOut.println("distTo[" + w + "] = " + distTo[w]);
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graphic G = new Graphic(in);
        int s = Integer.parseInt(args[1]);
        BreadFirstPaths bfs = new BreadFirstPaths(G, s);

//        for (int v = 0; v < G.V(); v++) {
//            if (bfs.hasPathTo(v)) {
//                StdOut.printf("%d to %d (%d): ", s, v, bfs.distTo(v));
//                for (int x : bfs.pathTo(v)) {
//                    if (x == s) StdOut.print(x);
//                    else        StdOut.print("-" + x);
//                }
//                StdOut.println();
//            }
//
//            else {
//                StdOut.printf("%d to %d (-): not connected\n", s, v);
//            }
//        }
        int v = 4;
        System.out.println(bfs.distTo(v));
    }

}
