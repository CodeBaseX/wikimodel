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
package org.wikimodel.wem.util.tmp;

import java.util.ArrayList;
import java.util.List;

import org.wikimodel.wem.util.IListListener;

/**
 * This is an internal utility class used as a context to keep in memory the
 * current state of parsed trees (list items).
 * 
 * @author kotelnikov
 */
public class ListBuilder1 {

    private static class CharPos {

        public final int pos;

        public final char rowChar;

        public final char treeChar;

        public CharPos(char treeChar, char rowChar, int pos) {
            this.treeChar = treeChar;
            this.rowChar = rowChar;
            this.pos = pos;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof CharPos))
                return false;
            CharPos p = (CharPos) obj;
            return p.pos == pos && p.treeChar == treeChar;
        }

    }

    private TreeBuilder1<CharPos> fBuilder = new TreeBuilder1<CharPos>(
        new TreeBuilder1.ITreeBuilderListener<CharPos>() {

            public void beginItem(int depth, CharPos data) {
                fListener.beginRow(data.treeChar, data.rowChar);
            }

            public void beginLevel(int depth, CharPos prev) {
                fListener.beginTree(prev.treeChar);
            }

            public void endItem(int depth, CharPos data) {
                fListener.endRow(data.treeChar, data.rowChar);
            }

            public void endLevel(int i, CharPos prev) {
                fListener.endTree(prev.treeChar);
            }
        });

    private IListListener fListener;

    private List<CharPos> fPrevRow;

    /**
     * @param listener
     */
    public ListBuilder1(IListListener listener) {
        fListener = listener;
    }

    /**
     * @param row
     * @param treeParams
     * @param rowParams the parameters of the row
     */
    public void alignContext(String row) {
        List<CharPos> list = getCharPositions(row);
        int prevLen = fPrevRow != null ? fPrevRow.size() : 0;
        int currentLen = list.size();
        int len = Math.min(currentLen, prevLen);
        int i;
        for (i = 0; i < len - 1; i++) {
            CharPos prev = fPrevRow.get(i);
            CharPos second = list.get(i);
            if (!prev.equals(second))
                break;
        }
        fBuilder.trim(i, true);
        if (i < currentLen) {
            for (; i < currentLen; i++) {
                CharPos p = list.get(i);
                fBuilder.align(p.pos, p);
            }
        }
        fPrevRow = list;
    }

    public void finish() {
        fBuilder.finish();
    }

    private List<CharPos> getCharPositions(String s) {
        List<CharPos> list = new ArrayList<CharPos>();
        char[] array = s.toCharArray();
        int pos = 0;
        for (int i = 0; i < array.length; i++) {
            char ch = array[i];
            if (ch == '\r' || ch == '\n')
                continue;
            if (!Character.isSpaceChar(ch)) {
                char treeChar = getTreeType(ch);
                list.add(new CharPos(treeChar, ch, pos));
            }
            pos++;
        }
        return list;
    }

    /**
     * @param rowType the type of the row
     * @return the type of the tree corresponding to the given row type
     */
    protected char getTreeType(char rowType) {
        return rowType;
    }

}
