package entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MessageBox implements Iterable<Message>{
    private final ArrayList<Message> messages;
    private String owner;
    private Integer currentSize;

    /**
     * Creates a new MessageBox for the designated owner
     * @param owner The owner of the MessageBox
     */
    public MessageBox(String owner) {
        this.messages = new ArrayList<>();
        this.currentSize = 0;
        this.owner = owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    // Might not be needed
    public List<Message> getMessages() {
        return messages;
    }

    public String getOwner() {
        return owner;
    }

    public void receiveMessage(Message message) {
        messages.add(message);
        currentSize++;
    }
    // don't know if this needs to be in this class
    public Message findMessage(Message message){
        for(Message m : messageBox){
            if(m == message){
                return m;
            }
        }
        return null;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Message> iterator() {
        Iterator<Message> msgIterator = new Iterator<Message>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < currentSize && messages.get(currentIndex) != null;
            }

            @Override
            public Message next() {
                currentIndex++;
                return messages.get(currentIndex);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return msgIterator;
    }

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @implSpec <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     * @since 1.8
     */
    @Override
    public void forEach(Consumer<? super Message> action) {
        Iterable.super.forEach(action);
    }

    /**
     * Creates a {@link Spliterator} over the elements described by this
     * {@code Iterable}.
     *
     * @return a {@code Spliterator} over the elements described by this
     * {@code Iterable}.
     * @implSpec The default implementation creates an
     * <em><a href="Spliterator.html#binding">early-binding</a></em>
     * spliterator from the iterable's {@code Iterator}.  The spliterator
     * inherits the <em>fail-fast</em> properties of the iterable's iterator.
     * @implNote The default implementation should usually be overridden.  The
     * spliterator returned by the default implementation has poor splitting
     * capabilities, is unsized, and does not report any spliterator
     * characteristics. Implementing classes can nearly always provide a
     * better implementation.
     * @since 1.8
     */
    @Override
    public Spliterator<Message> spliterator() {
        return Iterable.super.spliterator();
    }
}
