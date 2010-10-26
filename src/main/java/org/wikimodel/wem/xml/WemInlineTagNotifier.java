/**
 * 
 */
package org.wikimodel.wem.xml;

import org.wikimodel.wem.IWemListenerInline;
import org.wikimodel.wem.WikiFormat;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;

/**
 * @author kotelnikov
 */
public class WemInlineTagNotifier extends AbstractTagNotifier
    implements
    IWemListenerInline {

    /**
     * @param listener
     */
    public WemInlineTagNotifier(ITagListener listener) {
        super(listener);
    }

    public void beginFormat(WikiFormat format) {
        fListener.beginTag(FORMAT, tagParams(format), userParams(format));
    }

    public void endFormat(WikiFormat format) {
        fListener.endTag(FORMAT, tagParams(format), userParams(format));
    }

    public void onEscape(String str) {
        fListener.beginTag(ESCAPE, EMPTY_MAP, EMPTY_MAP);
        fListener.onText(str);
        fListener.endTag(ESCAPE, EMPTY_MAP, EMPTY_MAP);
    }

    public void onImage(String ref) {
        WikiReference r = new WikiReference(ref);
        fListener.onTag(
            IMAGE,
            tagParams(tagParams(r), IMAGE_IMPLICIT, "true"),
            userParams(r));
    }

    public void onImage(WikiReference ref) {
        fListener.onTag(IMAGE, tagParams(
            tagParams(ref),
            IMAGE_IMPLICIT,
            "false"), userParams(ref));
    }

    public void onLineBreak() {
        fListener.onTag(LINE_BREAK, EMPTY_MAP, EMPTY_MAP);
    }

    public void onNewLine() {
        fListener.onText(NEW_LINE);
    }

    public void onReference(String ref) {
        WikiReference r = new WikiReference(ref);
        fListener.onTag(REF_IMPLICIT, tagParams(r), userParams(r));
    }

    public void onReference(WikiReference ref) {
        fListener.onTag(REF_IMPLICIT, tagParams(ref), userParams(ref));
    }

    public void onSpace(String str) {
        fListener.onText(str);
    }

    public void onSpecialSymbol(String str) {
        // fListener.beginTag(SPECIAL, EMPTY_MAP, EMPTY_MAP);
        fListener.onText(str);
        // fListener.endTag(SPECIAL, EMPTY_MAP, EMPTY_MAP);
    }

    public void onVerbatimInline(String str, WikiParameters params) {
        fListener.beginTag(VERBATIM_INLINE, EMPTY_MAP, userParams(params));
        fListener.onCDATA(str);
        fListener.endTag(VERBATIM_INLINE, EMPTY_MAP, userParams(params));
    }

    public void onWord(String str) {
        fListener.onText(str);
    }

}
