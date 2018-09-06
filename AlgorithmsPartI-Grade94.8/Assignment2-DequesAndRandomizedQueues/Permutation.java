import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        /*while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                rq.enqueue(item);
            else if (!rq.isEmpty())
                StdOut.print(rq.dequeue() + " ");
        }
        */

// test randomized iterator
        rq.enqueue("A");
        rq.enqueue("B");
        rq.enqueue("C");
        rq.enqueue("D");
        rq.enqueue("E");
        rq.enqueue("F");
        rq.enqueue("G");
        rq.enqueue("H");
        // test



        int k = Integer.parseInt(args[0]);
        Iterator<String> it = rq.iterator();
        int i = 0;
        while (it.hasNext() && i < k) {
            StdOut.println(it.next());
            i++;
        }


    }
}