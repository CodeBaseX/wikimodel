/**
 * 
 */
package org.wikimodel.graph;

/**
 * @author kotelnikov
 */
public class NodeWalkerListener<T> implements INodeWalkerListener<T> {

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
    public void beginNode(T parent, T node) {
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#endNode(java.lang.Object,
     *      java.lang.Object)
     */
    public void endNode(T parent, T node) {
        // 
    }

}
