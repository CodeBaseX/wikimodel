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

import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;

import junit.framework.TestCase;

/**
 * @author mkirst(at portolancs dot com)
 */
public class MediaWikiReferenceParserTest extends TestCase {

    private MediaWikiReferenceParser clazz;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        clazz = new MediaWikiReferenceParser();
    }

    public void testParseSimpleLinks() {
        WikiReference actual;

        actual = clazz.parse("Image:foo.png");
        assertEquals("foo.png", actual.getLink());

        actual = clazz.parse("image:foo.png");
        assertEquals("foo.png", actual.getLink());

        actual = clazz.parse("File:bar.png");
        assertEquals("bar.png", actual.getLink());

        actual = clazz.parse("file:bar.png");
        assertEquals("bar.png", actual.getLink());
    }

    public void testParseImageParams() {
        WikiReference actual;
        actual = clazz.parse("Image:image.png|thumb|250px|center|Image Caption");
        assertEquals("image.png", actual.getLink());
        assertEquals("Image Caption", actual.getLabel());
        
        final WikiParameters parameters = actual.getParameters();
        
        assertNotNull(parameters.getParameter("width"));
        assertEquals("250px", parameters.getParameter("width").getValue());
        
        assertNotNull(parameters.getParameter("alt"));
        assertEquals("Image Caption", parameters.getParameter("alt").getValue());
        
        assertNotNull(parameters.getParameter("align"));
        assertEquals("center", parameters.getParameter("align").getValue());
        
        assertNotNull(parameters.getParameter("format"));
        assertEquals("thumb", parameters.getParameter("format").getValue());
    }
    
    public void testParseExternalLinks() {
        WikiReference actual;

        actual = clazz.parse("http://code.google.com/p/wikimodel/");
        assertEquals("http://code.google.com/p/wikimodel/", actual.getLink());
        
        actual = clazz.parse("http://code.google.com/p/wikimodel/ Official site");
        assertEquals("http://code.google.com/p/wikimodel/", actual.getLink());
        assertEquals("Official site", actual.getLabel());
    }

    public void testParseInternalLinks() {
        WikiReference actual;

        actual = clazz.parse("Main Page");
        assertEquals("Main Page", actual.getLink());
        
        actual = clazz.parse("Main Page|Different Text");
        assertEquals("Main Page", actual.getLink());
        assertEquals("Different Text", actual.getLabel());
        
        actual = clazz.parse("Main Page|");
        assertEquals("Main Page", actual.getLink());
        assertEquals("", actual.getLabel());
    }
    
    /**
     * @see http://code.google.com/p/wikimodel/issues/attachmentText?id=184
     */
    public void testNamedParameters() {
        WikiReference actual;

        actual = clazz.parse("File:example.jpg|link=Main Page|caption");
        assertEquals("example.jpg", actual.getLink());
        assertEquals("Main Page", actual.getParameters().getParameter("link").getValue());
        
        actual = clazz.parse("File:example.jpg|link=|caption");
        assertEquals("example.jpg", actual.getLink());
        assertEquals("", actual.getParameters().getParameter("link").getValue());
    }
}
