import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MoveToFront {
    private static final int R = 256; // extended ASCII

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        List<Character> moveToFront = createASCIIList();
        while (!BinaryStdIn.isEmpty()) {
            char input = BinaryStdIn.readChar();
            int position = 0;
            Iterator<Character> moveToFrontIterator = moveToFront.iterator();
            while (moveToFrontIterator.hasNext()) {
                if (moveToFrontIterator.next() == input) {
                    BinaryStdOut.write((char) position, 8);
                    //StdOut.printf("%02x", (char)position&0xff); // display the binary output to Hex
                    char toFront = moveToFront.get(position);
                    moveToFront.remove(position);
                    moveToFront.add(0, toFront);
                    //moveToFront.add(0,moveToFront.get(position));
                    //moveToFront.remove(position+1);  //already add one position
                    break;
                }
                position++;
                // System.out.println(position);
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        List<Character> moveToFront = createASCIIList();
        while (!BinaryStdIn.isEmpty()) {
            int position = BinaryStdIn.readChar();
            BinaryStdOut.write(moveToFront.get(position), 8);
            char toFront = moveToFront.get(position);
            moveToFront.remove(position);
            moveToFront.add(0, toFront);
        }
        BinaryStdOut.close();
    }


    private static List<Character> createASCIIList() {
        List<Character> list = new LinkedList<>();
        for (int i = 0; i < R; i++) {
            list.add((char) i);
        }
        return list;
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("usage: input '-' for encoding or '+' for decoding");
        }
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException("usage: input '-' for encoding or '+' for decoding");
        }
    }

}
