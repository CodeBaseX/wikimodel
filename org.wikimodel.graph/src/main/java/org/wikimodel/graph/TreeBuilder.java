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
public class TreeBuilder<T, E extends Throwable> extends NodeWalker<T, E>
    implements
    INodeWalkerSource<T, E> {

    /**
     * The path used as a source of nodes
     */
    private List<T> fPath = new ArrayList<T>();

    private int fPathPos;

    private int fStackPos;

    /**
     * The common constructor used to initialize internal fields
     */
    public TreeBuilder() {
        super(null, null);
        fSource = this;
    }

    public void align(List<T> path, INodeWalkerListener<T, E> listener)
        throws E {
        fPath.clear();
        fPath.addAll(path);
        int pathSize = fPath.size();
        int stackSize = fStack.size();
        fStackPos = 0;
        fPathPos = 0;
        if (fStackPos < stackSize && fPathPos < pathSize) {
            while (fStackPos < stackSize && fPathPos < pathSize) {
                int stackShift = getLength(fStack, fStackPos);
                int pathShift = getLength(fPath, fPathPos);
                if (stackShift >= pathShift) {
                    if (!equal(fStack, fStackPos, fPath, fPathPos))
                        break;
                    if (fPathPos >= pathSize - 1)
                        break;
                    fPathPos++;
                }
                fStackPos++;
            }
        }
        fNextNode = shift();
        while (!isFinished()) {
            shift(listener);
            if (fNextNode == null && pathSize > 0 && fPathPos >= pathSize) {
                break;
            }
        }
    }

    protected boolean equal(
        List<T> firstList,
        int firstPos,
        List<T> secondList,
        int secondPos) {
        T first = firstList.get(firstPos);
        T second = secondList.get(secondPos);
        return first == null || second == null ? first == second : first
            .equals(second);
    }

    public T getFirstSubnode(T node) {
        return shift();
    }

    protected int getLength(List<T> list, int pos) {
        return pos + 1;
    }

    public T getNextSubnode(T parent, T node) {
        return shift();
    }

    private T shift() {
        T result = null;
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