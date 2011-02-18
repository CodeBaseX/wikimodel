/**
 * 
 */
package org.wikimodel.wem.xml.sax;

import java.util.Map;
import java.util.Map.Entry;

import org.wikimodel.wem.WikiPageUtil;
import org.wikimodel.wem.xml.ITagListener;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

/**
 * @author kotelnikov
 */
public class WemToSax implements ITagListener {

    public final static String USER_NS = "http://www.wikimodel.org/ns/user-defined-params#";

    public final static String USER_PREFIX = "u";

    public final static String WEM_NS = "http://www.wikimodel.org/ns/wem#";

    public final static String WEM_PREFIX = "w";

    private int fDepth;

    private ContentHandler fHandler;

    /**
     * 
     */
    public WemToSax(ContentHandler handler) {
        fHandler = handler;
    }

    /**
     * @see org.wikimodel.wem.xml.ITagListener#beginTag(java.lang.String,
     *      java.util.Map, java.util.Map)
     */
    public void beginTag(
        String tagName,
        Map<String, String> tagParams,
        Map<String, String> userParams) {
        try {
            if (fDepth == 0) {
                fHandler.startDocument();
                fHandler.startPrefixMapping(WEM_PREFIX, WEM_NS);
                fHandler.startPrefixMapping(USER_PREFIX, USER_NS);
            }
            Attributes atts = getAttributes(tagParams, userParams);
            fHandler.startElement(WEM_NS, tagName, getQualifiedName(
                WEM_PREFIX,
                tagName), atts);
            fDepth++;
        } catch (Throwable e) {
            handleError("beginTag error " + tagParams.toString(), e);
        }
    }

    /**
     * @see org.wikimodel.wem.xml.ITagListener#endTag(java.lang.String,
     *      java.util.Map, java.util.Map)
     */
    public void endTag(
        String tagName,
        Map<String, String> tagParams,
        Map<String, String> userParams) {
        try {
            fHandler.endElement(WEM_NS, tagName, getQualifiedName(
                WEM_PREFIX,
                tagName));
            fDepth--;
            if (fDepth == 0) {
                fHandler.endDocument();
            }
        } catch (Throwable t) {
            handleError("endTag error " + tagParams.toString(), t);
        }
    }

    private Attributes getAttributes(
        final Map<String, String> tagParams,
        final Map<String, String> userParams) {
        final Object[] tArray = tagParams.entrySet().toArray();
        final Object[] uArray = userParams.entrySet().toArray();
        return new Attributes() {

            @SuppressWarnings("unchecked")
            private Map.Entry<String, String> getEntry(int index) {
                if (index < 0 || index > tArray.length + uArray.length)
                    return null;
                if (index < tArray.length)
                    return ((Map.Entry<String, String>) tArray[index]);
                index -= tArray.length;
                return ((Map.Entry<String, String>) uArray[index]);
            }

            public int getIndex(String qName) {
                int idx = qName.indexOf(':');
                String prefix = (idx >= 0) ? qName.substring(0, idx) : "";
                qName = qName.substring(idx + 1);
                return getIndex(prefix, qName, WEM_PREFIX, USER_PREFIX);
            }

            public int getIndex(String uri, String localName) {
                return getIndex(uri, localName, WEM_NS, USER_NS);
            }

            /**
             * @param uri
             * @param localName
             * @param wemNS
             * @param usrNS
             * @return
             */
            @SuppressWarnings("unchecked")
            private int getIndex(
                String uri,
                String localName,
                String wemNS,
                String usrNS) {
                Object[] array = null;
                int base = 0;
                if (wemNS.equals(uri)) {
                    array = tArray;
                } else if (usrNS.equals(uri)) {
                    base = tArray.length;
                    array = uArray;
                }
                if (array == null)
                    return -1;
                int result = -1;
                int pos = 0;
                for (Object obj : array) {
                    Map.Entry<String, String> entry = (Entry<String, String>) obj;
                    if (localName.equals(entry.getKey())) {
                        result = base + pos;
                        break;
                    }
                    pos++;
                }
                return result;
            }

            public int getLength() {
                return tArray.length + uArray.length;
            }

            public String getLocalName(int index) {
                Map.Entry<String, String> entry = getEntry(index);
                if (entry == null)
                    return null;
                return entry.getKey();
            }

            public String getQName(int index) {
                Map.Entry<String, String> entry = getEntry(index);
                if (entry == null)
                    return null;
                String prefix = null;
                if (isUserParam(index))
                    prefix = USER_PREFIX;
                else if (isTagParam(index))
                    prefix = WEM_PREFIX;
                return getQualifiedName(prefix, entry.getKey());
            }

            public String getType(int index) {
                return "CDATA";
            }

            public String getType(String qName) {
                return "CDATA";
            }

            public String getType(String uri, String localName) {
                return "CDATA";
            }

            public String getURI(int index) {
                if (isTagParam(index))
                    return WEM_NS;
                if (isUserParam(index))
                    return USER_NS;
                return null;
            }

            public String getValue(int index) {
                Entry<String, String> entry = getEntry(index);
                if (entry != null)
                    return entry.getValue();
                return null;
            }

            private String getValue(Map<String, String> map, String key) {
                String value = map.get(key);
                value = WikiPageUtil.escapeXmlAttribute(value);
                return value;
            }

            public String getValue(String qName) {
                int idx = qName.indexOf(':');
                String prefix = qName.substring(0, idx);
                qName = qName.substring(idx + 1);
                if (WEM_PREFIX.equals(prefix))
                    return getValue(tagParams, qName);
                if (USER_PREFIX.equals(prefix))
                    return getValue(userParams, qName);
                return null;
            }

            public String getValue(String uri, String localName) {
                if (WEM_NS.equals(uri))
                    return getValue(tagParams, localName);
                if (USER_NS.equals(uri))
                    return getValue(userParams, localName);
                return null;
            }

            private boolean isTagParam(int index) {
                return (index >= 0 && index < tArray.length);
            }

            private boolean isUserParam(int index) {
                return (index >= tArray.length && index < tArray.length
                    + uArray.length);
            }
        };
    }

    /**
     * @param tagName
     * @return
     */
    private String getQualifiedName(String prefix, String tagName) {
        tagName = WikiPageUtil.escapeXmlAttribute(tagName);
        return prefix != null && !"".equals(prefix)
            ? prefix + ":" + tagName
            : tagName;
    }

    private void handleError(String s, Throwable e) {
        System.out.println( s );
//        if (e instanceof Error) {            
//            throw ((Error) e);
//        }
//        return new RuntimeException(e);
    }

    /**
     * @see org.wikimodel.wem.xml.ITagListener#onCDATA(java.lang.String)
     */
    public void onCDATA(String content) {
        onText(content);
    }

    /**
     * @see org.wikimodel.wem.xml.ITagListener#onTag(java.lang.String,
     *      java.util.Map, java.util.Map)
     */
    public void onTag(
        String tagName,
        Map<String, String> tagParams,
        Map<String, String> userParams) {
        beginTag(tagName, tagParams, userParams);
        endTag(tagName, tagParams, userParams);
    }

    /**
     * @see org.wikimodel.wem.xml.ITagListener#onText(java.lang.String)
     */
    public void onText(String content) {
        try {
            content = WikiPageUtil.escapeXmlString(content, false);
            char[] chars = content.toCharArray();
            fHandler.characters(chars, 0, chars.length);
        } catch (Throwable e) {
            handleError("onText error " + content, e);
        }
    }

}
