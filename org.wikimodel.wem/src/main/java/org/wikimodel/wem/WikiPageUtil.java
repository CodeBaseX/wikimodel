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
package org.wikimodel.wem;

import java.util.Arrays;

/**
 * This class contains some utility methods used for escaping xml strings as
 * well as for encoding/decoding http parameters.
 * 
 * @author kotelnikov
 */
public class WikiPageUtil {

    /**
     * Reserved symbols - see RFC 2396 (http://www.ietf.org/rfc/rfc2396.txt)
     */
    private static final char[] HTTP_RESERVED_SYMBOLS = {
        ';',
        '/',
        '?',
        ':',
        '@',
        '&',
        '=',
        '+',
        '$',
        ',' };

    /**
     * Unreserved symbols - see RFC 2396 (http://www.ietf.org/rfc/rfc2396.txt)
     */
    private static final char[] HTTP_UNRESERVED_SYMBOLS = {
        '-',
        '_',
        '.',
        '!',
        '~',
        '*',
        '\'',
        '(',
        ')',
        '#' };

    static {
        Arrays.sort(HTTP_RESERVED_SYMBOLS);
        Arrays.sort(HTTP_UNRESERVED_SYMBOLS);
    }

    /**
     * Returns the decoded http string - all special symbols, replaced by
     * replaced by the %[HEX HEX] sequence, where [HEX HEX] is the hexadecimal
     * code of the escaped symbol will be restored to its original characters
     * (see RFC-2616 http://www.w3.org/Protocols/rfc2616/).
     * 
     * @param str the string to decode
     * @return the decoded string.
     */
    public static String decodeHttpParams(String str) {
        if (str == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char ch = array[i];
            if (ch == '%') {
                if (i + 2 >= array.length) {
                    break;
                }
                int val = (array[++i] - '0');
                val <<= 4;
                val |= (array[++i] - '0');
                ch = (char) val;
            }
            buf.append(ch);
        }
        return buf.toString();
    }

    /**
     * Returns the encoded string - all special symbols will be replaced by
     * %[HEX HEX] sequence, where [HEX HEX] is the hexadecimal code of the
     * escaped symbol (see RFC-2616
     * http://www.w3.org/Protocols/rfc2616/rfc2616.html).
     * 
     * @param str the string to encode
     * @return the encoded string.
     */
    public static String encodeHttpParams(String str) {
        if (str == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char ch = array[i];
            if ((ch >= 'a' && ch <= 'z')
                || (ch >= 'A' && ch <= 'Z')
                || (ch >= '0' && ch <= '9')
                || Character.isDigit(ch)
                || Arrays.binarySearch(HTTP_RESERVED_SYMBOLS, ch) >= 0
                || Arrays.binarySearch(HTTP_UNRESERVED_SYMBOLS, ch) >= 0) {
                buf.append(array[i]);
            } else {
                buf.append("%" + Integer.toHexString(array[i]));
            }
        }
        return buf.toString();
    }

    /**
     * Returns the escaped attribute string.
     * 
     * @param str the string to escape
     * @return the escaped string.
     */
    public static String escapeXmlAttribute(String str) {
        return escapeXmlString(str, true);
    }

    /**
     * Returns the escaped string.
     * 
     * @param str the string to escape
     * @return the escaped string.
     */
    public static String escapeXmlString(String str) {
        return escapeXmlString(str, false);
    }

