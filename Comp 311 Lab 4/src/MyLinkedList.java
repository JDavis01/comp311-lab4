import java.util.AbstractList;
import java.util.ListIterator;

/**
 *
 * @author jacob davis
 * @version 7/28/19
 *
 * @param <E> generic type
 * list of links with an iterator
 */
public class MyLinkedList<E> extends AbstractList<E> {
    private Link<E> header;
    private int length;

    /**
     * Constructs a linked list
     */
    public MyLinkedList() {
        this.length = 0;
        this.header = new Link<E>(null, null, null);
        this.header.previous = this.header;
        this.header.next = this.header;
    }

    /**
     * iterates over list
     * @param index where iter starts
     * @return ListIterator<E> an iterator
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return new LinkedListIterator(index);
    }

    /**
     * size of list
     * @return int the length
     */
    @Override
    public int size() {
        return this.length;
    }

    /**
     * gets element from index
     * @param index the position
     * @return E element at index
     */
    @Override
    public E get(int index) {
        ListIterator<E> itr = listIterator(index);
        return itr.next();
    }

    /**
     *
     * @author jacob davis
     * @version 7/28/19
     *
     * @param <E> generic type
     * links that the list is made up of
     */
    private static final class Link<E> {
        private E data;
        private Link<E> previous;
        private Link<E> next;

        /**
         * constructs the link
         * @param element the data
         * @param previous the link before this
         * @param next the link after this
         */
        public Link(E element, Link<E> previous, Link<E> next) {
            this.data = element;
            this.previous = previous;
            this.next = next;
        }

        /**
         * adds link after this
         * @param obj data for new link
         * @return Link<E> the link added
         */
        public Link<E> append(E obj) {
            Link<E> link = new Link<E>(obj, this, this.next);
            this.next.previous = link;
            this.next = link;
            return link;
        }

        /**
         * adds link before this
         * @param obj the data for new link
         * @return Link<E> the link added
         */
        public Link<E> prepend(E obj) {
            Link<E> link = new Link<E>(obj, this.previous, this);
            this.previous.next = link;
            this.previous = link;
            return link;
        }

        /**
         * removes this link and
         * and connects this's previous and
         * next and set this data to null.
         */
        public void remove() {
            this.previous.next = this.next;
            this.next.previous = this.previous;
            this.data = null;
        }
    }

    /**
     *
     * @author jacob davis
     * @version 7/28/19
     * iterator for MyLinkedList
     */
    private class LinkedListIterator implements ListIterator<E> {
        private Link<E> cursor;
        private Link<E> priorCursor;
        private int expectedModCount;
        private int cursorIndex;

        /**
         * constructs an iterator for this list
         * @param position where to start in list
         */
        public LinkedListIterator(int position) {
            synchronizeModCounts();

            if (position < 0 || position > size()) {
                throw new java.lang.IndexOutOfBoundsException(
                        "" + position + " is out of bounds [0..." +
                                size() + "]");
            }

            if (position <= size() / 2) {
                this.cursor = MyLinkedList.this.header.next;
                for (int i = 0; i < position; i++) {
                    this.cursor = this.cursor.next;
                }
            }

            else {
                this.cursor = MyLinkedList.this.header;
                for (int i = size(); i > position; i--) {
                    this.cursor = this.cursor.previous;
                }
            }

            this.cursorIndex = position;
            this.priorCursor = null;
        }

        /**
         * adds link at this cursor
         * @param obj the data for new link
         */
        @Override
        public void add(E obj) {
            checkForComodification();
            this.cursor.prepend(obj);
            ++this.cursorIndex;
            ++MyLinkedList.this.length;
            this.priorCursor = null;
            updateAndSynchronizeModCounts();
        }

        /**
         * checks to see if iterator has a next link
         * @return boolean true if another link
         */
        @Override
        public boolean hasNext() {
            return this.cursor != MyLinkedList.this.header;
        }

        /**
         * checks to see if itr has a previous link
         * @return boolean true if another link
         */
        @Override
        public boolean hasPrevious() {
            return this.cursor.previous != MyLinkedList.this.header;
        }

        /**
         * moves the iterator forward
         * @return E the element where the
         * cursor was previously.
         */
        @Override
        public E next() {
            checkForComodification();

            if (!hasNext()) {
                throw new java.util.NoSuchElementException(
                        "Beyond end of list");
            }
            this.priorCursor = this.cursor;
            this.cursor = this.cursor.next;
            ++this.cursorIndex;
            return this.priorCursor.data;
        }

        /**
         * the current index
         * @return int the index returned
         */
        @Override
        public int nextIndex() {
            return this.cursorIndex;
        }

        /**
         * moves iterator backwards
         * @return E the element where
         * the cursor was before.
         */
        @Override
        public E previous() {
            checkForComodification();

            if (!hasPrevious()) {
                throw new java.util.NoSuchElementException(
                        "Beyond beginning of list");
            }
            this.priorCursor = this.cursor;
            this.cursor = this.cursor.previous;
            --this.cursorIndex;
            return this.priorCursor.data;
        }

        /**
         * the index previously
         * @return int the index before
         */
        @Override
        public int previousIndex() {
            return this.cursorIndex - 1;
        }

        /**
         * removes link at current
         * cursor location.
         */
        @Override
        public void remove() {
            checkForComodification();
            checkForValid();
            this.priorCursor.remove();
            this.priorCursor = null;
            --MyLinkedList.this.length;
            updateAndSynchronizeModCounts();
            if (!hasNext()) {
                this.cursor = this.cursor.previous;
            }
        }

        /**
         * sets current link to this one
         * @param obj the data for link
         */
        @Override
        public void set(E obj) {
            this.priorCursor.append(obj);
            this.priorCursor.remove();
            this.priorCursor = null;
        }

        /**
         * Ensures that the expected and actual modification counts between
         * the list and the iterator are in sync with one another.
         * @throws java.util.ConcurrentModificationException when they aren't
         */
        protected void checkForComodification()
        {
            if (this.expectedModCount != MyLinkedList.this.modCount)
            {
                throw new java.util.ConcurrentModificationException();
            }
        }

        /**
         * Re-synchronizes the modification counts between the list
         * and the iterator.
         */
        protected void synchronizeModCounts()
        {
            this.expectedModCount = MyLinkedList.this.modCount;
        }

        /**
         * Updates and then re-synchronizes the modification counts
         * between the list and the iterator.
         */
        protected void updateAndSynchronizeModCounts()
        {
            ++MyLinkedList.this.modCount;
            this.synchronizeModCounts();
        }

        /**
         * Ensures that there was a prior call to either next or previous
         * before performing an operation on the last returned item.
         * @throws IllegalStateException when there was no prior call
         */
        protected void checkForValid()
        {
            if (this.priorCursor == null)
            {
                throw new IllegalStateException(
                    "No prior call to next() or previous()");
            }
        }
    }
}
