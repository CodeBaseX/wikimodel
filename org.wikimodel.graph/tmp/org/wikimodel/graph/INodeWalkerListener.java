package org.wikimodel.graph;

/**
 * Methods of this listener are used to notify about different stages of tree
 * walking. It contains methods which are called before and after visiting of
 * each node. The methods {@link #beginChildNodes(Object)} and
 * {@link #endChildNodes(Object)} are called for each visiting node only if it
 * has children.
 * 
 * @author kotelnikov
 */
public interface INodeWalkerListener {

    /**
     * This method is launched before visiting child nodes of the given node .
     * This method is not executed if method {@link #beginNode(Object, Object)}
     * returns <code>false</code>.
     * 
     * @param node the visited node
     * @throws Exception
     */
    void beginChildNodes(Object node) throws Exception;

    /**
     * This method is launched before visiting <code>node</code> object. When
     * this method is called the given node not yet in the walker's stack. If
     * this method returns <code>true</code> then sub-nodes of this node also
     * will be visited.
     * 
     * @param parent the parent node
     * @param node a new node to initialize
     * @return <code>true</code> if the given node will be visited.
     * @throws Exception
     */
    boolean beginNode(Object parent, Object node) throws Exception;

    /**
     * This method is launched when all sub-nodes of the given node are visited
     * already. This method is not executed if method
     * {@link #beginNode(Object, Object)} returns <code>false</code>.
     * 
     * @param node a new node to deactivate
     * @throws Exception
     */
    void endChildNodes(Object node) throws Exception;

    /**
     * This method is launched after visiting the given node. This method can
     * destroy all resources associated with this node. When this method is
     * called the given node does not in the stack anymore
     * 
     * @param context node walker context
     * @param node a node to destroy.
     * @throws Exception
     */
    void endNode(Object parent, Object node) throws Exception;

}