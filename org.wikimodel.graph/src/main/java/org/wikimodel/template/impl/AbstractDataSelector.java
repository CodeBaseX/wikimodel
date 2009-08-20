/**
 * 
 */
package org.wikimodel.template.impl;

import java.util.Collection;

import org.wikimodel.template.IXmlTemplateConst;

/**
 * @author kotelnikov
 */
public abstract class AbstractDataSelector<N>
    implements
    ITemplateDataSelector<N> {

    /**
     * 
     */
    public AbstractDataSelector() {
        super();
    }

    protected abstract String getAttribute(N node, String attr);

    /**
     * This method tries to interpret the given parameter as a container of
     * objects and returns them in the form of an array. If this parameter could
     * not be interpreted as a container then this method returns
     * <code>null</code>. This method could be overloaded in subclasses to
     * re-define the default behaveour.
     * 
     * @param data
     * @return an array of entries contained in the given container
     */
    protected Object[] getObjectAsArray(Object data) {
        if (data instanceof Collection<?>) {
            return ((Collection<?>) data).toArray();
        }
        return null;
    }

    /**
     * @param node
     * @param attr
     * @return
     */
    public String getSelector(N node, String attr) {
        String jscontent = getAttribute(node, attr);
        return jscontent != null && !"".equals(jscontent) ? jscontent : null;
    }

    protected abstract Object selectChildData(N node, Object data, String path);

    /**
     * @see org.wikimodel.template.impl.ITemplateDataSelector#selectData(java.lang.Object,
     *      java.lang.Object)
     */
    public Object[] selectData(N node, Object data) {
        Object[] result = null;
        String selector = getSelector(node, IXmlTemplateConst.JSITERATE);
        boolean expand = (selector != null);
        if (selector == null) {
            selector = getSelector(node, IXmlTemplateConst.JSSELECT);
        }
        if (selector != null) {
            data = selectData(node, data, selector);
        }
        if (result == null) {
            if (expand) {
                result = getObjectAsArray(data);
            }
            if (result == null) {
                result = toArray(data);
            }
        }
        return result;
    }

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
     * @param data
     * @return
     */
    protected Object[] toArray(Object... data) {
        return data;
    }

}
