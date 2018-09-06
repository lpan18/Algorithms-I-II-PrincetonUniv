import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class main {
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        System.out.println(board.toString() );
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            //StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        //String dir = System.getProperty("user.dir");
        //StdOut.println(dir);
    }

}
