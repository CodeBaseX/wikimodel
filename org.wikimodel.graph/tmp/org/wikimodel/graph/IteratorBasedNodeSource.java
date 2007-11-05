package org.wikimodel.graph;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author kotelnikov
 */
public abstract class IteratorBasedNodeSource implements INodeWalkerSource {

    /**
     * This stack contains current node iterators.
     */
    private Stack fIteratorStack = new Stack();

    /**
     * @throws Exception
     */
    public void close() throws Exception {
        while (!fIteratorStack.empty()) {
            Iterator iterator = (Iterator) fIteratorStack.pop();
            deleteIterator(iterator);
        }
    }

    /**
     * @param iterator
     */
    protected void deleteIterator(Iterator iterator) {
    }

    public Object getFirstSubnode(Object node) throws Exception {
        Iterator iterator = newIterator(node);
        fIteratorStack.push(iterator);
        return getNextIteratorValue();
    }

    /**
     * Returns the next value in the currently active iterator
     * 
     * @return the next value in the currently active iterator
     */
    protected Object getNextIteratorValue() {
        Iterator iterator = !fIteratorStack.empty() ? (Iterator) fIteratorStack
            .peek() : null;
        return (iterator != null && iterator.hasNext())
            ? iterator.next()
            : null;
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerSource#getNextSubnode(java.lang.Object,
     *      java.lang.Object)
     */
    public Object getNextSubnode(Object parent, Object node) throws Exception {
        Iterator iterator = !fIteratorStack.empty() ? (Iterator) fIteratorStack
            .pop() : null;
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
     * @throws Exception
     */
    protected abstract Iterator newIterator(Object currentNode)
        throws Exception;

}