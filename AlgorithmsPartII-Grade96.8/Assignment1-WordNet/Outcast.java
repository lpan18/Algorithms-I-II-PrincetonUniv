
public class Outcast {
    private final WordNet wordnet;

    /**
     * Constructor takes a WordNet object
     *
     * @param wordnet
     */
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    /**
     * Given an array of WordNet nouns, return an outcast of WordNet
     *
     * @param nouns
     * @return an outcast of WordNet
     */
    public String outcast(String[] nouns) {
        int maxDistance = -1;
        String maxNoun = null;
        for (String noun : nouns) {
            if (!wordnet.isNoun(noun)) {
                throw new IllegalArgumentException("Input is not WordNet nouns!");
            }
        }
        for (int i = 0; i < nouns.length; i++) {
            int dist = 0;
            for (int j = 0; j < nouns.length; j++) {
                dist += wordnet.distance(nouns[i], nouns[j]);
            }
            if (maxDistance < dist) {
                maxDistance = dist;
                maxNoun = nouns[i];
            }
        }
        return maxNoun;
    }

    // for unit testing of this class
    /*
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
    */
}