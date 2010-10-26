/*
 * Copyright (c) 2010 mkirst(at portolancs dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wikimodel.wem.mediawiki;

import org.wikimodel.wem.IWikiMacroParser;
import org.wikimodel.wem.WikiMacro;
import org.wikimodel.wem.WikiParameters;

/**
 * @author mkirst(at portolancs dot com)
 */
public class MediaWikiMacroParser implements IWikiMacroParser {

    /* (non-Javadoc)
     * @see org.wikimodel.wem.IWikiMacroParser#parse(java.lang.String)
     */
    public WikiMacro parse(String str) {
        String name = null;
        String definition = str;
        WikiParameters params = WikiParameters.EMPTY;
        if (str.equals("__TOC__")) {
            name = WikiMacro.MACRO_TOC;
            params = params.addParameter("numbered", "true");
        } else if (str.equals("__NOTOC__")) {
            name = WikiMacro.MACRO_NOTOC;
        } else if (str.equals("__FORCETOC__")) {
            name = WikiMacro.MACRO_FORCETOC;
        } else if (str.contains("references")) {
            // moved/borrowed from MediawikiScanner.jj [r]
            // FIXME: where is the documentation for THIS feature?
            name = WikiMacro.MACRO_FOOTNOTES;
        } else if (str.length() > 4 && str.startsWith("{{") && str.endsWith("}}")) {
            // template with named and unnamed parameters
            final String macro = str.substring(2, str.length()-2);
            String[] parts = macro.split("[|]");
            name = parts[0];
            for (int i=1; i<parts.length; i++) {
                String key = Integer.toString(i);
                String value = parts[i];
                int equidx = parts[i].indexOf('=');
                if (equidx > 0) {
                    key = parts[i].substring(0, equidx);
                    value = parts[i].substring(equidx+1);
                }
                params = params.addParameter(key, value);
            }
        } else {
            // seems to be an unsupported magic word, see
            // http://www.mediawiki.org/wiki/Help:Magic_words
            name = WikiMacro.UNHANDLED_MACRO;
        }
        return new WikiMacro(name, params, definition);
    }

}
