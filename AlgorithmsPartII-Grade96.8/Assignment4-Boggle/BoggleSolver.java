import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private TrieSET dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("dictionary is null!");
        }
        this.dict = new TrieSET();
        for (String word : dictionary) {
            this.dict.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("board is null!");
        }
        Set<String> validWords = new HashSet<String>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                boolean[][] marked = new boolean[board.rows()][board.cols()];
                collect(board, i, j, marked, "", validWords);
            }
        }
        return validWords;
    }

    // Get all neighbour characters to form the word and check if in the dictionary
    private void collect(BoggleBoard board, int row, int col, boolean[][] marked, String prefix, Set<String> set) {
        if (marked[row][col]) {
            return;
        }
        char letter = board.getLetter(row, col);
        String word = prefix;
        if (letter == 'Q') {
            word += "QU";
        } else {
            word += letter;
        }

        if (!dict.hasPrefix(word)) {
            return;
        }

        if (word.length() > 2 && dict.contains(word)) {
            set.add(word);
        }
        marked[row][col] = true;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (row + i >= 0 && row + i < board.rows() && col + j >= 0 && col + j < board.cols()) {
                    collect(board, row + i, col + j, marked, word, set);
                }
            }
        }
        marked[row][col] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word is null!");
        }
        if (dict.contains(word)) {
            switch (word.length()) {
                case 0:
                case 1:
                case 2:
                    return 0;
                case 3:
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
        } else {
            return 0;
        }
    }

    private class TrieSET {
        private static final int R = 26; // A-Z letters
        private static final int OFFSET = 65; // Offset of letter A in ASCII table
        private Node root;      // root of trie
        private int n;          // number of keys in trie

        private class Node {
            private Node[] next = new Node[R]; // 26 letters A through Z
            private boolean isString;
        }

        public boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }

        public Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c - OFFSET], key, d + 1);
        }

        public void add(String key) {
            if (key == null) throw new IllegalArgumentException("argument to add() is null");
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (!x.isString) n++;
                x.isString = true;
            } else {
                char c = key.charAt(d);
                x.next[c - OFFSET] = add(x.next[c - OFFSET], key, d + 1);
            }
            return x;
        }

        public boolean hasPrefix(String prefix) {
            return get(root, prefix, 0) != null;
        }
    }
}
