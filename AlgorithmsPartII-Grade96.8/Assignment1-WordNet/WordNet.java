import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdIn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Lei Pan
 */

public class WordNet {
    private final Map<Integer, String> idsToSynsets;
    private final Map<String, Set<Integer>> nounsToIds;
    private final SAP sap;

    /**
     * constructor takes the name of the two input files
     *
     * @param synsets   all noun synsets
     * @param hypernyms hypernym relationships
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Input string is null!");
        }
        idsToSynsets = new HashMap<Integer, String>();
        nounsToIds = new HashMap<String, Set<Integer>>();
        initSynsets(synsets);
        Digraph graph = initHypernyms(hypernyms);
        //System.out.println(graph.V());
        //System.out.println(graph.E());
        if (!rootedDAG(graph)) {
            throw new IllegalArgumentException("The WordNet digraph is not a rooted DAG!");
        }
        sap = new SAP(graph);
    }

    private void initSynsets(String synsets) {
        if (synsets == null) {
            throw new IllegalArgumentException();
        }
        In file = new In(synsets);
        while (file.hasNextLine()) {
            String[] line = file.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String idString = line[1];
            idsToSynsets.put(id, idString);

            // separate nouns from idString and put nouns that appear more than once
            String[] nouns = idString.split(" ");
            for (String noun : nouns) {
                Set<Integer> ids = nounsToIds.get(noun);
                if (ids == null) {
                    ids = new HashSet<Integer>();
                }
                ids.add(id);
                nounsToIds.put(noun, ids);
            }
        }
    }

    private Digraph initHypernyms(String hypernyms) {
        Digraph graph = new Digraph(idsToSynsets.size());
        if (hypernyms == null) {
            throw new IllegalArgumentException();
        }
        In file = new In(hypernyms);
        while (file.hasNextLine()) {
            String[] line = file.readLine().split(",");
            int v = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int w = Integer.parseInt(line[i]);
                graph.addEdge(v, w);
            }
        }
        return graph;
    }

    private boolean rootedDAG(Digraph graph) {
        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle()) {
            return false;
        }
        // detect if a DAG is rooted
        int roots = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (!graph.adj(i).iterator().hasNext()) {
                roots++;
                if (roots > 1) {
                    return false;
                }
            }
        }
        return roots == 1;
    }

    /**
     * @return all WordNet nouns in a set, returned as an Iterable
     */

    public Iterable<String> nouns() {
        return nounsToIds.keySet();
    }

    /**
     * @param word
     * @return is the word a WordNet noun?
     */
    public boolean isNoun(String word) {
        if (word == null || word.equals("")) {
            throw new IllegalArgumentException("Input string is null!");
        }
        return nounsToIds.containsKey(word);
    }

    /**
     * The distance between nounA and nounB. The distance is defined as following:
     * distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.
     *
     * @param nounA
     * @param nounB
     * @return distance(A, B)
     */
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Input string is not WordNet noun!");
        }
        return sap.length(nounsToIds.get(nounA), nounsToIds.get(nounB));
    }

    /**
     * An ancestral path between two vertices v and w in a digraph is a directed path from v to a common ancestor x, together with a directed path
     * from w to the same ancestor x. A shortest ancestral path is an ancestral path of minimum total length.
     *
     * @param nounA
     * @param nounB
     * @return the common ancestor of nounA and nounB in a shortest ancestral path
     */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Input string is not WordNet noun!");
        }
        int ancestorId = sap.ancestor(nounsToIds.get(nounA), nounsToIds.get(nounB));
        return idsToSynsets.get(ancestorId);
    }

    // for unit testing of this class
    /*
    public static void main(String[] args) {
       // WordNet test = new WordNet(args[0], args[1]);
        int counts = 0;
        for (String noun : test.nouns()) {
            counts++;
        }
        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readString();
            String nounB = StdIn.readString();
            if (!test.isNoun(nounA)) {
                System.out.println(nounA + " is not in the WordNet");
            }
            if (!test.isNoun(nounB)) {
                System.out.println(nounB + " is not in the WordNet");
            }
            System.out.println("distance between " + nounA + " and " + nounB + " is " + test.distance(nounA, nounB));
            System.out.println("ancestor SAP of " + nounA + " and " + nounB + " is " + test.sap(nounA, nounB));

            System.out.println("size of nouns " + counts);
        }

    }
    */
}
