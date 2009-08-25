/**
 * 
 */
package org.wikimodel.wem.xml;

import java.util.Map;

/**
 * @author kotelnikov
 */
public interface ITagListener {

    void beginTag(
        String tagName,
        Map<String, String> tagParams,
        Map<String, String> userParams);

    void endTag(
        String tagName,
        Map<String, String> tagParams,
        Map<String, String> userParams);

    void onCDATA(String content);

    void onTag(
        String tagName,
        Map<String, String> tagParams,
        Map<String, String> userParams);

    void onText(String str);

}
