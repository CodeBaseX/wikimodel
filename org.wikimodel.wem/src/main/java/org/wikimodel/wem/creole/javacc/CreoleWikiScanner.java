/* Generated By:JavaCC: Do not edit this line. CreoleWikiScanner.java */
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
package org.wikimodel.wem.creole.javacc;

import org.wikimodel.wem.IWikiReferenceParser;
import org.wikimodel.wem.impl.IWikiScannerContext;
import org.wikimodel.wem.IWemConstants;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.WikiStyle;
import org.wikimodel.wem.creole.CreoleWikiReferenceParser;

/**
 * This is the internal wiki page parser generated from the grammar file.
 * 
 * @author kotelnikov
 */
public class CreoleWikiScanner implements CreoleWikiScannerConstants {

    private IWikiScannerContext fContext;

    private IWikiReferenceParser fReferenceParser = new CreoleWikiReferenceParser();

    public void parse(IWikiScannerContext context) throws ParseException {
        fContext = context;
        doParse();
    }

// <getters>
  final public Token getLIST_ITEM() throws ParseException {
                           Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_LIST_ITEM:
      t = jj_consume_token(I_LIST_ITEM);
      break;
    case D_LIST_ITEM:
      t = jj_consume_token(D_LIST_ITEM);
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                             {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getTABLE_ROW() throws ParseException {
                           Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_TABLE_ROW:
      t = jj_consume_token(I_TABLE_ROW);
      break;
    case D_TABLE_ROW:
      t = jj_consume_token(D_TABLE_ROW);
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                             {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getHEADER_BEGIN() throws ParseException {
                              Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_HEADER_BEGIN:
      t = jj_consume_token(I_HEADER_BEGIN);
      break;
    case D_HEADER_BEGIN:
      t = jj_consume_token(D_HEADER_BEGIN);
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                                      {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getVERBATIM_BLOCK() throws ParseException {
                                Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_VERBATIM_BLOCK:
      t = jj_consume_token(I_VERBATIM_BLOCK);
      break;
    case D_VERBATIM_BLOCK:
      t = jj_consume_token(D_VERBATIM_BLOCK);
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                                            {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getHORLINE() throws ParseException {
                         Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_HORLINE:
      t = jj_consume_token(I_HORLINE);
      break;
    case D_HORLINE:
      t = jj_consume_token(D_HORLINE);
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                       {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getVERBATIM_INLINE() throws ParseException {
                                 Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_VERBATIM_INLINE:
      t = jj_consume_token(I_VERBATIM_INLINE);
      break;
    case D_VERBATIM_INLINE:
      t = jj_consume_token(D_VERBATIM_INLINE);
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                                               {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getREFERENCE() throws ParseException {
                           Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_REFERENCE:
      t = jj_consume_token(I_REFERENCE);
      break;
    case D_REFERENCE:
      t = jj_consume_token(D_REFERENCE);
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                             {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getHEADER_END() throws ParseException {
                            Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_HEADER_END:
      t = jj_consume_token(I_HEADER_END);
      break;
    case D_HEADER_END:
      t = jj_consume_token(D_HEADER_END);
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                                {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getTABLE_CELL() throws ParseException {
                            Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_TABLE_CELL:
      t = jj_consume_token(I_TABLE_CELL);
      break;
    case D_TABLE_CELL:
      t = jj_consume_token(D_TABLE_CELL);
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                                {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getBR() throws ParseException {
                    Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_BR:
      t = jj_consume_token(I_BR);
      break;
    case D_BR:
      t = jj_consume_token(D_BR);
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                        {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getFORMAT_SYMBOL() throws ParseException {
                               Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_FORMAT_SYMBOL:
      t = jj_consume_token(I_FORMAT_SYMBOL);
      break;
    case D_FORMAT_SYMBOL:
      t = jj_consume_token(D_FORMAT_SYMBOL);
      break;
    default:
      jj_la1[10] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                                         {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

    // "Standard" tokens. They are the same for all wikis.
  final public Token getURI() throws ParseException {
                     Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_URI:
      t = jj_consume_token(I_URI);
      break;
    case D_URI:
      t = jj_consume_token(D_URI);
      break;
    default:
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                           {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getNL() throws ParseException {
                    Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_NL:
      t = jj_consume_token(I_NL);
      break;
    case D_NL:
      t = jj_consume_token(D_NL);
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                        {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getSPACE() throws ParseException {
                       Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_SPACE:
      t = jj_consume_token(I_SPACE);
      break;
    case D_SPACE:
      t = jj_consume_token(D_SPACE);
      break;
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                 {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getWORD() throws ParseException {
                      Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_WORD:
      t = jj_consume_token(I_WORD);
      break;
    case D_WORD:
      t = jj_consume_token(D_WORD);
      break;
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                              {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

  final public Token getSPECIAL_SYMBOL() throws ParseException {
                                Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_SPECIAL_SYMBOL:
      t = jj_consume_token(I_SPECIAL_SYMBOL);
      break;
    case D_SPECIAL_SYMBOL:
      t = jj_consume_token(D_SPECIAL_SYMBOL);
      break;
    default:
      jj_la1[15] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                                                                                            {if (true) return t;}
    throw new Error("Missing return statement in function");
  }

// </getters>
  final public void doParse() throws ParseException {
    token_source.SwitchTo(token_source.INITIAL_CONTEXT);
        fContext.beginDocument();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case I_LIST_ITEM:
      case I_TABLE_ROW:
      case I_HEADER_BEGIN:
      case I_VERBATIM_BLOCK:
      case I_HORLINE:
      case I_VERBATIM_INLINE:
      case I_REFERENCE:
      case I_HEADER_END:
      case I_TABLE_CELL:
      case I_BR:
      case I_FORMAT_SYMBOL:
      case I_URI:
      case I_NL:
      case I_SPACE:
      case I_WORD:
      case I_SPECIAL_SYMBOL:
      case D_LIST_ITEM:
      case D_TABLE_ROW:
      case D_HEADER_BEGIN:
      case D_VERBATIM_BLOCK:
      case D_HORLINE:
      case D_VERBATIM_INLINE:
      case D_REFERENCE:
      case D_HEADER_END:
      case D_TABLE_CELL:
      case D_BR:
      case D_FORMAT_SYMBOL:
      case D_URI:
      case D_NL:
      case D_SPACE:
      case D_WORD:
      case D_SPECIAL_SYMBOL:
        ;
        break;
      default:
        jj_la1[16] = jj_gen;
        break label_1;
      }
      docElements();
    }
    jj_consume_token(0);
        fContext.endDocument();
  }

  final public void docElements() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_HEADER_BEGIN:
    case D_HEADER_BEGIN:
      header();
      break;
    case I_TABLE_ROW:
    case D_TABLE_ROW:
      table();
      break;
    case I_LIST_ITEM:
    case D_LIST_ITEM:
      list();
      break;
    case I_VERBATIM_BLOCK:
    case D_VERBATIM_BLOCK:
      verbatimBlock();
      break;
    case I_HORLINE:
    case D_HORLINE:
      horline();
      break;
    case I_VERBATIM_INLINE:
    case I_REFERENCE:
    case I_HEADER_END:
    case I_TABLE_CELL:
    case I_BR:
    case I_FORMAT_SYMBOL:
    case I_URI:
    case I_SPACE:
    case I_WORD:
    case I_SPECIAL_SYMBOL:
    case D_VERBATIM_INLINE:
    case D_REFERENCE:
    case D_HEADER_END:
    case D_TABLE_CELL:
    case D_BR:
    case D_FORMAT_SYMBOL:
    case D_URI:
    case D_SPACE:
    case D_WORD:
    case D_SPECIAL_SYMBOL:
      if (jj_2_1(2)) {
        quot();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case I_VERBATIM_INLINE:
        case I_REFERENCE:
        case I_HEADER_END:
        case I_TABLE_CELL:
        case I_BR:
        case I_FORMAT_SYMBOL:
        case I_URI:
        case I_SPACE:
        case I_WORD:
        case I_SPECIAL_SYMBOL:
        case D_VERBATIM_INLINE:
        case D_REFERENCE:
        case D_HEADER_END:
        case D_TABLE_CELL:
        case D_BR:
        case D_FORMAT_SYMBOL:
        case D_URI:
        case D_SPACE:
        case D_WORD:
        case D_SPECIAL_SYMBOL:
          paragraph();
          break;
        default:
          jj_la1[17] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      break;
    case I_NL:
    case D_NL:
      emptyParagraph();
      break;
    default:
      jj_la1[18] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void header() throws ParseException {
   Token t = null;
    t = getHEADER_BEGIN();
        int level = t.image.trim().length();
        fContext.beginHeader(level);
    if (jj_2_2(2)) {
      line();
    } else {
      ;
    }
        fContext.endHeader();
  }

  final public void table() throws ParseException {
   Token t = null;
   String str = "";
        fContext.beginTable();
    label_2:
    while (true) {
      tableRow();
      if (jj_2_3(2)) {
        ;
      } else {
        break label_2;
      }
    }
        fContext.endTable();
  }

  final public void tableRow() throws ParseException {
    Token t = null;
    String str = null;
    t = getTABLE_ROW();
        str = t.image.trim();
        fContext.beginTableRow(str.length() > 1);
        t = null;
    label_3:
    while (true) {
      if (jj_2_4(2)) {
        ;
      } else {
        break label_3;
      }
      block();
    }
        fContext.endTableRow();
  }

  final public void list() throws ParseException {
        fContext.beginList();
    label_4:
    while (true) {
      listItem();
      if (jj_2_5(2)) {
        ;
      } else {
        break label_4;
      }
    }
        fContext.endList();
  }

  final public void listItem() throws ParseException {
    Token t = null;
    t = getLIST_ITEM();
            fContext.beginListItem(t.image.trim());
    if (jj_2_6(2)) {
      line();
    } else {
      ;
    }
            fContext.endListItem();
  }

  final public void block() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case I_VERBATIM_INLINE:
    case I_REFERENCE:
    case I_HEADER_END:
    case I_TABLE_CELL:
    case I_BR:
    case I_FORMAT_SYMBOL:
    case I_URI:
    case I_SPACE:
    case I_WORD:
    case I_SPECIAL_SYMBOL:
    case D_VERBATIM_INLINE:
    case D_REFERENCE:
    case D_HEADER_END:
    case D_TABLE_CELL:
    case D_BR:
    case D_FORMAT_SYMBOL:
    case D_URI:
    case D_SPACE:
    case D_WORD:
    case D_SPECIAL_SYMBOL:
      lines();
      break;
    case I_VERBATIM_BLOCK:
    case D_VERBATIM_BLOCK:
      verbatimBlock();
      break;
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void verbatimBlock() throws ParseException {
    Token t = null;
    t = getVERBATIM_BLOCK();
        String str = t.image.trim();
        str = str.substring(3, str.length() - 3);
        fContext.onVerbatim(str,  false);
  }

  final public void horline() throws ParseException {
    Token t = null;
    t = getHORLINE();
        fContext.onHorizontalLine();
  }

  final public void paragraph() throws ParseException {
    Token t = null;
    String str = "";
        fContext.beginParagraph();
    lines();
        fContext.endParagraph();
  }

  final public void quot() throws ParseException {
    Token t = null;
    int depthCounter = 0;
        fContext.beginQuot();
    quotLine();
    label_5:
    while (true) {
      if (jj_2_7(2)) {
        ;
      } else {
        break label_5;
      }
      newLine();
      quotLine();
    }
        fContext.endQuot();
  }

  final public void quotLine() throws ParseException {
    Token t = null;
    int depthCounter = 0;
    label_6:
    while (true) {
      t = getSPACE();
                                    depthCounter++;
      if (jj_2_8(2)) {
        ;
      } else {
        break label_6;
      }
    }
      fContext.beginQuotLine(depthCounter);
    line();
      fContext.endQuotLine();
  }

  final public void emptyParagraph() throws ParseException {
    getNL();
    label_7:
    while (true) {
      if (jj_2_9(2)) {
        ;
      } else {
        break label_7;
      }
      getNL();
    }
  }

  final public void lines() throws ParseException {
    line();
    label_8:
    while (true) {
      if (jj_2_10(2)) {
        ;
      } else {
        break label_8;
      }
      newLine();
      line();
    }
  }

  final public void newLine() throws ParseException {
    getNL();
        fContext.onNewLine();
  }

  final public void line() throws ParseException {
    Token t = null;
    String str = null;
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case I_BR:
      case D_BR:
        t = getBR();
                fContext.onLineBreak();
        break;
      case I_WORD:
      case D_WORD:
        t = getWORD();
                fContext.onWord(t.image);
        break;
      case I_SPACE:
      case D_SPACE:
        t = getSPACE();
                fContext.onSpace(t.image);
        break;
      case I_SPECIAL_SYMBOL:
      case D_SPECIAL_SYMBOL:
        t = getSPECIAL_SYMBOL();
                fContext.onSpecialSymbol(t.image);
        break;
      case I_FORMAT_SYMBOL:
      case D_FORMAT_SYMBOL:
        t = getFORMAT_SYMBOL();
                str = t.image.trim();
                WikiStyle style = null;
                if ("**".equals(str)) {
                    style  = IWemConstants.STRONG;
                } else if ("//".equals(str)) {
                    style  = IWemConstants.EM;
                } else if ("##".equals(str)) {
                    style = IWemConstants.MONO;
                }
                fContext.onFormat(style);
        break;
      case I_HEADER_END:
      case D_HEADER_END:
        t = getHEADER_END();
                if (!fContext.isInHeader()) {
                    fContext.onSpecialSymbol(t.image);
                }
        break;
      case I_VERBATIM_INLINE:
      case D_VERBATIM_INLINE:
        t = getVERBATIM_INLINE();
                str = t.image.trim();
                str = str.substring(3, str.length() - 3);
                fContext.onVerbatim(str, true);
        break;
      case I_URI:
      case D_URI:
        t = getURI();
                fContext.onReference(t.image.trim());
        break;
      case I_REFERENCE:
      case D_REFERENCE:
        t = getREFERENCE();
                str = t.image.trim();
                str = str.substring(2, str.length() - 2);
                WikiReference ref = fReferenceParser.parse(str);
                fContext.onReference(ref);
        break;
      case I_TABLE_CELL:
      case D_TABLE_CELL:
        t = getTABLE_CELL();
                if (fContext.isInTable()) {
                    str = t.image.trim();
                    fContext.onTableCell(str.length() > 1);
                } else {
                    fContext.onSpecialSymbol(t.image);
                }
        break;
      default:
        jj_la1[20] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      if (jj_2_11(2)) {
        ;
      } else {
        break label_9;
      }
    }
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  final private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  final private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  final private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  final private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  final private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  final private boolean jj_2_8(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(7, xla); }
  }

  final private boolean jj_2_9(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(8, xla); }
  }

  final private boolean jj_2_10(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(9, xla); }
  }

  final private boolean jj_2_11(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_11(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(10, xla); }
  }

  final private boolean jj_3R_21() {
    if (jj_3R_17()) return true;
    return false;
  }

  final private boolean jj_3R_20() {
    if (jj_3R_34()) return true;
    return false;
  }

  final private boolean jj_3_2() {
    if (jj_3R_11()) return true;
    return false;
  }

  final private boolean jj_3R_19() {
    if (jj_3R_33()) return true;
    return false;
  }

  final private boolean jj_3_11() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_19()) {
    jj_scanpos = xsp;
    if (jj_3R_20()) {
    jj_scanpos = xsp;
    if (jj_3R_21()) {
    jj_scanpos = xsp;
    if (jj_3R_22()) {
    jj_scanpos = xsp;
    if (jj_3R_23()) {
    jj_scanpos = xsp;
    if (jj_3R_24()) {
    jj_scanpos = xsp;
    if (jj_3R_25()) {
    jj_scanpos = xsp;
    if (jj_3R_26()) {
    jj_scanpos = xsp;
    if (jj_3R_27()) {
    jj_scanpos = xsp;
    if (jj_3R_28()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_35() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(57)) {
    jj_scanpos = xsp;
    if (jj_scan_token(73)) return true;
    }
    return false;
  }

  final private boolean jj_3R_31() {
    if (jj_3R_43()) return true;
    return false;
  }

  final private boolean jj_3R_11() {
    Token xsp;
    if (jj_3_11()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_11()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_43() {
    if (jj_3R_44()) return true;
    return false;
  }

  final private boolean jj_3R_36() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(52)) {
    jj_scanpos = xsp;
    if (jj_scan_token(68)) return true;
    }
    return false;
  }

  final private boolean jj_3_1() {
    if (jj_3R_10()) return true;
    return false;
  }

  final private boolean jj_3R_30() {
    if (jj_3R_42()) return true;
    return false;
  }

  final private boolean jj_3R_38() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(47)) {
    jj_scanpos = xsp;
    if (jj_scan_token(63)) return true;
    }
    return false;
  }

  final private boolean jj_3R_34() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(56)) {
    jj_scanpos = xsp;
    if (jj_scan_token(72)) return true;
    }
    return false;
  }

  final private boolean jj_3R_17() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(55)) {
    jj_scanpos = xsp;
    if (jj_scan_token(71)) return true;
    }
    return false;
  }

  final private boolean jj_3R_13() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_30()) {
    jj_scanpos = xsp;
    if (jj_3R_31()) return true;
    }
    return false;
  }

  final private boolean jj_3R_15() {
    if (jj_3R_18()) return true;
    return false;
  }

  final private boolean jj_3R_41() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(50)) {
    jj_scanpos = xsp;
    if (jj_scan_token(66)) return true;
    }
    return false;
  }

  final private boolean jj_3R_44() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(45)) {
    jj_scanpos = xsp;
    if (jj_scan_token(61)) return true;
    }
    return false;
  }

  final private boolean jj_3R_37() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(49)) {
    jj_scanpos = xsp;
    if (jj_scan_token(65)) return true;
    }
    return false;
  }

  final private boolean jj_3R_39() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(53)) {
    jj_scanpos = xsp;
    if (jj_scan_token(69)) return true;
    }
    return false;
  }

  final private boolean jj_3R_18() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(54)) {
    jj_scanpos = xsp;
    if (jj_scan_token(70)) return true;
    }
    return false;
  }

  final private boolean jj_3R_40() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(48)) {
    jj_scanpos = xsp;
    if (jj_scan_token(64)) return true;
    }
    return false;
  }

  final private boolean jj_3_6() {
    if (jj_3R_11()) return true;
    return false;
  }

  final private boolean jj_3R_33() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(51)) {
    jj_scanpos = xsp;
    if (jj_scan_token(67)) return true;
    }
    return false;
  }

  final private boolean jj_3_10() {
    if (jj_3R_15()) return true;
    if (jj_3R_11()) return true;
    return false;
  }

  final private boolean jj_3R_29() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(43)) {
    jj_scanpos = xsp;
    if (jj_scan_token(59)) return true;
    }
    return false;
  }

  final private boolean jj_3R_32() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(42)) {
    jj_scanpos = xsp;
    if (jj_scan_token(58)) return true;
    }
    return false;
  }

  final private boolean jj_3_9() {
    if (jj_3R_18()) return true;
    return false;
  }

  final private boolean jj_3R_42() {
    if (jj_3R_11()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_10()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_28() {
    if (jj_3R_41()) return true;
    return false;
  }

  final private boolean jj_3R_14() {
    if (jj_3R_32()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_6()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_27() {
    if (jj_3R_40()) return true;
    return false;
  }

  final private boolean jj_3_5() {
    if (jj_3R_14()) return true;
    return false;
  }

  final private boolean jj_3R_26() {
    if (jj_3R_39()) return true;
    return false;
  }

  final private boolean jj_3_8() {
    if (jj_3R_17()) return true;
    return false;
  }

  final private boolean jj_3R_16() {
    Token xsp;
    if (jj_3_8()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_8()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_11()) return true;
    return false;
  }

  final private boolean jj_3R_25() {
    if (jj_3R_38()) return true;
    return false;
  }

  final private boolean jj_3_4() {
    if (jj_3R_13()) return true;
    return false;
  }

  final private boolean jj_3R_24() {
    if (jj_3R_37()) return true;
    return false;
  }

  final private boolean jj_3_7() {
    if (jj_3R_15()) return true;
    if (jj_3R_16()) return true;
    return false;
  }

  final private boolean jj_3R_12() {
    if (jj_3R_29()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_4()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_10() {
    if (jj_3R_16()) return true;
    return false;
  }

  final private boolean jj_3R_23() {
    if (jj_3R_36()) return true;
    return false;
  }

  final private boolean jj_3_3() {
    if (jj_3R_12()) return true;
    return false;
  }

  final private boolean jj_3R_22() {
    if (jj_3R_35()) return true;
    return false;
  }

  public CreoleWikiScannerTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  final private int[] jj_la1 = new int[21];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static {
      jj_la1_0();
      jj_la1_1();
      jj_la1_2();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }
   private static void jj_la1_1() {
      jj_la1_1 = new int[] {0x4000400,0x8000800,0x10001000,0x20002000,0x40004000,0x80008000,0x10000,0x20000,0x40000,0x80000,0x100000,0x200000,0x400000,0x800000,0x1000000,0x2000000,0xfffffc00,0x83bf8000,0xfffffc00,0xa3bfa000,0x83bf8000,};
   }
   private static void jj_la1_2() {
      jj_la1_2 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x1,0x2,0x4,0x8,0x10,0x20,0x40,0x80,0x100,0x200,0x3ff,0x3bf,0x3ff,0x3bf,0x3bf,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[11];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  public CreoleWikiScanner(java.io.InputStream stream) {
     this(stream, null);
  }
  public CreoleWikiScanner(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new CreoleWikiScannerTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public CreoleWikiScanner(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new CreoleWikiScannerTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public CreoleWikiScanner(CreoleWikiScannerTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(CreoleWikiScannerTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Enumeration e = jj_expentries.elements(); e.hasMoreElements();) {
        int[] oldentry = (int[])(e.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[74];
    for (int i = 0; i < 74; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 21; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 74; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 11; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
            case 7: jj_3_8(); break;
            case 8: jj_3_9(); break;
            case 9: jj_3_10(); break;
            case 10: jj_3_11(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  final private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
