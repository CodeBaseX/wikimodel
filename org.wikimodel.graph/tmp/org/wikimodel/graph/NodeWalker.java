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
    protected Object getPeekNode() {
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
     * @see org.wikimodel.graph.AbstractNodeWalker#reset(java.lang.Object)
     */
    public void reset(Object root) {
        super.reset(root);
        fStack.clear();
    }

}
