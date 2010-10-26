/*******************************************************************************
 * Copyright (c) 2005,2007 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.test;

import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.xwiki.xwiki20.XWikiReferenceParser;

import junit.framework.TestCase;

/**
 * @author VincentMassol
 * @author tmortagne
 */
public class XWikiReferenceParserTest extends TestCase {
    public void testParseReferenceWhenReferenceOnly() {
        XWikiReferenceParser parser = new XWikiReferenceParser();
        WikiReference reference = parser.parse("reference");
        assertNull(reference.getLabel());
        assertEquals(WikiParameters.EMPTY, reference.getParameters());
        assertEquals("reference", reference.getLink());
    }

    public void testParseReferenceWhenLabelSpecified() {
        XWikiReferenceParser parser = new XWikiReferenceParser();
        WikiReference reference = parser.parse("label>>reference");
        assertEquals("label", reference.getLabel());
        assertEquals(WikiParameters.EMPTY, reference.getParameters());
        assertEquals("reference", reference.getLink());
    }

    public void testParseReferenceWhenParametersSpecified() {

        XWikiReferenceParser parser = new XWikiReferenceParser();
        WikiReference reference = parser
            .parse("reference||param1=value1 param2=value2");
        assertNull(reference.getLabel());
        assertEquals(2, reference.getParameters().getSize());
        assertEquals("value1", reference
            .getParameters()
            .getParameter("param1")
            .getValue());
        assertEquals("value2", reference
            .getParameters()
            .getParameter("param2")
            .getValue());
        assertEquals("reference", reference.getLink());
    }

    public void testParseReferenceWhenLabelAndParametersSpecified() {
        XWikiReferenceParser parser = new XWikiReferenceParser();
        WikiReference reference = parser.parse("label>>reference||param=value");
        assertEquals("label", reference.getLabel());
        assertEquals("value", reference
            .getParameters()
            .getParameter("param")
            .getValue());
        assertEquals("reference", reference.getLink());
    }

    public void testParseReferenceWithGreaterThanSymbolInLabel() {
        XWikiReferenceParser parser = new XWikiReferenceParser();
        WikiReference reference = parser
            .parse("<strong>hello</strong>>>reference");
        assertEquals("<strong>hello</strong>", reference.getLabel());
        assertEquals("reference", reference.getLink());
    }

    public void testParseReferenceWithPipeSymbolInLink() {
        XWikiReferenceParser parser = new XWikiReferenceParser();
        WikiReference reference = parser.parse("reference|||param=value");
        assertEquals("reference|", reference.getLink());
        assertEquals("value", reference
            .getParameters()
            .getParameter("param")
            .getValue());
    }

    public void testParseReferenceWhenLabelAndParametersSpecifiedWithSomeEscaping() {
        XWikiReferenceParser parser = new XWikiReferenceParser();
        WikiReference reference = parser
            .parse("la~>>bel>>refe~||rence||param=value");
        assertEquals("la>>bel", reference.getLabel());
        assertEquals("value", reference
            .getParameters()
            .getParameter("param")
            .getValue());
        assertEquals("refe||rence", reference.getLink());
    }

    public void testParseReferenceWhenLabelAndParametersSpecifiedWithSomeEscapingAndInternalLink() {
        XWikiReferenceParser parser = new XWikiReferenceParser();
        WikiReference reference = parser
            .parse("[[sub~>>label>>subre~||ference||subparam=subvalue]]la~>>bel>>refe~||rence||param=value");
        assertEquals(
            "[[sub~>>label>>subre~||ference||subparam=subvalue]]la>>bel",
            reference.getLabel());
        assertEquals("value", reference
            .getParameters()
            .getParameter("param")
            .getValue());
        assertEquals("refe||rence", reference.getLink());
    }
}
