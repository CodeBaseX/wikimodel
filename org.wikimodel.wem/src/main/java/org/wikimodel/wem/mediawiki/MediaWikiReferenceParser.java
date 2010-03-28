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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.WikiReferenceParser;

/**
 * @author kotelnikov
 */
public class MediaWikiReferenceParser extends WikiReferenceParser
{

    private static final List<String> format = Arrays.asList("border", "frame", "thumb", "frameless");

    private static final List<String> align = Arrays.asList("left", "right", "center", "none");

    private static final List<String> valign =
        Arrays.asList("baseline", "sub", "super", "top", "text-top", "middle", "bottom", "text-bottom");

    public WikiReference parse(String str)
    {

        WikiReference wikiReference;

        // Piped Internal Link
        // [[Main Page|different text]]
        // [[File:image.png|thumb|250px|center|Image Caption]]
        if (str.contains("|") && !str.endsWith("|")) {
            String reference = str.trim().substring(0, str.indexOf('|'));
            String label = str.trim().substring(str.indexOf('|') + 1);
            int pipeCount = str.replaceAll("[^|]", "").length();
            if ((reference.toLowerCase().startsWith("file:") && pipeCount > 1)
                || (reference.toLowerCase().startsWith("image:") && pipeCount > 0)) {
                WikiParameters params = this.generateImageParams(label);
                reference = reference.trim().substring(reference.indexOf(":")+1);
                wikiReference = new WikiReference(reference, params);
            } else {
                return new WikiReference(reference, label);
            }
        }

        // External link with label
        // Case: [http://mediawiki.org MediaWiki]
        else if (-1 != str.trim().indexOf(' ') && (str.contains("://") || str.contains("mailto:"))) {
            String link = str.substring(0, str.indexOf(' ')).trim();
            String label = str.substring(str.indexOf(' ') + 1).trim();
            wikiReference = new WikiReference(link, label);
        } else {
            wikiReference = new WikiReference(str.trim());
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
                paramsList.add(new WikiParameter(p[0], p[1]));
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
