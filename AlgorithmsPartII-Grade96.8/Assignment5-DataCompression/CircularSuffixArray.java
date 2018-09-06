import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private final Integer[] index;
    private final String input;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        //if (s == null || s.equals("")) {
        if (s == null){
        throw new IllegalArgumentException("can not get suffix array for null string!");
        }
        input = s;
        index = new Integer[input.length()];
        for (int i = 0; i < input.length(); i++) {
            index[i] = i;
        }
        Arrays.sort(index, new Comparator<Integer>() {
            @Override
            public int compare(Integer indexA, Integer indexB) {
                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(indexA) != input.charAt(indexB)) {
                        return input.charAt(indexA) - input.charAt(indexB);
                    } else {
                        indexA++;
                        indexB++;
                        if (indexA > input.length() - 1) {
                            indexA = 0;
                        }
                        if (indexB > input.length() - 1) {
                            indexB = 0;
                        }
                    }
                }
                return 0;
            }
        });
    }

        /*Arrays.sort(index, new IndexComparator()); }
    private class IndexComparator implements Comparator<Integer> {
        public int compare(Integer indexA, Integer indexB) {
            if (input.charAt(indexA) != input.charAt(indexB)) {
          */

    // length of s
    public int length() {
        return input.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i<0||i > index.length - 1) {
            throw new IllegalArgumentException("input integer out of boundary");
        }
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray test1 = new CircularSuffixArray("AAA");
        for (int i = 0; i < test1.length(); i++) System.out.println(test1.index(i));
        CircularSuffixArray test2 = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < test2.length(); i++) System.out.println(test2.index(i));
    }
}
