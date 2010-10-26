/**
 * 
 */
package org.wikimodel.wem.xml;

import org.wikimodel.wem.IWemListenerTable;
import org.wikimodel.wem.WikiParameters;

/**
 * @author kotelnikov
 */
public class WemTableTagNotifier extends AbstractTagNotifier
    implements
    IWemListenerTable {

    /**
     * @param listener
     */
    public WemTableTagNotifier(ITagListener listener) {
        super(listener);
    }

    public void beginTable(WikiParameters params) {
        fListener.beginTag(TABLE, EMPTY_MAP, userParams(params));
    }

    public void beginTableCell(boolean tableHead, WikiParameters params) {
        String tag = tableHead ? TABLE_HEAD : TABLE_CELL;
        fListener.beginTag(tag, EMPTY_MAP, userParams(params));
    }

    public void beginTableRow(WikiParameters params) {
        fListener.beginTag(TABLE_ROW, EMPTY_MAP, userParams(params));
    }

    public void endTable(WikiParameters params) {
        fListener.endTag(TABLE, EMPTY_MAP, userParams(params));
    }

    public void endTableCell(boolean tableHead, WikiParameters params) {
        String tag = tableHead ? TABLE_HEAD : TABLE_CELL;
        fListener.endTag(tag, EMPTY_MAP, userParams(params));
    }

    public void endTableRow(WikiParameters params) {
        fListener.endTag(TABLE_ROW, EMPTY_MAP, userParams(params));
    }

    public void onTableCaption(String str) {
        fListener.onTag(
            TABLE_CAPTION,
            tagParams(TABLE_CAPTION_PARAM, str),
            EMPTY_MAP);
    }

}
