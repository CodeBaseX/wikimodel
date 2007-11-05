/**
 * 
 */
package org.wikimodel.graph;

/**
 * @author kotelnikov
 */
public abstract class NodeWalkerListener1 implements INodeWalkerListener {

    private boolean fIn;

    private AbstractNodeWalker fWalker;

    /**
     * 
     */
    public NodeWalkerListener1(AbstractNodeWalker walker) {
        fIn = false;
        fWalker = walker;
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#beginNode(java.lang.Object,
     *      java.lang.Object)
     */
    public final void beginNode(Object parent, Object node) throws Exception {
        if (fIn && parent != null) {
            onBeginSubnodes(parent);
        }
        fIn = true;
        onBeginNode(parent, node);
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#endNode(java.lang.Object,
     *      java.lang.Object)
     */
    public final void endNode(Object parent, Object node) throws Exception {
        // if (!fIn && parent != null) {
        // onEndSubnodes(parent);
        // }
        fIn = false;
        onEndNode(parent, node);
        if (fWalker.getNextNode() == null && parent != null) {
            onEndSubnodes(parent);
        }
    }

    protected abstract void onBeginNode(Object parent, Object node)
        throws Exception;

    protected abstract void onBeginSubnodes(Object parent) throws Exception;

    protected abstract void onEndNode(Object parent, Object node)
        throws Exception;

    protected abstract void onEndSubnodes(Object parent) throws Exception;

}
