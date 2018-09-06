import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue
    private int n;               // number of elements on queue

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    /**
     * Initializes an empty queue.
     */
    public RandomizedQueue() {
        first = null;
        last = null;
        n = 0;
    }

    /**
     * Returns true if this queue is empty.
     *
     * @return {@code true} if this queue is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in this queue.
     *
     * @return the number of items in this queue
     */
    public int size() {
        return n;
    }


    /**
     * Adds the item to this queue.
     *
     * @param item the item to add
     */
    public void enqueue(Item item) {
        Node<Item> oldlast = last;
        last = new Node<>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        n++;
    }

    /**
     * Removes and returns a random item.
     *
     * @return a random item
     * @throws NoSuchElementException if this queue is empty
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int r = StdRandom.uniform(n);
        Node<Item> pItem = null;
        Node<Item> cItem = first;
        Node<Item> nItem = cItem.next;
        for (int i = 0; i < r; i++) {
            pItem = cItem;
            cItem = cItem.next;
            nItem = cItem.next;
        }
        if (r == 0) {
            first = first.next;
        } else if (r == n - 1) {
            pItem.next = null;
            last = pItem;
        } else {
            pItem.next = nItem;
        }
        n--;
        if (isEmpty()) first = last = null;   // to avoid loitering
        return cItem.item;
    }


    /**
     * Return a random item(but do not remove it).
     *
     * @return a random item(but do not remove it)
     * @throws NoSuchElementException if this queue is empty
     */
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");

        int r = StdRandom.uniform(n);

        Node<Item> cItem = first;
        for (int i = 0; i < r; i++) {
            cItem = cItem.next;
        }
        return cItem.item;

    }


    /**
     * Returns an iterator that iterates over the items in random order.
     *
     * @return an iterator that iterates over the items in random order
     */
    public Iterator<Item> iterator() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return new ListIterator<Item>(first);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Item[] shuffle;
        private int currentIndex;
        //private Node<Item> current;

        public ListIterator(Node<Item> first) {
            shuffle = (Item[]) new Object[n];
            currentIndex = 0;

            Node<Item> cNode = first;

            for (int i = 0; i < n; i++) {
                shuffle[i] = cNode.item;
                cNode = cNode.next;
            }
            StdRandom.shuffle(shuffle);
        }

        public boolean hasNext() {
            return currentIndex < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = shuffle[currentIndex];
            currentIndex++;
            return item;
        }
    }


}
