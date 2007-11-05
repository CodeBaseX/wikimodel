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
     * Method
     * {@link #shiftNodeItem(INodeWalkerSource, INodeWalkerContext, INodeWalkerListener)}
     * travers all graph structure and returns control only when all nodes are
     * visited.
     */
    public final static int AUTO = 0;

    /**
     * Deep first traversing a tree. Equals to {@link #FLAG_NO_CHILD}|
     * {@link #FLAG_NO_NEXT};
     */
    public final static int DEEP_FIRST = 4 | 1;

    /**
     * Stops after each step: after entering into the node and after exiting
     * from it. Equals to {@link #IN}|{@link #OUT}
     */
    public final static int EACH = 1 | 2 | 4 | 8;

    /**
     * Method
     * {@link #shiftNodeItem(INodeWalkerSource, INodeWalkerContext, INodeWalkerListener)}
     * returns control if the last visited node has at least one child node.
     */
    public final static int FLAG_HAS_CHILDREN = 8;

    /**
     * Method
     * {@link #shiftNodeItem(INodeWalkerSource, INodeWalkerContext, INodeWalkerListener)}
     * returns control if the last visited node has at least one non visited
     * sibling on the same level.
     */
    public final static int FLAG_HAS_NEXT = 2;

    /**
     * Method
     * {@link #shiftNodeItem(INodeWalkerSource, INodeWalkerContext, INodeWalkerListener)}
     * returns control if the last visited node has no child.
     */
    public final static int FLAG_NO_CHILD = 4;

    /**
     * Method
     * {@link #shiftNodeItem(INodeWalkerSource, INodeWalkerContext, INodeWalkerListener)}
     * returns control if the last visited had no more non visited sibling on
     * the same level.
     */
    public final static int FLAG_NO_NEXT = 1;

    /**
     * Stop after entering into the node. Equals to {@link #FLAG_HAS_CHILDREN}|
     * {@link #FLAG_NO_CHILD}
     */
    public final static int IN = 8 | 4;

    /**
     * Traversing the leaf nodes only. Equals to {@link #FLAG_NO_CHILD}
     */
    public final static int LEAF = 4;

    /**
     * Stop after exiting from the current node. Equals to
     * {@link #FLAG_HAS_NEXT}|{@link #FLAG_NO_NEXT}.
     */
    public final static int OUT = 2 | 1;

    /**
     * The next node to add to the stack; it is the next visited node
     */
    protected Object fNextNode;

    /**
     * The source of nodes in the graph
     */
    protected INodeWalkerSource fSource;

    /**
     * The current status of the navigation
     */
    protected int fStatus;

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

    protected abstract Object getPeekNode();

    protected abstract Object popNode();

    protected abstract void pushNode(Object currentNode);

    /**
     * Resets the internal state of the walker; sets the given object as the
     * root object of the tree
     * 
     * @param root
     */
    public void reset(Object root) {
        fStatus = 0;
        fNextNode = root;
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
     * @return <code>true</code> if one more step was successfully done
     * @throws Exception
     */
    public boolean shift(INodeWalkerListener nodeListener) throws Exception {
        Object parentNode = getPeekNode();
        if (parentNode == null && fNextNode == null) {
            fNextNode = fSource.getFirstSubnode(null);
            if (fNextNode == null)
                return false;
        }

        if ((fStatus & FLAG_HAS_CHILDREN) != 0 && parentNode != null) {
            nodeListener.beginChildNodes(parentNode);
        }

        if (fNextNode != null) {
            boolean visit = nodeListener.beginNode(parentNode, fNextNode);
            pushNode(fNextNode);
            if (visit) {
                fNextNode = fSource.getFirstSubnode(fNextNode);
            } else
                fNextNode = null;
            fStatus = (fNextNode != null) ? FLAG_HAS_CHILDREN : FLAG_NO_CHILD;
        } else {
            fNextNode = popNode();
            parentNode = getPeekNode();
            nodeListener.endNode(parentNode, fNextNode);
            fNextNode = fSource.getNextSubnode(parentNode, fNextNode);
            fStatus = (fNextNode != null) ? FLAG_HAS_NEXT : FLAG_NO_NEXT;
        }

        if ((fStatus & FLAG_NO_NEXT) != 0 && parentNode != null) {
            nodeListener.endChildNodes(parentNode);
        }

        return (fNextNode != null || parentNode != null);
    }

    /**
     * @param statusMask
     * @param nodeListener
     * @return
     * @throws Exception
     */
    public boolean shift(int statusMask, INodeWalkerListener nodeListener)
        throws Exception {
        boolean result;
        while ((result = shift(nodeListener))) {
            if ((statusMask & fStatus) != 0) {
                break;
            }
        }
        return result;
    }
}