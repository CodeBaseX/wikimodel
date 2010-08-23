/*******************************************************************************
 * Copyright (c) 2005,2008 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.mediawiki;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.WikiReferenceParser;

/**
 * @author kotelnikov
 * @author mkirst(at portolancs dot com)
 */
public class MediaWikiReferenceParser extends WikiReferenceParser
{

    private static final String PREFIX_IMAGE = "^\\s*(?:i|I)mage:.*";
    private static final String PREFIX_FILE = "^\\s*(?:f|F)ile:.*";
    private static final String PREFIX_MAILTO = "^\\s*(?:m|M)ailto:.*";

    private static final List<String> format = Arrays.asList("border", "frame", "thumb", "frameless");

    private static final List<String> align = Arrays.asList("left", "right", "center", "none");

    private static final List<String> valign =
        Arrays.asList("baseline", "sub", "super", "top", "text-top", "middle", "bottom", "text-bottom");

    @Override
    public WikiReference parse(String str)
    {
        // Piped Internal Link
        // [[Main Page|different text]]
        // [[File:image.png|thumb|250px|center|Image Caption]]
        // [[Image:foobar.png]]
        str = str.trim();
        String reference = null;
        String label = null;
        if (str.contains("|")) {
            reference = str.substring(0, str.indexOf('|'));
            label = str.substring(str.indexOf('|') + 1);
        } else {
            reference = str;
        }
        
        // special case images ...
        if (reference.matches(PREFIX_IMAGE) || reference.matches(PREFIX_FILE)) {
            reference = reference.substring(reference.indexOf(':') + 1);
            if (label != null) {
                WikiParameters params = this.generateImageParams(label);
                if (params.getParameter("alt") != null) {
                    label = params.getParameter("alt").getValue();
                } else {
                    label = "";
                }
                // copy ALT attribute to TITLE attribute, cause it's more user friendly
                if (params.getParameter("title") == null && "".equals(label)) {
                    params.addParameter("title", label);
                }
                return new WikiReference(reference, label, params);
            }
            return new WikiReference(reference);
        }

        // External link with label
        // Case: [http://mediawiki.org MediaWiki]
        WikiReference wikiReference;
        if (-1 != str.indexOf(' ') && (str.contains("://") || str.matches(PREFIX_MAILTO))) {
            String link = str.substring(0, str.indexOf(' ')).trim();
            label = str.substring(str.indexOf(' ') + 1).trim();
            wikiReference = new WikiReference(link, label);
        } else {
            wikiReference = new WikiReference(reference, label);
        }
        return wikiReference;

    }

    /**
     * Generate WikiParameters from the image parameters string
     * this is implemented with the Syntax given at http://www.mediawiki.org/wiki/Help:Images#Syntax
     * @param paramString MediaWiki image parameters as String
     * @return the WikiParameters.
     */
    private WikiParameters generateImageParams(String paramString)
    {
        List<WikiParameter> paramsList = new ArrayList<WikiParameter>();
        String[] params = paramString.split("[|]");

        for (String param : params) {
            if (param.indexOf("=") != -1) {
                String[] p = param.split("[=]");
                if (p.length > 1) {
                    paramsList.add(new WikiParameter(p[0], p[1]));
                } else {
                    paramsList.add(new WikiParameter(p[0], ""));
                }
            } else if (format.contains(param.toLowerCase())) {
                paramsList.add(new WikiParameter("format", param));
            } else if (align.contains(param.toLowerCase())) {
                paramsList.add(new WikiParameter("align", param));
            } else if (valign.contains(param.toLowerCase())) {
                paramsList.add(new WikiParameter("valign", param));
            } else if (param.toLowerCase().matches("[0-9]*px")) {
                paramsList.add(new WikiParameter("width", param));
            } else if (param.toLowerCase().matches("[0-9]*x[0-9]*px")) {
                paramsList.add(new WikiParameter("width", param.substring(0, param.indexOf("x")) + "px"));
                paramsList.add(new WikiParameter("height", param.substring(param.indexOf("x") + 1)));
            } else {
                paramsList.add(new WikiParameter("alt", param));
            }
        }

        return new WikiParameters(paramsList);
    }
}
