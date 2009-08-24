/**
 * 
 */
package org.wikimodel.template.impl;

import org.wikimodel.graph.INodeWalkerListener;
import org.wikimodel.graph.INodeWalkerSource;
import org.wikimodel.graph.util.NodeWalkerIterator;

public class TemplateEngine<N> {

    public class Context {

        private int fIndex = -1;

        private N fNode;

        private Context fParent;

        private Object[] fSelectedData;

        public Context(Context parent, N node, Object[] data) {
            fParent = parent;
            fNode = node;
            fSelectedData = data;
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
            if (!compare(fParent, context.fParent))
                return false;
            if (fIndex != context.fIndex)
                return false;
            return true;
        }

        public Context getChild() {
            N child = fManager.getFirstChild(fNode);
            return newContext(this, child);
        }

        public int getIndex() {
            return fIndex;
        }

        public Context getNext() {
            if (fIndex < fSelectedData.length - 1) {
                return this;
            }
            Context result = null;
            N node = fNode;
            while (result == null && node != null) {
                node = fManager.getNextSibling(node);
                result = newContext(fParent, node);
            }
            return result;
        }

        public N getNode() {
            return fNode;
        }

        /**
         * @return
         */
        public Object[] getSelectedData() {
            return fSelectedData;
        }

        @Override
        public int hashCode() {
            return fNode != null ? fNode.hashCode() : null;
        }

        public void inc() {
            fIndex++;
        }

        protected Context newContext(Context parent, N node) {
            if (node == null || parent == null)
                return null;
            Object[] childData = parent.selectChildData(node);
            return childData != null
                ? new Context(parent, node, childData)
                : null;
        }

        private Object[] selectChildData(N node) {
            if (fSelectedData == null || fIndex >= fSelectedData.length)
                return null;
            Object[] result = fSelector.selectData(node, fSelectedData, fIndex);
            return result;
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
        Context topNode = new Context(null, template, new Object[] { data });
        INodeWalkerListener<Context, RuntimeException> walkerListener = new INodeWalkerListener<Context, RuntimeException>() {

            public boolean beginNode(Context parent, Context node)
                throws RuntimeException {
                Object[] data = node.getSelectedData();
                int index = node.getIndex();
                boolean visit = true;
                if (data != null && index < data.length) {
                    N n = node.getNode();
                    visit = listener.beginNode(n, data, index);
                }
                return visit;
            }

            public void endNode(Context parent, Context node)
                throws RuntimeException {
                Object[] data = node.getSelectedData();
                int index = node.getIndex();
                if (data != null && index < data.length) {
                    N n = node.getNode();
                    listener.endNode(n, data, index);
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