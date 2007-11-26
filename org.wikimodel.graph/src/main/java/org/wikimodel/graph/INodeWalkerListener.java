package org.wikimodel.graph;

/**
 * Methods of this listener are used to notify about different stages of tree
 * walking. It contains methods which are called before and after visiting of
 * each node.
 * 
 * @author kotelnikov
 */
public interface INodeWalkerListener<T> {

    /**
     * This method is launched before visiting <code>node</code> object. When
     * this method is called the given node not yet in the walker's stack. If
     * this method returns <code>true</code> then sub-nodes of this node also
     * will be visited.
     * 
     * @param parent the parent node
     * @param node a new node to initialize
     */
    void beginNode(T parent, T node);

    /**
     * This method is launched after visiting the given node. This method can
     * destroy all resources associated with this node. When this method is
     * called the given node does not in the stack anymore
     * 
     * @param context node walker context
     * @param node a node to destroy.
     */
    void endNode(T parent, T node);

}