package org.wikimodel.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to recreate a tree structure by individual paths. Example
 * of usage:
 * 
 * <pre>
 * 
 * TreeBuilder builder = new TreeBuilder();
 *
 * // This method should give a list with individual 
 * // elements of the path 
 * List path = getPath("a/b/c"); 
 * 
 * // This listener will be notified about nodes
 * INodeWalkerListener listener = ... ;  
 * // Notifies about nodes of the graph corresponding 
 * // to the path "a/b/c" 
 * builder.align(path, listener);
 * 
 * path = getPath("a/x/y");//
 * // Notifies about new nodes "x" and "y"
 * builder.align(path, listener);
 * 
 * // Finalizes the tree building
 * path.clear();
 * builder.align(path, listener);
 * </pre>
 * 
 * @author kotelnikov
 */
public class TreeBuilder extends NodeWalker implements INodeWalkerSource {

    /**
     * The path used as a source of nodes
     */
    private List fPath = new ArrayList();

    private int fPathPos;

    private int fStackPos;

    /**
     * The common constructor used to initialize internal fields
     */
    public TreeBuilder() {
        super(null, null);
        fSource = this;
    }

    public void align(List path, INodeWalkerListener listener) throws Exception {
        fPath.clear();
        fPath.addAll(path);
        int pathSize = fPath.size();
        int stackSize = fStack.size();
        int stackShift = 0;
        int pathShift = 0;
        fStackPos = 0;
        fPathPos = 0;
        while (fStackPos < stackSize && fPathPos < pathSize) {
            Object stackSegment = fStack.get(fStackPos);
            Object pathSegment = fPath.get(fPathPos);
            stackShift += getLength(stackSegment);
            pathShift += getLength(pathSegment);
            if (stackShift >= pathShift) {
                if (!equal(stackSegment, pathSegment))
                    break;
                if (fPathPos >= pathSize - 1)
                    break;
                fPathPos++;
            }
            fStackPos++;
        }
        fNextNode = shift();
        while (!isFinished()) {
            shift(listener);
            if (fNextNode == null && pathSize > 0 && fPathPos >= pathSize) {
                break;
            }
        }
    }

    protected boolean equal(Object first, Object second) {
        return first == null || second == null ? first == second : first
            .equals(second);
    }

    public Object getFirstSubnode(Object node) throws Exception {
        return shift();
    }

    protected int getLength(Object node) {
        return 1;
    }

    public Object getNextSubnode(Object parent, Object node) throws Exception {
        return shift();
    }

    private Object shift() {
        Object result = null;
        if (fStackPos < fStack.size())
            return null;
        if (fPathPos < fPath.size()) {
            result = fPath.get(fPathPos);
            fPathPos++;
            fStackPos++;
        }
        return result;
    }

}