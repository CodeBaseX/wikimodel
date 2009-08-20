package org.wikimodel.template;

import java.util.Map;

import org.wikimodel.template.impl.AbstractDataSelector;
import org.wikimodel.template.impl.ITemplateListener;
import org.wikimodel.template.impl.ITemplateNodeManager;
import org.wikimodel.template.impl.TemplateEngine;

public abstract class XmlTemplateEngine<N> {

    private TemplateEngine<N> fEngine;

    private AbstractDataSelector<N> fSelector;

    public XmlTemplateEngine() {
        fSelector = newDataSelector();
        ITemplateNodeManager<N> manager = newTemplateNodeManager();
        fEngine = new TemplateEngine<N>(manager, fSelector);
    }

    protected abstract String getElementName(N node);

    protected abstract Map<String, String> getElementParams(N node);

    protected abstract String getTextContent(N node);

    protected abstract boolean isElement(N node);

    protected abstract AbstractDataSelector<N> newDataSelector();

    protected abstract ITemplateNodeManager<N> newTemplateNodeManager();

    public void process(N node, Object data, final IXmlTemplateListener listener) {
        fEngine.process(node, data, new ITemplateListener<N>() {
            public boolean beginNode(N node, Object data) {
                boolean visit;
                if (isElement(node)) {
                    visit = true;
                    String name = getElementName(node);
                    Map<String, String> params = getElementParams(node);
                    selectAttributes(node, data, params);
                    listener.beginElement(name, params);
                    String content = selectContent(node, data);
                    if (content != null) {
                        listener.onText(content);
                        visit = false;
                    }
                } else {
                    String text = getTextContent(node);
                    listener.onText(text);
                    visit = false;
                }
                return visit;
            }

            public void endNode(N node, Object data) {
                if (isElement(node)) {
                    String name = getElementName(node);
                    listener.endElement(name);
                }
            }

            /**
             * @param node
             * @param data
             * @param path
             * @return
             */
            private String select(N node, Object data, String path) {
                if (path == null)
                    return null;
                Object content = fSelector.selectData(node, data, path);
                return content != null ? XmlTemplateEngine.this
                    .toString(content) : null;
            }

            private void selectAttributes(
                N node,
                Object data,
                Map<String, String> params) {
                String path = fSelector.getSelector(
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
                    String value = select(node, data, selector);
                    if (value != null) {
                        params.put(attr, value);
                    }
                }
            }

            private String selectContent(N node, Object data) {
                String path = fSelector.getSelector(
                    node,
                    IXmlTemplateConst.JSVALUE);
                String result = select(node, data, path);
                return result;
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