/**
 * 
 */
package org.wikimodel.graph.util;

import org.wikimodel.graph.AbstractNodeWalker;
import org.wikimodel.graph.INodeWalkerListener;
import org.wikimodel.graph.INodeWalkerSource;
import org.wikimodel.graph.NodeWalker;
import org.wikimodel.graph.NodeWalkerListener;
import org.wikimodel.graph.AbstractNodeWalker.Mode;
import org.wikimodel.iterator.ShiftIterator;

/**
 * An implementation of an iterator over graph structure.
 * 
 * @author kotelnikov
 */
public class NodeWalkerIterator<T, E extends RuntimeException>
    extends
    ShiftIterator<T> {

    /**
     * The listener which is used to notify about individual steps of the
     * walking process.
     */
    private INodeWalkerListener<T, E> fListener;

    /**
     * A node walker containing the current stack of nodes
     */
    private AbstractNodeWalker<T, E> fWalker;

    /**
     * THe main constructor creating an internal node walker and initializing
     * object fields
     * 
     * @param source the source of sub-nodes in the graph
     * @param listener the listener used to notify about individual steps of the
     *        graph traversal process
     * @param topNode the topmost node of the graph; starting from this node the
     *        graph iteration is started Exception,
     */
    public NodeWalkerIterator(
        INodeWalkerSource<T, E> source,
        INodeWalkerListener<T, E> listener,
        T topNode) {
        fWalker = newNodeWalker(source, topNode);
        fListener = listener != null
            ? listener
            : new NodeWalkerListener<T, E>();
    }

    /**
     * This constructor initializes the object with a default (empty/do-nothing)
     * listener.
     * 
     * @param source the source of sub-nodes for all elements in the graph
     * @param topNode the topmost node of the graph from which the iteration
     *        process should be started
     */
    public NodeWalkerIterator(INodeWalkerSource<T, E> source, T topNode) {
        this(source, null, topNode);
    }

    /**
     * Defines the traversal mode for this iterator. The mode defines when the
     * control it returned to the iterator caller - before entering in a node,
     * after exiting from a node, for leaf nodes only, or for each step
     * (entering and exiting from a node). By default this method returns
     * {@link Mode#IN} value, which means that the control is returned after
     * entering in graph nodes. This method can be overloaded in subclasses.
     * 
     * @param mode the graph traversal mode
     */
    protected AbstractNodeWalker.Mode getMode() {
        return AbstractNodeWalker.Mode.IN;
    }

    /**
     * Returns the internal node walker
     * 
     * @return the internal node walker
     */
    public AbstractNodeWalker<T, E> getWalker() {
        return fWalker;
    }

    /**
     * Creates and returns a new node walker used by this iterator to keep the
     * state of the iteration process - it keeps current position in the graph
     * and the currently active node. This method by default creates an instance
     * of a simple {@link java.util.Stack stack}-based implementation of the
     * NodeWalker. This method can be overloaded in subclasses to create an
     * another implementation of a node walker.
     * 
     * @param source the source of sub-nodes for all elements in the graph
     * @param topNode the topmost node of the graph from which the iteration
     *        process should be started
     * @return a new node walker defining the current position of the iterator
     *         in the graph structure
     */
    protected AbstractNodeWalker<T, E> newNodeWalker(
        INodeWalkerSource<T, E> source,
        T topNode) {
        return new NodeWalker<T, E>(source, topNode);
    }

    /**
     * @see org.wikimodel.iterator.ShiftIterator#shiftItem()
     */
    @Override
    protected T shiftItem() {
        Mode mode = getMode();
        fWalker.shift(fListener, mode);
        return fWalker.getCurrentNode();
    }
}
