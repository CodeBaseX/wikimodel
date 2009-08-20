/**
 * 
 */
package org.wikimodel.template;

import java.util.Map;

/**
 * @author kotelnikov
 */
public class PrintTemplateListener implements IXmlTemplateListener {

    /**
     * 
     */
    public PrintTemplateListener() {
    }

    /**
     * @see org.wikimodel.template.IXmlTemplateListener#beginElement(java.lang.String,
     *      java.util.Map)
     */
    public void beginElement(String name, Map<String, String> params) {
        StringBuffer buf = new StringBuffer();
        buf.append("<");
        buf.append(name);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            buf.append(" ");
            String key = entry.getKey();
            String value = entry.getValue();
            buf.append(key);
            buf.append("='");
            buf.append(value);
            buf.append("'");
        }
        buf.append(">");
        print(buf.toString());
    }

    /**
     * @see org.wikimodel.template.IXmlTemplateListener#endElement(java.lang.String)
     */
    public void endElement(String name) {
        print("</" + name + ">");
    }

    protected String escape(String text) {
        return text;
    }

    /**
     * @see org.wikimodel.template.IXmlTemplateListener#onText(java.lang.String)
     */
    public void onText(String text) {
        text = escape(text);
        print(text);
    }

    protected void print(String string) {
        System.out.print(string);
    }

}
