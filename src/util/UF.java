package util;

import edu.princeton.cs.algs4.StdIn;

/**
 * The UF class represents a union–find data type (also known as the disjoint-sets data type).
 * It supports the classic union and find operations, along with a count operation that returns the total number of sets.
 */

public class UF {

    private int[] parent;
    private byte[] rank;
    private int count;

    public UF(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        parent = new int[n];
        rank = new byte[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int p) {
        validateIndex(p);
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    public int count() {
        return count;
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) {
            return;
        }
        count--;
        if (rank[rootP] > rank[rootQ]) {
            parent[rootQ] = rootP;
        }
        else if (rank[rootP] < rank[rootQ]) {
            parent[rootP] = rootQ;
        }
        else {
            parent[rootQ] = rootP;
            rank[rootP]++;
        }
    }

    private void validateIndex(int i) {
        int n = parent.length;
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        UF uf = new UF(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.find(p) == uf.find(q)) {
                continue;
            }
            uf.union(p, q);
            System.out.println(p + " " + q);
        }
        System.out.println(uf.count() + " components.");
    }
}
