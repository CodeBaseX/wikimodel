/**
 * 
 */
package org.wikimodel.template;

import java.util.Map;

/**
 * @author kotelnikov
 */
public interface IXmlTemplateListener {

    void beginElement(String name, Map<String, String> params);

    void endElement(String name);

    void onText(String text);

}
