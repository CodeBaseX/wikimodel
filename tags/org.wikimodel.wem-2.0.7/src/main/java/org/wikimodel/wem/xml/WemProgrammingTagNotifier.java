/**
 * 
 */
package org.wikimodel.wem.xml;

import java.util.Map;

import org.wikimodel.wem.IWemListenerProgramming;
import org.wikimodel.wem.WikiParameters;

/**
 * @author kotelnikov
 */
public class WemProgrammingTagNotifier extends AbstractTagNotifier
    implements
    IWemListenerProgramming {

    /**
     * @param listener
     */
    public WemProgrammingTagNotifier(ITagListener listener) {
        super(listener);
    }

    public void onExtensionBlock(String extensionName, WikiParameters params) {
        fListener.onTag(EXTENSION_BLOCK, tagParams(
            EXTENSION_NAME,
            extensionName), userParams(params));
    }

    public void onExtensionInline(String extensionName, WikiParameters params) {
        fListener.onTag(EXTENSION_INLINE, tagParams(
            EXTENSION_NAME,
            extensionName), userParams(params));
    }

    public void onMacroBlock(
        String macroName,
        WikiParameters params,
        String content) {
        Map<String, String> tagParams = tagParams(MACRO_NAME, macroName);
        Map<String, String> userParams = userParams(params);
        fListener.beginTag(MACRO_BLOCK, tagParams, userParams);
        fListener.onCDATA(content);
        fListener.endTag(MACRO_BLOCK, tagParams, userParams);
    }

    public void onMacroInline(
        String macroName,
        WikiParameters params,
        String content) {
        Map<String, String> tagParams = tagParams(MACRO_NAME, macroName);
        Map<String, String> userParams = userParams(params);
        fListener.beginTag(MACRO_INLINE, tagParams, userParams);
        fListener.onCDATA(content);
        fListener.endTag(MACRO_INLINE, tagParams, userParams);
    }

}
