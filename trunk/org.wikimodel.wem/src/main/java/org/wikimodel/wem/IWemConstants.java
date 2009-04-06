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

/**
 * This interface contains main styles used to define text formatting. Note that
 * individual parsers can extends this set by adding new styles.
 * 
 * @author kotelnikov
 */
public interface IWemConstants {

    /**
     * Bigger font
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_big"
     */
    WikiStyle BIG = new WikiStyle("big");

    /**
     * Inline citation.
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_cite"
     */
    WikiStyle CITE = new WikiStyle("cite");

    /**
     * Program code.
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_code"
     */
    WikiStyle CODE = new WikiStyle("code");

    /**
     * Deleted Text
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_del"
     */
    WikiStyle DEL = new WikiStyle("del");

    /**
     * Emphasis (should be used as a replacement for the "i" element).
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_em"
     */
    WikiStyle EM = new WikiStyle("em");

    /**
     * Inserted Text
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_ins"
     */
    WikiStyle INS = new WikiStyle("ins");

    /**
     * FIXME: there is no such a symbol. Should be replaced by the {@link #TT}
     * element.
     */
    WikiStyle MONO = new WikiStyle("mono");

    /**
     * References... FIXME: check what does it mean... I (kotelnikov) did not
     * found any references on such an HTML element. This style should be
     * removed or replaced by something else.
     */
    WikiStyle REF = new WikiStyle("ref");

    /**
     *Smaller font.
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_small"
     */
    WikiStyle SMALL = new WikiStyle("small");

    /**
     * Strike-through
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_strike"
     */
    WikiStyle STRIKE = new WikiStyle("strike");

    /**
     * Strong emphasis.
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_strong"
     */
    WikiStyle STRONG = new WikiStyle("strong");

    /**
     * Subscript
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_sub"
     */
    WikiStyle SUB = new WikiStyle("sub");

    /**
     * Superscript
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_sup"
     */
    WikiStyle SUP = new WikiStyle("sup");

    /**
     * Fixed pitch font.
     * 
     * @see "http://www.w3.org/TR/xhtml1/dtds.html#dtdentry_xhtml1-strict.dtd_tt"
     */
    WikiStyle TT = new WikiStyle("tt");

}
