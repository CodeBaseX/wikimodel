package org.wikimodel.graph;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author kotelnikov
 */
public abstract class IteratorBasedNodeSource<T, E extends Throwable>
    implements
    INodeWalkerSource<T, E> {

    /**
     * This stack contains current node iterators.
     */
    private Stack<Iterator<T>> fIteratorStack = new Stack<Iterator<T>>();

    /**
     * @throws E
     */
    public void close() throws E {
        while (!fIteratorStack.empty()) {
            Iterator<T> iterator = fIteratorStack.pop();
            deleteIterator(iterator);
        }
    }

    /**
     * @param iterator
     */
    protected void deleteIterator(Iterator<T> iterator) throws E {
    }

    public T getFirstSubnode(T node) throws E {
        Iterator<T> iterator = newIterator(node);
        fIteratorStack.push(iterator);
        return getNextIteratorValue();
    }

    /**
     * Returns the next value in the currently active iterator
     * 
     * @return the next value in the currently active iterator
     */
    protected T getNextIteratorValue() {
        Iterator<T> iterator = !fIteratorStack.empty()
            ? fIteratorStack.peek()
            : null;
        return (iterator != null && iterator.hasNext())
            ? iterator.next()
            : null;
    }

    /**
     * @throws E
     * @see org.wikimodel.graph.INodeWalkerSource#getNextSubnode(java.lang.Object,
     *      java.lang.Object)
     */
    public T getNextSubnode(T parent, T node) throws E {
        Iterator<T> iterator = !fIteratorStack.empty()
            ? fIteratorStack.pop()
            : null;
        if (iterator != null)
            deleteIterator(iterator);
        return getNextIteratorValue();
    }

    /**
     * Returns a new iterator over all subnodes of the given node
     * 
     * @param currentNode for this node an iterator over all subnodes will be
     *        returned
     * @return a new iterator over all subnodes of the given node
     */
    protected abstract Iterator<T> newIterator(T currentNode) throws E;

}