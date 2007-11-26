package org.wikimodel.graph;

import org.wikimodel.graph.util.NodeWalkerIterator;

/**
 * This class is used to walk over graph structures.
 * 
 * @see INodeWalkerSource
 * @see INodeWalkerListener
 * @author mkotelnikov
 */

public abstract class AbstractNodeWalker<T> {

    /**
     * Possible modes of traversal of a graph; it defines when the iterator
     * returns the control to the caller - after the entering in a node, after
     * exiting from a node, for leaf nodes or for each step.
     * 
     * @see NodeWalkerIterator#getMode()
     */
    public enum Mode {

        /**
         * Returns control for each step - when the walker enters in a node and
         * when it goes out
         */
        EACH,

        /**
         * Control is returned when the walker enters in a node
         */
        IN,

        /**
         * Controls is returned only for leaf nodes (nodes without children)
         */
        LEAF,

        /**
         * Controls is returned when the walker goes out of a node
         */
        OUT
    }

    /**
     * The last visited node has at least one child node to visit.
     */
    public final static int CHILD = 1;

    /**
     * The last visited node has a sibling node at the same level to visit.
     */
    public final static int NEXT = 2;

    /**
     * The last visited node has no child nodes to visit
     */
    public final static int NO_CHILD = 4;

    /**
     * The last visited had no more non visited sibling on the same level.
     */
    public final static int NO_NEXT = 8;

    /**
     * The next node to add to the stack; it is the next visited node
     */
    protected T fNextNode;

    /**
     * The source of nodes in the graph
     */
    protected INodeWalkerSource<T> fSource;

    /**
     * @param source
     */
    public AbstractNodeWalker(INodeWalkerSource<T> source, T topNode) {
        fSource = source;
        fNextNode = topNode;
    }

    /**
     * @return the current active node
     */
    public T getCurrentNode() {
        return getPeekNode();
    }

    /**
     * @return the next node which should be activated
     */
    public T getNextNode() {
        return fNextNode;
    }

    /**
     * Returns the topmost node in the stack withot removing it from the stack
     * throws
     * 
     * @return the topmost node in the stack
     */
    protected abstract T getPeekNode();

    /**
     * Returns <code>true</code> if the walking process is finished and the
     * walker can not make the next step. This method returns <code>true</code>
     * if the both methods {@link #getPeekNode()} and {@link #getNextNode()}
     * return <code>null</code>.
     * 
     * @return <code>true</code> if this walker can not make the next step
     */
    public boolean isFinished() {
        return fNextNode == null && getPeekNode() == null;
    }

    /**
     * Removes the topmost node of the stack and returns it.
     * 
     * @return the removed topmost node of the stack
     */
    protected abstract T popNode();

    /**
     * Pushes the given node in the top of the stack
     * 
     * @param currentNode
     */
    protected abstract void pushNode(T currentNode);

    /**
     * Sets the next node to visit.
     * 
     * @param nextNode the next node to visit
     */
    public void setNextNode(T nextNode) {
        fNextNode = nextNode;
    }

    /**
     * Loads the next node in the graph structure. Each step enters in a node or
     * exit from current active node.
     * 
     * @param source this is a source of graph nodes
     * @param context this context contains dynamic (run-time) information about
     *        the current position of the walker in the graph structure
     * @param nodeListener this listener is used to notify about events occurred
     *        with each node: walker goes in the node or goes out from it, when
     *        walker starts to traverse subnodes of a node or when it finish
     *        subnode traversing.
     * @return <code>true</code> if the walker goes down in the tree branches
     *         and <code>false</code> if it goes out of a node
     */
    public int shift(INodeWalkerListener<T> nodeListener) {
        int status;
        if (fNextNode != null) {
            T node = fNextNode;
            fNextNode = fSource.getFirstSubnode(node);
            T parentNode = getPeekNode();
            if (parentNode != null || node != null) {
                nodeListener.beginNode(parentNode, node);
            }
            pushNode(node);
            status = fNextNode != null ? CHILD : NO_CHILD;
        } else {
            T node = popNode();
            T parentNode = getPeekNode();
            fNextNode = fSource.getNextSubnode(parentNode, node);
            if (parentNode != null || node != null) {
                nodeListener.endNode(parentNode, node);
            }
            status = fNextNode != null ? NEXT : NO_NEXT;
        }
        return status;
    }

    /**
     * @param listener this listener is used to notify about events occurred
     *        with each node: walker goes in the node or goes out from it, when
     *        walker starts to traverse subnodes of a node or when it finish
     *        subnode traversing.
     * @param mask this mask defines when the walker returns the control to the
     *        caller; if the status returned by the method
     *        {@link #shift(INodeWalkerListener)} and this mask has common bits
     *        then the control will be returned to the caller
     * @return <code>true</code> if
     */
    public boolean shift(INodeWalkerListener<T> listener, int mask) {
        int status = 0;
        boolean ok = !isFinished();
        while ((status & mask) == 0 && ok) {
            status = shift(listener);
            ok = !isFinished();
        }
        return ok;
    }

    /**
     * @param listener this listener is used to notify about events occurred
     *        with each node: walker goes in the node or goes out from it, when
     *        walker starts to traverse subnodes of a node or when it finish
     *        subnode traversing.
     * @param mode a walking mode
     * @return <code>true</code> if the there is at least one step to do
     */
    public boolean shift(INodeWalkerListener<T> listener, Mode mode) {
        int mask = 0;
        if (mode == Mode.IN) {
            mask = AbstractNodeWalker.CHILD | AbstractNodeWalker.NO_CHILD;
        } else if (mode == Mode.OUT) {
            mask = AbstractNodeWalker.NO_CHILD | AbstractNodeWalker.NO_NEXT;
        } else if (mode == Mode.LEAF) {
            mask = AbstractNodeWalker.NO_CHILD;
        } else if (mode == Mode.EACH) {
            mask = AbstractNodeWalker.CHILD
                | AbstractNodeWalker.NO_CHILD
                | AbstractNodeWalker.NEXT
                | AbstractNodeWalker.NO_NEXT;
        }
        return shift(listener, mask);
    }

}