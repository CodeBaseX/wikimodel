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
package org.wikimodel.wem.xwiki;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wikimodel.wem.xhtml.XhtmlCharacterType;
import org.wikimodel.wem.xhtml.XhtmlCharacter;
import org.wikimodel.wem.xhtml.XhtmlEscapeHandler;
import org.wikimodel.wem.xhtml.handler.TagHandler;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

/**
 * Escape characters that would be confused for XWiki wiki syntax if they were not escaped.
 *  
 * @author Vincent Massol
 */
public class XWikiXhtmlEscapeHandler implements XhtmlEscapeHandler
{
    private static final Pattern LIST_PATTERN = Pattern.compile("\\p{Blank}*((\\*+[:;]*)|([1*]+\\.[:;]*)|([:;]+))\\p{Blank}+");
    
    private static final Pattern HEADER_PATTERN = Pattern.compile("\\p{Blank}*=+");

    private static final Pattern MACRO_PATTERN = Pattern.compile("\\{\\{[^\\t\\n\\r !\"#\\$%&'\\(\\)\\*\\+,-\\.\\/\\:;<=>\\?@\\[\\\\\\]^_`\\{\\|\\}\\~]"); 
    
    private List<String> fReservedKeywords = Arrays.asList("**", "//", "##", "--", "__", "^^", ",,");
    
    public void initialize(Map<String, Object> context)
    {
        context.put("isPotentialList", true);
        context.put("isPotentialHeader", true);
        context.put("isStillInHeader", false);
        context.put("buffer", new StringBuilder());
    }

    /**
     * {@inheritDoc}
     * @see XhtmlEscapeHandler#handleCharacter(XhtmlCharacter, Stack, TagContext, Map)
     */
    public XhtmlCharacter handleCharacter(XhtmlCharacter current, Stack<XhtmlCharacter> characters, TagContext tagContext, Map<String, Object> context)
    {
        XhtmlCharacter result = current;
        String currentTag = getTagName(tagContext);
        
        StringBuilder buffer = (StringBuilder) context.get("buffer");
        buffer.append(current.getCharacter());
        
        // Escape the escape symbol
        if (current.getCharacter() == '~') {
            result.setType(XhtmlCharacterType.ESCAPED);
            context.put("isPotentialList", false);
            context.put("isPotentialHeader", false);
            return result;
        }
        
        // Escape = symbols when in a header (since it would be confused for closing header symbols) 
        // or when starting a line (since otherwise it would be considered as a header syntax in xwiki syntax)
        if (current.getCharacter() == '=') {
            boolean isPotentialHeader = (Boolean) context.get("isPotentialHeader");
            if (currentTag.equals("h1") || currentTag.equals("h2") || currentTag.equals("h3") 
                || currentTag.equals("h4") || currentTag.equals("h5") || currentTag.equals("h6")) {
                result.setType(XhtmlCharacterType.ESCAPED);
                context.put("isPotentialList", false);
                context.put("isPotentialHeader", false);
                return result;
            } else if (isPotentialHeader && currentTag.equals("p")) {
                boolean isStillInHeader = (Boolean) context.get("isStillInHeader");
                if (isStillInHeader) {
                    result.setType(XhtmlCharacterType.ESCAPED);
                } else {
                    // Check if the buffer matches a header format
                    Matcher matcher = HEADER_PATTERN.matcher(buffer);
                    if (matcher.matches()) {
                        // Set all chars till the beginning or till the first space as escaped
                        result.setType(XhtmlCharacterType.ESCAPED);
                        for (int i = characters.size() - 1; i > -1; i--) {
                            XhtmlCharacter ch = characters.elementAt(i);
                            if (ch.getCharacter() == ' ') {
                                break;
                            } else {
                                ch.setType(XhtmlCharacterType.ESCAPED);
                            }
                        }
                        context.put("isStillInHeader", true);
                    }
                    context.put("isPotentialList", false);
                }
            }
        } else {
            context.put("isStillInHeader", false);
        }

        // Escape lists
        boolean isPotentialList = (Boolean) context.get("isPotentialList");
        if (isPotentialList && (current.getCharacter() == ' ') && (currentTag.equals("p"))) {
            if (characters.size() > 0) {
                XhtmlCharacter previous = characters.peek();
                if ((previous.getCharacter() == '*') || (previous.getCharacter() == '.') || (previous.getCharacter() == ':') || (previous.getCharacter() == ';')) {
                    // Check if the buffer matches a list format
                    Matcher matcher = LIST_PATTERN.matcher(buffer);
                    if (matcher.matches()) {
                        // Set all chars till the beginning or till the first space as escaped
                        for (int i = characters.size() - 1; i > -1; i--) {
                            XhtmlCharacter ch = characters.elementAt(i);
                            if (ch.getCharacter() == ' ') {
                                break;
                            } else {
                                ch.setType(XhtmlCharacterType.ESCAPED);
                            }
                        }
                    }
                    context.put("isPotentialList", false);
                    context.put("isPotentialHeader", false);
                }
            }            
        }
        
        if (characters.size() > 0) {

            // Only escape "[[" if there's a matching "]]".
            if (current.getCharacter() == ']' && characters.peek().getCharacter() == ']') {
                // Look for "[["
                int pos = buffer.indexOf("[[");
                if (pos > -1) {
                    // We need to escape the "[[" characters
                    characters.get(pos).setType(XhtmlCharacterType.ESCAPED);
                    characters.get(pos + 1).setType(XhtmlCharacterType.ESCAPED);
                }
            }

            // Only escape "{{" if there's a matching "}}" and if there's no special character just after the "{{"
            if (current.getCharacter() == '}' && characters.peek().getCharacter() == '}') {
                // Look for "{{" followed by a non special character
                Matcher matcher = MACRO_PATTERN.matcher(buffer);
                while (matcher.find()) {
                    // We need to escape the "{{" characters
                    int pos = matcher.start();
                    characters.get(pos).setType(XhtmlCharacterType.ESCAPED);
                    characters.get(pos + 1).setType(XhtmlCharacterType.ESCAPED);
                }
            }            
            
            // Escape all reserved keywords
            XhtmlCharacter previous = characters.peek();
            if (fReservedKeywords.contains("" + previous.getCharacter() + current.getCharacter())) {
                previous.setType(XhtmlCharacterType.ESCAPED);
                result.setType(XhtmlCharacterType.ESCAPED);
                context.put("isPotentialList", false);
                context.put("isPotentialHeader", false);
                return result;
            }

        }
        
        return result;
    }
    
    protected String getTagName(TagContext tagContext)
    {
        // In order to find the HTML tag being handled we need to find the first non null handler
        // We also ignore SPAN tags since we want to handle their content as normal content.
        TagContext context = tagContext;
        TagHandler handler = context.fHandler;
        while (((handler == null) || context.getName().equalsIgnoreCase("span")) && (context.getParent() != null)) {
            context = context.getParent();
            handler = context.fHandler;
        }
        String tag;
        if (handler == null) {
            // We haven't found a handler. It means we're inside the top element and we assume we're on an implicit paragraph.
            tag = "p";
        } else {
            tag = context.getName().toLowerCase();
        }
        
        return tag;
    }
}
