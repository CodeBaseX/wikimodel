package org.wikimodel.graph;

/**
 * This methods of this interface are used by node walker
 * 
 * @author mkotelnikov
 */
public interface INodeWalkerSource {

    /**
     * Returns the first subnode of the given node. If the given node is
     * <code>null</code> then this method should return the root of the tree.
     * 
     * @param node for this node the first subnode should be returned
     * @return the first subnode of the specified node
     * @throws Exception - an exception can be rised if there are problems
     */
    Object getFirstSubnode(Object node) throws Exception;

    /**
     * Returns the next sibling of the the given node. .
     * 
     * @param parent the parent node; it can be <code>null</code>
     * @param node for this node the next sibling should be returned
     * @return the next sibling of the the given node.
     * @throws Exception - an exception can be rised if there are problems
     */
    Object getNextSubnode(Object parent, Object node) throws Exception;

}