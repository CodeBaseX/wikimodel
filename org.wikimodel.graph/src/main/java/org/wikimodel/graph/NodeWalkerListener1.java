/**
 * 
 */
package org.wikimodel.graph;

/**
 * @author kotelnikov
 */
public abstract class NodeWalkerListener1<T> implements INodeWalkerListener<T> {

    private boolean fIn;

    private AbstractNodeWalker<T> fWalker;

    /**
     * 
     */
    public NodeWalkerListener1(AbstractNodeWalker<T> walker) {
        fIn = false;
        fWalker = walker;
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#beginNode(java.lang.Object,
     *      java.lang.Object)
     */
    public final void beginNode(T parent, T node) {
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
    public final void endNode(T parent, T node) {
        // if (!fIn && parent != null) {
        // onEndSubnodes(parent);
        // }
        fIn = false;
        onEndNode(parent, node);
        if (fWalker.getNextNode() == null && parent != null) {
            onEndSubnodes(parent);
        }
    }

    protected abstract void onBeginNode(T parent, T node);

    protected abstract void onBeginSubnodes(T parent);

    protected abstract void onEndNode(T parent, T node);

    protected abstract void onEndSubnodes(T parent);

}
