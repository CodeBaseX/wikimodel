package org.wikimodel.graph;

/**
 * This methods of this interface are used by node walker
 * 
 * @author mkotelnikov
 */
public interface INodeWalkerSource<T> {

    /**
     * Returns the first subnode of the given node. If the given node is
     * <code>null</code> then this method should return the root of the tree.
     * 
     * @param node for this node the first subnode should be returned
     * @return the first subnode of the specified node
     */
    T getFirstSubnode(T node);

    /**
     * Returns the next sibling of the the given node. .
     * 
     * @param parent the parent node; it can be <code>null</code>
     * @param node for this node the next sibling should be returned
     * @return the next sibling of the the given node.
     */
    T getNextSubnode(T parent, T node);

}