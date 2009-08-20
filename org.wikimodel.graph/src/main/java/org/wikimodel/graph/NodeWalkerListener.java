/**
 * 
 */
package org.wikimodel.graph;

/**
 * @author kotelnikov
 */
public class NodeWalkerListener<T, E extends Throwable>
    implements
    INodeWalkerListener<T, E> {

    /**
     * 
     */
    public NodeWalkerListener() {
        super();
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#beginNode(java.lang.Object,
     *      java.lang.Object)
     */
    public boolean beginNode(T parent, T node) throws E {
        return true;
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#endNode(java.lang.Object,
     *      java.lang.Object)
     */
    public void endNode(T parent, T node) throws E {
        // 
    }

}