    /**
     * Returns the escaped string.
     * 
     * @param str the string to escape
     * @param escapeQuots if this flag is <code>true</code> then "'" and "\""
     *        symbols also will be escaped
     * @return the escaped string.
     */
    public static String escapeXmlString(String str, boolean escapeQuots) {
        if (str == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '>'
                || array[i] == '&'
                || array[i] == '<'
                || (escapeQuots && (array[i] == '\'' || array[i] == '"'))) {
                buf.append("&#x" + Integer.toHexString(array[i]) + ";");
            } else {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * @param text
     * @return CDATA block corresponding to the given text
     */
    public static String getCDATA(String text) {
        StringBuffer buf = new StringBuffer();
        buf.append("<![CDATA[");
        int startPos = 0;
        while (startPos >= 0 && startPos < text.length()) {
            int id = text.indexOf("]]>", startPos);
            if (id >= 0) {
                buf.append(text.substring(startPos, id));
                buf.append("]]]]><![CDATA[>");
                startPos += id + "]]>".length();
            } else {
                buf.append(text.substring(startPos));
                startPos = -1;
            }
        }
        buf.append("]]>");
        return buf.toString();
    }

    /**
     * Returns <code>true</code> if the given value is a valid XML character.
     * 
     * @param ch the value to check
     * @return <code>true</code> if the given value is a valid XML character.
     * @see http://www.w3.org/TR/xml/#charsets
     */
    public static boolean isValidXmlChar(int ch) {
        return (ch == 0x9)
            || (ch == 0xA)
            || (ch == 0xD)
            || (ch >= 0x20 && ch <= 0xD7FF)
            || (ch >= 0xE000 && ch <= 0xFFFD)
            || (ch >= 0x10000 && ch <= 0x10FFFF);
    }

    /**
     * This method checks the given string and returns <code>true</code> if it
     * is a valid XML name
     * 
     * @param tagName the name to check
     * @param colonEnabled if this flag is <code>true</code> then this method
     *        accepts the ':' symbol in the name
     * @return <code>true</code> if the given string is a valid XML name
     * @see http://www.w3.org/TR/xml/#NT-Name
     */
    public static boolean isValidXmlName(String tagName, boolean colonEnabled) {
        boolean valid = false;
        int len = tagName != null ? tagName.length() : 0;
        for (int i = 0; i < len; i++) {
            char ch = tagName.charAt(i);
            if (i == 0) {
                valid = isValidXmlNameStartChar(ch, colonEnabled);
            } else {
                valid = isValidXmlNameChar(ch, colonEnabled);
            }
            if (!valid) {
                break;
            }
        }
        return valid;
    }

    /**
     * Returns <code>true</code> if the given value is a valid XML name
     * character.
     * 
     * @param ch the character to check
     * @param colonEnabled if this flag is <code>true</code> then this method
     *        accepts the ':' symbol.
     * @return <code>true</code> if the given value is a valid XML name
     *         character
     * @see http://www.w3.org/TR/xml/#NT-NameChar
     */
    public static boolean isValidXmlNameChar(char ch, boolean colonEnabled) {
        return isValidXmlNameStartChar(ch, colonEnabled)
            || (ch == '-')
            || (ch == '.')
            || (ch >= '0' && ch <= '9')
            || (ch == 0xB7)
            || (ch >= 0x0300 && ch <= 0x036F)
            || (ch >= 0x203F && ch <= 0x2040);
    }

    /**
     * Returns <code>true</code> if the given value is a valid first character
     * of an XML name.
     * 
     * @param ch the character to check
     * @param colonEnabled if this flag is <code>true</code> then this method
     *        accepts the ':' symbol.
     * @return <code>true</code> if the given value is a valid first character
     *         for an XML name
     * @see http://www.w3.org/TR/xml/#NT-NameStartChar
     */
    public static boolean isValidXmlNameStartChar(char ch, boolean colonEnabled) {
        if (ch == ':') {
            return colonEnabled;
        }
        return (ch >= 'A' && ch <= 'Z')
            || ch == '_'
            || (ch >= 'a' && ch <= 'z')
            || (ch >= 0xC0 && ch <= 0xD6)
            || (ch >= 0xD8 && ch <= 0xF6)
            || (ch >= 0xF8 && ch <= 0x2FF)
            || (ch >= 0x370 && ch <= 0x37D)
            || (ch >= 0x37F && ch <= 0x1FFF)
            || (ch >= 0x200C && ch <= 0x200D)
            || (ch >= 0x2070 && ch <= 0x218F)
            || (ch >= 0x2C00 && ch <= 0x2FEF)
            || (ch >= 0x3001 && ch <= 0xD7FF)
            || (ch >= 0xF900 && ch <= 0xFDCF)
            || (ch >= 0xFDF0 && ch <= 0xFFFD)
            || (ch >= 0x10000 && ch <= 0xEFFFF);
    }
}
