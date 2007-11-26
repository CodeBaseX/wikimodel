/**
 * 
 */
package org.wikimodel.graph;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import junit.framework.TestCase;

import org.wikimodel.graph.util.NodeWalkerIterator;
import org.wikimodel.iterator.ShiftIterator;

/**
 * @author kotelnikov
 */
public class NodeWalkerTest extends TestCase {
    //
    // static abstract class NodeFilter<T> implements INodeWalkerSource<T> {
    //
    // private INodeWalkerSource<T> fSource;
    //
    // public NodeFilter(INodeWalkerSource<T> source) {
    // fSource = source;
    // }
    //
    // private T filter(T node, T subnode) {
    // while (subnode != null && isFiltered(subnode)) {
    // subnode = fSource.getNextSubnode(node, subnode);
    // }
    // return subnode;
    // }
    //
    // public T getFirstSubnode(T node) {
    // T next = fSource.getFirstSubnode(node);
    // return filter(node, next);
    // }
    //
    // public T getNextSubnode(T parent, T node) {
    // T next = fSource.getNextSubnode(parent, node);
    // return filter(parent, next);
    // }
    //
    // /**
    // * @param node the node to check
    // * @return <code>true</code> if the specified should not be returned
    // * by the filter
    // */
    // protected abstract boolean isFiltered(T node);
    // }

    int fDepth;

    /**
     * @param name
     */
    public NodeWalkerTest(String name) {
        super(name);
    }

    void print(String string) {
        for (int i = 0; i < fDepth; i++) {
            System.out.print("    ");
        }
        System.out.println(string);
    }

    /**
     * 
     */
    public void test() {
        INodeWalkerSource<File> source = new IteratorBasedNodeSource<File>() {
            protected Iterator<File> newIterator(File f) {
                File[] a = f.listFiles();
                if (a == null)
                    return null;
                Arrays.sort(a);
                final File[] array = a;
                return new ShiftIterator<File>() {

                    int pos = 0;

                    protected File shiftItem() {
                        File result = null;
                        while (result == null && pos < array.length) {
                            File file = array[pos++];
                            if (!isFiltered(file))
                                result = file;
                        }
                        return result;
                    }

                    private boolean isFiltered(File file) {
                        return ".svn".equals(file.getName());
                    }
                };
            }

        };
        // source = new NodeFilter<File>(source) {
        //
        // @Override
        // protected boolean isFiltered(File node) {
        // String name = node.getName();
        // return ".svn".equals(name);
        // }
        //
        // };
        File file = new File("./src");
        final INodeWalkerListener<File> listener = new NodeWalkerListener<File>() {

            @Override
            public void beginNode(File parent, File node) {
                fDepth++;
            }

            @Override
            public void endNode(File parent, File node) {
                fDepth--;
            }

        };

        Iterator<File> iterator = new NodeWalkerIterator<File>(
            source,
            listener,
            file) {
            @Override
            protected AbstractNodeWalker.Mode getMode() {
                return AbstractNodeWalker.Mode.EACH; // super.getMode();
            }
        };

        while (iterator.hasNext()) {
            File f = iterator.next();
            print("[" + f.getName() + "]");
        }
    }
}
