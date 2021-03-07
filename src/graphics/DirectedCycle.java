package graphics;

import edu.princeton.cs.algs4.In;
import util.Stack;

/**
 * 有向路径：由一系列顶点组成，对于其中的每个顶点都存在一条有向边从它指向序列中的下一个顶点 （那么终点的那个顶点呢？）
 *
 * 有向环：一条至少含有一条边且起点和终点相同的有向路径
 *
 * 调度问题：
 * 一种应用广泛的模型是给定一组任务并安排它们的执行顺序，限制条件是这些任务的执行方法和起始时间。
 * 限制条件还可能包括任务的时耗以及消耗的其他资源。
 * 最重要的一种限制条件叫做优先级限制，它指明了哪些任务必须哪些任务之前完成。
 *
 * 优先级限制下的调度问题：
 * 给定一组需要完成的任务，以及一组关于任务完成的先后次序的优先级限制。
 * 在满足限制条件的前提下应该如何安排并完成所有任务？
 *
 * 以上的问题在有向图中可以等价于拓扑排序：
 * 给定一幅有向图，将所有的顶点排序，使得所欲的有向边均从排在前面的元素指向排在后面的元素（或者说明无法做到这一点）
 *
 * 有向图中的环：
 * x -> y, y -> z, z -> x
 * 这个优先级限制的问题是无解的。
 * 一般来说，如果一个有优先级限制的问题中存在有向环，那么这个问题肯定是无解的。
 * 所以需要有向环检测（只需找出一个环即可）
 *
 * 一旦我们找到了一条有向边 v -> w 且 w 已存在于栈中，就找到了一个环。
 *
 * boolean[] onStack 是用来保存递归调用期间栈上的所有顶点
 * 在执行 dfs(G, v) 时，查找的是一条由起点到v的有向路径。
 * 要保存这条路径，维护一个由顶点索引的数组onStack[], 以标记递归调用的栈上的所有顶点
 * 在调用 dfs(G, v) 时将 onStack[v] 设为true，在调用结束时将其设为 false.
 */

public class DirectedCycle {
    private boolean[] marked;
    private int[] edgeTo;
    private boolean[] onStack;      // onStack[v] = if vertex v on the stack
    private Stack<Integer> cycle;   // directed cycle (or null if no such cycle)

    /**
     * Determines whether the DirectGraph G has a directed cycle and, if so, finds such a cycle
     * @param G
     */
    public DirectedCycle(DirectGraph G) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        onStack = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v] && cycle == null) {
                dfs(G, v);
            }
        }
    }

    /**
     * run DFS and find a directed cycle (if one exists)
     * @param G the DirectGraph
     * @param v the source vertex
     */
    private void dfs(DirectGraph G, int v) {
        marked[v] = true;
        onStack[v] = true;
        for (int w : G.adj(v)) {
            if (cycle != null) {
                return;
            }
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
            else if (onStack[w]) {
                cycle = new Stack<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);

                assert check();
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }

    /**
     * Certificate that DirectGraph has a directed cycle if it reports one
     * @return true if DirectGraph has a directed cycle, false otherwise
     */
    private boolean check() {
        if (hasCycle()) {
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) {
                    first = v;
                }
                last = v;
            }
            if (first != last) {
                System.err.printf("Cycle begins with %d and ends with %d, thus this is not a cycle.\n", first, last);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        DirectGraph G = new DirectGraph(in);

        DirectedCycle finder = new DirectedCycle(G);

        if (finder.hasCycle()) {
            System.out.println("Directed Cycle: ");
            StringBuilder stringBuilder = new StringBuilder();
            for (int vertex : finder.cycle()) {
                stringBuilder.append(vertex).append(" -> ");
            }
            int stringLength = stringBuilder.length();
            stringBuilder.delete(stringLength - 4, stringLength);
            System.out.println(stringBuilder.toString());
        }
        else {
            System.out.println("No directed cycle found.");
        }
    }
}
