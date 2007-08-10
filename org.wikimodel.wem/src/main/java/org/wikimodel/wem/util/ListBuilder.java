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
package org.wikimodel.wem.util;

/**
 * This is an internal utility class used as a context to keep in memory the
 * current state of parsed trees (list items).
 * 
 * @author kotelnikov
 */
public class ListBuilder {

    protected static class Node {

        protected Node fParent;

        protected char fRowType;

        protected int fShift;

        protected char fTreeType;

        /**
         * @param shift
         * @param treeType
         * @param rowType
         */
        protected Node(int shift, char treeType, char rowType) {
            fShift = shift;
            fTreeType = treeType;
            fRowType = rowType;
        }

        /**
         * @return the depth of this element
         */
        public int getDepth() {
            int depth = 0;
            Node node = this;
            while (node != null) {
                node = node.fParent;
                depth++;
            }
            return depth;
        }

        /**
         * @return the type of the row
         */
        public char getType() {
            return fTreeType;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            StringBuffer buf = new StringBuffer();
            Node node = this;
            while (node != null) {
                buf.append(node.fTreeType);
                node = node.fParent;
            }
            return buf.toString();
        }
    }

    private IListListener fListener;

    /**
     *
     */
    public Node fNode;

    /**
     * @param listener
     */
    public ListBuilder(IListListener listener) {
        fListener = listener;
    }

    /**
     * @param row
     * @param treeParams
     * @param rowParams the parameters of the row
     */
    public void alignContext(String row) {
        if (row == null || row.length() == 0) {
            removeTail(fNode, true);
            fNode = null;
            return;
        }
        char[] array = row.toCharArray();
        int i = 0;
        Node current = fNode;
        Node prev = null;
        Node removed = null;
        boolean newTree = false;
        char rowType = '\0';
        for (i = trimEol(array, i); i < array.length; i++) {
            i = getNextCharPos(array, i);
            if (i >= array.length)
                break;
            removed = null;
            while (current != null && current.fShift < i) {
                prev = current;
                current = current.fParent;
            }
            rowType = array[i];
            char treeType = getTreeType(rowType);
            if (current == null || current.fTreeType != treeType) {
                newTree = true;
                current = newNode(i, treeType, rowType);
                if (prev == null) {
                    removeTail(fNode, true);
                    fNode = current;
                } else {
                    removeTail(prev, false);
                    prev.fParent = current;
                }
                fListener.beginTree(treeType);
                current.fRowType = rowType;
                fListener.beginRow(current.fRowType);
            } else {
                removed = current;
                current.fShift = i;
            }
        }
        removeTail(removed, false);
        if (!newTree) {
            fListener.endRow(current.fRowType);
            current.fRowType = rowType;
            fListener.beginRow(current.fRowType);
        }
    }

    /**
     * @return the type of the current topmost row
     */
    public char getCurrentRowType() {
        return fNode != null ? fNode.fTreeType : ' ';
    }

    /**
     * @return the depth of this context
     */
    public int getDepth() {
        return fNode != null ? fNode.getDepth() : 0;
    }

    private int getNextCharPos(char[] array, int i) {
        for (; i < array.length && array[i] == ' '; i++) {
            //
        }
        return i;
    }

    /**
     * @param rowType the type of the row
     * @return the type of the tree corresponding to the given row type
     */
    protected char getTreeType(char rowType) {
        return rowType;
    }

    /**
     * @param shift the current shift
     * @param treeType
     * @param rowType
     * @return a new row info object
     */
    protected Node newNode(int shift, char treeType, char rowType) {
        return new Node(shift, treeType, rowType);
    }

    private void removeTail(Node row, boolean endRow) {
        if (row == null)
            return;
        removeTail(row.fParent, true);
        if (endRow) {
            fListener.endRow(row.fRowType);
            fListener.endTree(row.fTreeType);
        }
        row.fParent = null;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return fNode != null ? fNode.toString() : "";
    }

    private int trimEol(char[] array, int i) {
        for (i = 0; i < array.length && (array[i] == '\n' || array[i] == '\r'); i++) {
            //
        }
        return i;
    }

}
