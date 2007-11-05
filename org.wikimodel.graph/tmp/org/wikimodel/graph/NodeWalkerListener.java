/**
 * 
 */
package org.wikimodel.graph;

/**
 * @author kotelnikov
 */
public class NodeWalkerListener implements INodeWalkerListener {

    /**
     * 
     */
    public NodeWalkerListener() {
        super();
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#beginChildNodes(java.lang.Object)
     */
    public void beginChildNodes(Object node) throws Exception {
        //
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#beginNode(java.lang.Object,
     *      java.lang.Object)
     */
    public boolean beginNode(Object parent, Object node) throws Exception {
        return true;
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#endChildNodes(java.lang.Object)
     */
    public void endChildNodes(Object node) throws Exception {
        // 
    }

    /**
     * @see org.wikimodel.graph.INodeWalkerListener#endNode(java.lang.Object,
     *      java.lang.Object)
     */
    public void endNode(Object parent, Object node) throws Exception {
        // 
    }

}
