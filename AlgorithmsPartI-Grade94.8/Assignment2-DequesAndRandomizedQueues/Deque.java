import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {


    private Node<Item> first;     // top of deque
    private Node<Item> last;     // end of deque
    private int n;                // size of the deque

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    /**
     * Initializes an empty deque.
     */
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    /**
     * Returns true if this deque is empty.
     *
     * @return true if this deque is empty; false otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in this deque.
     *
     * @return the number of items in this deque
     */
    public int size() {
        return n;
    }

    /**
     * Adds the item to the front.
     *
     * @param item the item to add
     * @throws IllegalArgumentException if the item is empty
     */
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Item null");
        Node<Item> oldfirst = first;
        first = new Node<>();
        first.item = item;
        first.next = oldfirst;
        n++;
    }


    /**
     * Adds the item to the end.
     *
     * @param item the item to add
     */
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Item null");
        Node<Item> oldlast = last;
        last = new Node<>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        n++;
    }

    /**
     * Removes and returns the item from the front.
     *
     * @return the item of the front
     * @throws NoSuchElementException if this deque is empty
     */
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = first.item;        // save item to return
        first = first.next;            // delete first node
        n--;
        if (isEmpty()) last = null;   // to avoid loitering
        return item;                   // return the saved item
    }

    /**
     * Removes and returns the item from the end.
     *
     * @return the item of the end
     * @throws NoSuchElementException if this deque is empty
     */
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = last.item;        // save item to return
        last.next = null;            // delete last node
        n--;
        if (isEmpty()) first = null;   // to avoid loitering
        return item;                   // return the saved item
    }


    /**
     * Returns an iterator over items in order from front to end.
     *
     * @return an iterator over items in order from front to end
     */
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}


