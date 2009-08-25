package org.wikimodel.template;

import java.util.Collection;
import java.util.Map;

import org.wikimodel.template.impl.ITemplateDataSelector;
import org.wikimodel.template.impl.ITemplateListener;
import org.wikimodel.template.impl.TemplateEngine;

public abstract class XmlTemplateEngine<N> {

    public static abstract class XmlDataSelector<N>
        implements
        ITemplateDataSelector<N> {

        protected IXmlTemplateNodeManager<N> fNodeManager;

        /**
         * 
         */
        public XmlDataSelector(IXmlTemplateNodeManager<N> nodeManager) {
            super();
            fNodeManager = nodeManager;
        }

        /**
         * This method tries to interpret the given parameter as a container of
         * objects and returns them in the form of an array. If this parameter
         * could not be interpreted as a container then this method returns
         * <code>null</code>. This method could be overloaded in subclasses to
         * re-define the default behaveour.
         * 
         * @param node TODO
         * @param data
         * @param selector TODO
         * @return an array of entries contained in the given container
         */
        protected Object[] getObjectAsArray(N node, Object data, String selector) {
            if (data instanceof Collection<?>) {
                return ((Collection<?>) data).toArray();
            }
            return null;
        }

        protected abstract Object selectChildData(
            N node,
            Object data,
            String path);

        public Object selectData(N node, Object data, String selector) {
            if (selector == null)
                return null;
            selector = selector.trim();
            Object result = null;
            String path;
            int idx = selector.indexOf('.');
            if (idx > 0) {
                path = selector.substring(0, idx);
                selector = selector.substring(idx + 1);
            } else {
                path = selector;
                selector = "";
            }
            if (IXmlTemplateConst.THIS.equals(path)) {
                result = data;
            } else {
                result = selectChildData(node, data, path);
            }
            if (result != null && !"".equals(selector)) {
                result = selectData(node, result, selector);
            }
            return result;
        }

        /**
         * @see org.wikimodel.template.impl.ITemplateDataSelector#selectData(java.lang.Object,
         *      Object[], int)
         */
        public Object[] selectData(N node, Object[] data, int index) {
            if (data == null)
                return EMPTY_ARRAY;
            Object object = data[index];

            Object[] result = null;
            String selector = getAttribute(
                fNodeManager,
                node,
                IXmlTemplateConst.JSITERATE);
            if (selector != null) {
                object = selectData(node, object, selector);
                if (object != null) {
                    result = getObjectAsArray(node, object, selector);
                    if (result == null) {
                        result = toArray(object);
                    }
                }
            } else {
                selector = getAttribute(
                    fNodeManager,
                    node,
                    IXmlTemplateConst.JSSELECT);
                if (selector != null) {
                    object = selectData(node, object, selector);
                    result = object != null ? toArray(object) : EMPTY_ARRAY;
                } else {
                    result = toArray(object);
                }
            }
            return result;
        }

    }

    private static final Object[] EMPTY_ARRAY = toArray();

    /**
     * @param node
     * @param attr
     * @return
     */
    protected static <N> String getAttribute(
        IXmlTemplateNodeManager<N> nodeManager,
        N node,
        String attr) {
        if (!nodeManager.isElement(node))
            return null;
        String jscontent = nodeManager.getAttribute(node, attr);
        return jscontent != null && !"".equals(jscontent) ? jscontent : null;
    }

    /**
     * @param data
     * @return
     */
    protected static Object[] toArray(Object... data) {
        return data;
    }

    private TemplateEngine<N> fEngine;

    private IXmlTemplateNodeManager<N> fNodeManager;

    private XmlDataSelector<N> fSelector;

    public XmlTemplateEngine() {
        fNodeManager = newNodeManager();
        fSelector = newDataSelector(fNodeManager);
        fEngine = new TemplateEngine<N>(fNodeManager, fSelector);
    }

    protected abstract XmlDataSelector<N> newDataSelector(
        IXmlTemplateNodeManager<N> nodeManager);

    protected abstract IXmlTemplateNodeManager<N> newNodeManager();

    public void process(N node, Object data, final IXmlTemplateListener listener) {
        fEngine.process(node, data, new ITemplateListener<N>() {
            public boolean beginNode(N node, Object[] array, int index) {
                boolean visit;
                if (fNodeManager.isElement(node)) {
                    visit = true;
                    String name = fNodeManager.getElementName(node);
                    Map<String, String> params = fNodeManager
                        .getElementParams(node);
                    Object data = array[index];
                    selectAttributes(node, data, params);
                    listener.beginElement(name, params);

                    String path = getAttribute(
                        fNodeManager,
                        node,
                        IXmlTemplateConst.JSVALUE);
                    if (path != null) {
                        String content = selectStringContent(node, data, path);
                        if (content == null)
                            content = "";
                        listener.onText(content);
                        visit = false;
                    }
                } else {
                    String text = fNodeManager.getTextContent(node);
                    listener.onText(text);
                    visit = false;
                }
                return visit;
            }

            public void endNode(N node, Object[] array, int index) {
                if (fNodeManager.isElement(node)) {
                    String name = fNodeManager.getElementName(node);
                    listener.endElement(name);
                }
            }

            private void selectAttributes(
                N node,
                Object data,
                Map<String, String> params) {
                String path = getAttribute(
                    fNodeManager,
                    node,
                    IXmlTemplateConst.JSATTRIBUTES);
                if (path == null)
                    return;
                String[] array = path.split(";");
                for (String str : array) {
                    str = str.trim();
                    if ("".equals(str))
                        continue;
                    int idx = str.indexOf(':');
                    String attr = str.substring(0, idx);
                    String selector = str.substring(idx + 1);
                    String value = selectStringContent(node, data, selector);
                    if (value != null) {
                        params.put(attr, value);
                    }
                }
            }

            /**
             * @param node
             * @param data
             * @param path
             * @return
             */
            private String selectStringContent(N node, Object data, String path) {
                if (path == null)
                    return null;
                Object content = fSelector.selectData(node, data, path);
                return XmlTemplateEngine.this.toString(content);
            }

        });
    }

    /**
     * @param data
     * @return
     */
    protected String toString(Object data) {
        return data != null ? data.toString() : "";
    }
}