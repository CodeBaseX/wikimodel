/**
 * 
 */
package org.wikimodel.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kotelnikov
 */
public class NodeWalker extends AbstractNodeWalker {

    protected List fStack = new ArrayList();

    public NodeWalker(INodeWalkerSource source, Object topNode) {
        super(source, topNode);
    }

    /**
     * @see org.wikimodel.graph.AbstractNodeWalker#getPeekNode()
     */
    public Object getPeekNode() {
        return !fStack.isEmpty() ? fStack.get(fStack.size() - 1) : null;
    }

    /**
     * @see org.wikimodel.graph.AbstractNodeWalker#popNode()
     */
    protected Object popNode() {
        return !fStack.isEmpty() ? fStack.remove(fStack.size() - 1) : null;
    }

    /**
     * @see org.wikimodel.graph.AbstractNodeWalker#pushNode(java.lang.Object)
     */
    protected void pushNode(Object currentNode) {
        fStack.add(currentNode);
    }

    /**
     * Resets the stack and sets the given node as the next node to visit
     * 
     * @param root a new root node of the tree to visit
     */
    public void reset(Object root) {
        super.setNextNode(root);
        fStack.clear();
    }

}
