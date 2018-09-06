import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
 *  <i>Binary standard input</i>. This class <em> provides methods for reading
 *  in bits from standard<</em> input, either one bit at a time (as a {@code boolean}),
 *  8 bits at a time (as a {@code byte} or {@code char}),
 *  16 bits at a time (as a {@code short}), 32 bits at a time
 *  (as an {@code int} or {@code float}), or 64 bits at a time (as a
 *  {@code double} or {@code long}).
 *  <p>
 *  All primitive types are assumed to be represented using their
 *  standard Java representations, in big-endian (most significant
 *  byte first) order.
 *  <p>
 *  The client should not intermix calls to {@code BinaryStdIn} with calls
 *  to {@code StdIn} or {@code System.in};
 *  otherwise unexpected behavior will result.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        while (!BinaryStdIn.isEmpty()) {
            String input = BinaryStdIn.readString();
            CircularSuffixArray csa = new CircularSuffixArray(input);
            for (int i = 0; i < csa.length(); i++) {
                if (csa.index(i) == 0) {
                    BinaryStdOut.write(i);
                    break;
                }
            }
            for (int i = 0; i < csa.length(); i++) {
                if (csa.index(i) == 0) {
                    BinaryStdOut.write(input.charAt(input.length() - 1), 8);
                    continue;
                }
                BinaryStdOut.write(input.charAt(csa.index(i) - 1), 8);

            }
            BinaryStdOut.close();
        }
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        while (!BinaryStdIn.isEmpty()) {
            int first = BinaryStdIn.readInt();
            String chars = BinaryStdIn.readString();
            char[] t = chars.toCharArray();
            Map<Character, Queue<Integer>> position = new HashMap<Character, Queue<Integer>>();
            for (int i = 0; i < t.length; i++) {
                if (!position.containsKey(t[i])) {
                    position.put(t[i], new Queue<Integer>());
                }
                position.get(t[i]).enqueue(i);
            }
            Arrays.sort(t);
            int[] next = new int[t.length];
            for (int i = 0; i < t.length; i++) {
                next[i] = position.get(t[i]).dequeue();
            }

            //for (int i = 0,cur = first; i < t.length; i++, cur = next[cur])
            int cur = first;
            for (int i = 0; i < t.length; i++) {
                BinaryStdOut.write(t[cur]);
                cur = next[cur];
            }
            BinaryStdOut.close();

        }
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("usage: input '-' for encoding or '+' for decoding");
        }
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException("usage: input '-' for encoding or '+' for decoding");
        }
    }
}
