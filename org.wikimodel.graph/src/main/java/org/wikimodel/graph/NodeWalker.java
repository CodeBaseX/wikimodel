/**
 * 
 */
package org.wikimodel.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A default sub-class of the {@link AbstractNodeWalker} class which implements
 * the stack of nodes using the standard {@link Stack} java class.
 * 
 * @author kotelnikov
 */
public class NodeWalker<T, E extends Throwable>
    extends
    AbstractNodeWalker<T, E> {

    protected List<T> fStack = new ArrayList<T>();

    public NodeWalker(INodeWalkerSource<T, E> source, T topNode) {
        super(source, topNode);
    }

    /**
     * @see org.wikimodel.graph.AbstractNodeWalker#getPeekNode()
     */
    @Override
    public T getPeekNode() {
        return !fStack.isEmpty() ? fStack.get(fStack.size() - 1) : null;
    }

    /**
     * @see org.wikimodel.graph.AbstractNodeWalker#popNode()
     */
    @Override
    protected T popNode() {
        return !fStack.isEmpty() ? fStack.remove(fStack.size() - 1) : null;
    }

    /**
     * @see org.wikimodel.graph.AbstractNodeWalker#pushNode(java.lang.Object)
     */
    @Override
    protected void pushNode(T currentNode) {
        fStack.add(currentNode);
    }

    /**
     * Resets the stack and sets the given node as the next node to visit
     * 
     * @param root a new root node of the tree to visit
     * @throws E
     */
    public void reset(T root) throws E {
        super.setNextNode(root);
        fStack.clear();
    }

}
