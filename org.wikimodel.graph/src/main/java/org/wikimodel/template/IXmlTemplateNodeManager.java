/**
 * 
 */
package org.wikimodel.template;

import java.util.Map;

import org.wikimodel.template.impl.ITemplateNodeManager;

/**
 * @author kotelnikov
 */
public interface IXmlTemplateNodeManager<N> extends ITemplateNodeManager<N> {

    String getAttribute(N node, String attrName);

    String getElementName(N node);

    Map<String, String> getElementParams(N node);

    String getTextContent(N node);

    boolean isElement(N node);

}
