import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
//import edu.princeton.cs.algs4.Queue;

public class SAP {
    private final Digraph graph;
    private int shortestPath;
    private int ancestorId;

    /**
     * constructor takes a digraph (not necessarily a DAG)
     *
     * @param G
     */
    public SAP(Digraph G) {
        this.graph = new Digraph(G);
        shortestPath = -1;
        ancestorId = -1;
    }

    /**
     * Return length of shortest ancestral path between v and w; -1 if no such pat
     *
     * @param v
     * @param w
     * @return length of shortest sap for (v and w)
     */
    public int length(int v, int w) {
        calculateSAP(v, w);
        return shortestPath;
    }

    /**
     * Return a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     *
     * @param v
     * @param w
     * @return common ancestor Id of v and w
     */
    public int ancestor(int v, int w) {
        calculateSAP(v, w);
        return ancestorId;
    }

    /**
     * Return length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     *
     * @param v
     * @param w
     * @return length of shortest sap for (Iterable v and w)
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        calculateSAP(v, w);
        return shortestPath;
    }

    /**
     * Return a common ancestor that participates in shortest ancestral path; -1 if no such path
     *
     * @param v
     * @param w
     * @return common ancestor Id of Iterable v and w
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        calculateSAP(v, w);
        return ancestorId;
    }

    private void calculateSAP(int v, int w) {
        if (v < 0 || w < 0 || v >= graph.V() || w >= graph.V()) {
            throw new IllegalArgumentException("Input value int v or int w outside prescribed range!");
        }
        if (v == w) {
            shortestPath = 0;
            ancestorId = v;
            return;
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        shortestPath = -1;
        ancestorId = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (shortestPath == -1) {
                    shortestPath = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestorId = i;
                } else if (shortestPath > bfsV.distTo(i) + bfsW.distTo(i)) {
                    shortestPath = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestorId = i;
                }
            }
        }
    }

    private void calculateSAP(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("Input value is null!");
        }
        for (int a : v) {
            if (a < 0 || a >= graph.V()) {
                throw new IllegalArgumentException("Input value Iterable v outside prescribed range!");
            }
        }
        for (int b : w) {
            if (b < 0 || b >= graph.V()) {
                throw new IllegalArgumentException("Input value Iterable w outside prescribed range!");
            }
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        shortestPath = -1;
        ancestorId = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (shortestPath == -1) {
                    shortestPath = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestorId = i;
                } else if (shortestPath > bfsV.distTo(i) + bfsW.distTo(i)) {
                    shortestPath = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestorId = i;
                }
            }
        }
    }

    // for unit testing of this class
    /*
    public static void main(String[] args){
        In in = new In(args[0]);
        Digraph graph = new Digraph(in);
        SAP sap = new SAP(graph);
        while(!StdIn.isEmpty()){
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length =sap.length(v,w);
            int anscestor = sap.ancestor(v,w);
            StdOut.printf("length = %d, anscestor = %d\n", length, anscestor );
        }

        Bag<Integer> listV = new Bag<Integer>();
        Bag<Integer> listW = new Bag<Integer>();

        listV.add(13); listV.add(23);listV.add(24);
        listW.add(6); listW.add(16);listW.add(17);
        StdOut.println("length is " + sap.length(listV, listW));
        StdOut.println("ancestor is " + sap.ancestor(listV, listW));
}
*/

}


