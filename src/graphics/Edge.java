package graphics;

/**
 * The Edge class represents a weighted edge in an EdgeWeightedGraph.
 * Each edge consists of two integers (naming the two vertices) and a real-value weight.
 * The data type provides methods for accessing the two endpoints of the edge and the weight.
 * The natural order for this data type is by ascending order of weight.
 */

public class Edge implements Comparable<Edge> {

    private final int v;
    private final int w;
    private final double weight;

    /**
     * Initialize an edge between vertex v and w of the given weight
     * @param v vertex v
     * @param w vertex w
     * @param weight the given weight of the edge
     */
    public Edge(int v, int w, double weight) {
        if (v < 0) {
            throw new IllegalArgumentException("Vertex number must be non-negative.");
        }
        if (w < 0) {
            throw new IllegalArgumentException("Vertex number must be non-negative.");
        }
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException("Weight is not a number.");
        }

        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    /**
     *
     * @return either endpoint of this edge
     */
    public int either() {
        return v;
    }

    /**
     *
     * @param vertex
     * @return the other endpoint of this edge different from the given vertex
     */
    public int other(int vertex) {
        if (vertex == v) {
            return w;
        }
        else if (vertex == w) {
            return v;
        }
        else {
            throw new IllegalArgumentException("Illegal endpoint.");
        }
    }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.5f", v, w, weight);
    }
}
