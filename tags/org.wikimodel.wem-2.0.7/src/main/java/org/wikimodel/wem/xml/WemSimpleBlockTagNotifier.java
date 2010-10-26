/**
 * 
 */
package org.wikimodel.wem.xml;

import java.util.Map;

import org.wikimodel.wem.IWemListenerSimpleBlocks;
import org.wikimodel.wem.WikiParameters;

/**
 * @author kotelnikov
 */
public class WemSimpleBlockTagNotifier extends AbstractTagNotifier
    implements
    IWemListenerSimpleBlocks {

    /**
     * @param listener
     */
    public WemSimpleBlockTagNotifier(ITagListener listener) {
        super(listener);
    }

    public void beginInfoBlock(String infoType, WikiParameters params) {
        fListener.beginTag(
            INFO_BLOCK,
            tagParams(INFO_BLOCK_TYPE, infoType),
            userParams(params));
    }

    public void beginParagraph(WikiParameters params) {
        fListener.beginTag(PARAGRAPH, EMPTY_MAP, userParams(params));
    }

    public void endInfoBlock(String infoType, WikiParameters params) {
        fListener.endTag(
            INFO_BLOCK,
            tagParams(INFO_BLOCK_TYPE, infoType),
            userParams(params));
    }

    public void endParagraph(WikiParameters params) {
        fListener.endTag(PARAGRAPH, EMPTY_MAP, userParams(params));
    }

    public void onEmptyLines(int count) {
        fListener.onTag(
            EMPTY_LINES,
            tagParams(EMPTY_LINES_SIZE, "" + count),
            EMPTY_MAP);
    }

    public void onHorizontalLine(WikiParameters params) {
        fListener.onTag(HORIZONTAL_LINE, EMPTY_MAP, userParams(params));
    }

    public void onVerbatimBlock(String str, WikiParameters params) {
        Map<String, String> userParams = userParams(params);
        fListener.beginTag(VERBATIM_BLOCK, EMPTY_MAP, userParams);
        fListener.onCDATA(str);
        fListener.endTag(VERBATIM_BLOCK, EMPTY_MAP, userParams);
    }

}
