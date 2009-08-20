/**
 * 
 */
package org.wikimodel.template.impl;

import org.wikimodel.graph.INodeWalkerListener;
import org.wikimodel.graph.INodeWalkerSource;
import org.wikimodel.graph.util.NodeWalkerIterator;

public class TemplateEngine<N> {

    public class Context {

        private Object[] fData;

        private int fIndex = -1;

        private N fNode;

        private Object fParentData;

        public Context(N node, Object data) {
            fNode = node;
            fParentData = data;
            fData = fSelector.selectData(node, data);
            inc();
        }

        private boolean compare(Object first, Object second) {
            return first != null ? first.equals(second) : first == second;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            Context context = (Context) obj;
            if (!compare(fNode, context.fNode))
                return false;
            if (!compare(fParentData, context.fParentData))
                return false;
            if (fIndex != context.fIndex)
                return false;
            return true;
        }

        public Context getChild() {
            Object data = getData();
            if (data == null)
                return null;
            N child = fManager.getFirstChild(fNode);
            return child != null ? newContext(child, data) : null;
        }

        /**
         * @return
         */
        public Object getData() {
            return fIndex < fData.length ? fData[fIndex] : null;
        }

        public Context getNext() {
            if (fIndex < fData.length - 1) {
                return this;
            }
            N next = fManager.getNextSibling(fNode);
            return next != null ? newContext(next, fParentData) : null;
        }

        public N getNode() {
            return fNode;
        }

        @Override
        public int hashCode() {
            return fNode != null ? fNode.hashCode() : null;
        }

        public void inc() {
            fIndex++;
        }

        protected Context newContext(N node, Object data) {
            return new Context(node, data);
        }

        @Override
        public String toString() {
            return fNode != null ? fNode.toString() : "";
        }
    }

    private ITemplateNodeManager<N> fManager;

    private ITemplateDataSelector<N> fSelector;

    private INodeWalkerSource<Context, RuntimeException> fSource = new INodeWalkerSource<Context, RuntimeException>() {
        public Context getFirstSubnode(Context node) throws RuntimeException {
            return node.getChild();
        }

        public Context getNextSubnode(Context parent, Context node)
            throws RuntimeException {
            return parent != null ? node.getNext() : null;
        }
    };

    public TemplateEngine(
        ITemplateNodeManager<N> manager,
        ITemplateDataSelector<N> selector) {
        fManager = manager;
        fSelector = selector;
    }

    public void process(
        N template,
        Object data,
        final ITemplateListener<N> listener) {
        Context topNode = new Context(template, data);
        INodeWalkerListener<Context, RuntimeException> walkerListener = new INodeWalkerListener<Context, RuntimeException>() {

            public boolean beginNode(Context parent, Context node)
                throws RuntimeException {
                Object data = node.getData();
                boolean visit = true;
                if (data != null) {
                    N n = node.getNode();
                    visit = listener.beginNode(n, data);
                }
                return visit;
            }

            public void endNode(Context parent, Context node)
                throws RuntimeException {
                Object data = node.getData();
                if (data != null) {
                    N n = node.getNode();
                    listener.endNode(n, data);
                }
                node.inc();
            }
        };
        NodeWalkerIterator<Context, RuntimeException> iterator = new NodeWalkerIterator<Context, RuntimeException>(
            fSource,
            walkerListener,
            topNode);
        while (iterator.hasNext()) {
            iterator.next();
        }
    }

}