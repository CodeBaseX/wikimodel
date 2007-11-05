package org.wikimodel.graph;

/**
 * This class is used to walk over graph structures.
 * 
 * @see INodeWalkerSource
 * @see INodeWalkerListener
 * @author mkotelnikov
 */

public abstract class AbstractNodeWalker {

    /**
     * The next node to add to the stack; it is the next visited node
     */
    protected Object fNextNode;

    /**
     * The source of nodes in the graph
     */
    protected INodeWalkerSource fSource;

    /**
     * @param source
     */
    public AbstractNodeWalker(INodeWalkerSource source, Object topNode) {
        fSource = source;
        fNextNode = topNode;
    }

    /**
     * @return the current active node
     */
    public Object getCurrentNode() {
        return getPeekNode();
    }

    /**
     * @return the next node which should be activated
     */
    public Object getNextNode() {
        return fNextNode;
    }

    /**
     * Returns the topmost node in the stack withot removing it from the stack
     * 
     * @return the topmost node in the stack
     */
    protected abstract Object getPeekNode();

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
    protected abstract Object popNode();

    /**
     * Pushes the given node in the top of the stack
     * 
     * @param currentNode
     */
    protected abstract void pushNode(Object currentNode);

    /**
     * Sets the next node to visit.
     * 
     * @param nextNode the next node to visit
     */
    public void setNextNode(Object nextNode) {
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
     *         and <code>false</code> if it goes one of a node
     * @throws Exception
     */
    public boolean shift(INodeWalkerListener nodeListener) throws Exception {
        boolean result = false;
        if (fNextNode != null) {
            Object node = fNextNode;
            fNextNode = fSource.getFirstSubnode(node);
            Object parentNode = getPeekNode();
            if (parentNode != null || node != null) {
                nodeListener.beginNode(parentNode, node);
            }
            pushNode(node);
            result = true;
        } else {
            Object node = popNode();
            Object parentNode = getPeekNode();
            fNextNode = fSource.getNextSubnode(parentNode, node);
            if (parentNode != null || node != null) {
                nodeListener.endNode(parentNode, node);
            }
        }
        return result;
    }

}