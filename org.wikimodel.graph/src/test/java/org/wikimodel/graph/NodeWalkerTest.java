/**
 * 
 */
package org.wikimodel.graph;

import java.io.File;
import java.util.Iterator;

import junit.framework.TestCase;

import org.wikimodel.iterator.ShiftIterator;

/**
 * @author kotelnikov
 */
public class NodeWalkerTest extends TestCase {

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
        INodeWalkerSource source = new IteratorBasedNodeSource() {

            protected Iterator newIterator(Object currentNode) throws Exception {
                File f = (File) currentNode;
                final File[] array = f.listFiles();
                return new ShiftIterator() {

                    int pos = 0;

                    protected Object shiftItem() {
                        return array != null && pos < array.length
                            ? array[pos++]
                            : null;
                    }

                };
            }

        };
        File file = new File("./src");
        final NodeWalker walker = new NodeWalker(source, file);
        final INodeWalkerListener listener = new NodeWalkerListener1(walker) {

            protected void onBeginNode(Object parent, Object node)
                throws Exception {
                print("<" + ((File) node).getName() + ">");
                fDepth++;
            }

            protected void onEndNode(Object parent, Object node)
                throws Exception {
                fDepth--;
                print("</" + ((File) node).getName() + ">");
            }

            protected void onBeginSubnodes(Object parent) throws Exception {
                print("<children key='" + ((File) parent).getName() + "'>");
            }

            protected void onEndSubnodes(Object parent) throws Exception {
                print("</children><!-- " + ((File) parent).getName() + " -->");
            }

        };
        Iterator iterator = new ShiftIterator() {

            protected Object shiftItem() {
                try {
                    while (!walker.isFinished()) {
                        boolean in = walker.shift(listener);
                        if (walker.getNextNode() == null)
                            break;
                        if (in)
                            break;
                    }
                    return walker.getPeekNode();
                } catch (Exception e) {
                    return null;
                }
            }
        };

        while (iterator.hasNext()) {
            File f = (File) iterator.next();
            print("[" + f.getName() + "]");
        }
    }
}
