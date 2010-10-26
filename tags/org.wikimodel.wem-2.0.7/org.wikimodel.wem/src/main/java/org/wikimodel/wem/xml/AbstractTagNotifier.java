/**
 * 
 */
package org.wikimodel.wem.xml;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.wikimodel.wem.WikiFormat;
import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.WikiStyle;

/**
 * @author kotelnikov
 */
public class AbstractTagNotifier implements ISaxConst {

    protected static final Map<String, String> EMPTY_MAP = Collections
        .emptyMap();

    protected ITagListener fListener;

    /**
     * 
     */
    public AbstractTagNotifier(ITagListener listener) {
        fListener = listener;
    }

    /**
     * @param map
     * @param params
     * @return
     */
    private Map<String, String> addParams(
        Map<String, String> map,
        String... params) {
        for (int i = 0; i < params.length; i++) {
            String key = params[i];
            i++;
            String value = i < params.length ? params[i] : null;
            map.put(key, value);
        }
        return map;
    }

    /**
     * @param params
     * @return
     */
    private Map<String, String> getParamsMap(Iterable<WikiParameter> params) {
        Map<String, String> map = newParamMap();
        for (WikiParameter param : params) {
            String key = param.getKey();
            String value = param.getValue();
            map.put(key, value);
        }
        return map;
    }

    /**
     * @return
     */
    protected Map<String, String> newParamMap() {
        return new LinkedHashMap<String, String>();
    }

    protected Map<String, String> tagParams(
        Map<String, String> tagParams,
        String... params) {
        if (tagParams == EMPTY_MAP)
            return tagParams(params);
        return addParams(tagParams, params);
    }

    protected Map<String, String> tagParams(String... params) {
        if (params.length == 0)
            return EMPTY_MAP;
        Map<String, String> map = newParamMap();
        return addParams(map, params);
    }

    protected Map<String, String> tagParams(WikiFormat format) {
        if (format == null)
            return EMPTY_MAP;
        List<WikiStyle> styles = format.getStyles();
        if (styles.isEmpty())
            return EMPTY_MAP;
        Map<String, String> map = newParamMap();
        StringBuffer buf = new StringBuffer();
        for (WikiStyle style : styles) {
            String name = style.toString();
            if (buf.length() > 0)
                buf.append("; ");
            buf.append(name);
        }
        map.put(STYLES, buf.toString());
        return map;
    }

    protected Map<String, String> tagParams(WikiReference ref) {
        if (ref == null)
            return EMPTY_MAP;
        return tagParams("label", ref.getLabel(), "href", ref.getLink());
    }

    protected Map<String, String> userParams(WikiFormat format) {
        if (format == null)
            return EMPTY_MAP;
        List<WikiParameter> params = format.getParams();
        return getParamsMap(params);
    }

    protected Map<String, String> userParams(WikiParameters params) {
        if (params == null || params.getSize() == 0)
            return EMPTY_MAP;
        return getParamsMap(params);
    }

    protected Map<String, String> userParams(WikiReference ref) {
        if (ref == null)
            return EMPTY_MAP;
        return userParams(ref.getParameters());
    }

}
