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

import java.util.ArrayList;
import java.util.List;

/**
 * This is an internal utility class used as a context to keep in memory the
 * current state of parsed trees (list items).
 * 
 * @author MikhailKotelnikov
 */
public final class TreeBuilder {

    /**
     * This interface identifies position of elements in rows.
     * 
     * @author MikhailKotelnikov
     */
    public interface IPos {

        /**
         * @param pos
         * @return <code>true</code> if the underlying data in both positions
         *         are the same
         */
        boolean equalsData(IPos pos);

        /**
         * @return the position of the node
         */
        int getPos();
    }

    public interface ITreeListener {

        void onBeginRow(IPos n);

        void onBeginTree(IPos n);

        void onEndRow(IPos n);

        void onEndTree(IPos n);
    }

    private static void addTail(
        ITreeListener listener,
        List<IPos> firstArray,
        List<IPos> secondArray,
        int secondPos,
        boolean openTree) {
        IPos n = getNode(secondArray, secondPos);
        if (n == null)
            return;
        if (openTree)
            listener.onBeginTree(n);
        listener.onBeginRow(n);
        firstArray.add(n);
        addTail(listener, firstArray, secondArray, secondPos + 1, true);
    }

    private static void doAlign(
        ITreeListener listener,
        List<IPos> firstArray,
        List<IPos> secondArray,
        boolean expand) {
        boolean newTree = true;
        int f;
        int s;
        int firstLen = firstArray.size();
        int secondLen = secondArray.size();
        for (f = 0, s = 0; f < firstLen && s < secondLen; f++) {
            IPos first = firstArray.get(f);
            IPos second = secondArray.get(s);
            int firstPos = first.getPos();
            int secondPos = second.getPos();
            if (firstPos >= secondPos) {
                if (!first.equalsData(second)) {
                    break;
                } else if (s == secondLen - 1) {
                    newTree = false;
                    break;
                }
                s++;
            }
        }
        removeTail(listener, firstArray, f, newTree);
        if (expand) {
            addTail(listener, firstArray, secondArray, s, newTree);
        }
    }

    private static IPos getNode(List<IPos> list, int pos) {
        return pos < 0 || pos >= list.size() ? null : list.get(pos);
    }

    private static void removeTail(
        ITreeListener listener,
        List<IPos> array,
        int pos,
        boolean closeTree) {
        IPos node = getNode(array, pos);
        if (node == null)
            return;
        removeTail(listener, array, pos + 1, true);
        listener.onEndRow(node);
        if (closeTree)
            listener.onEndTree(node);
        array.remove(pos);
    }

    /**
     *
     */
    public List<IPos> fList = new ArrayList<IPos>();

    private ITreeListener fListener;

    /**
     * 
     */
    public TreeBuilder(ITreeListener listener) {
        super();
        fListener = listener;
    }

    public void align(IPos pos) {
        List<IPos> list = new ArrayList<IPos>();
        if (pos != null)
            list.add(pos);
        align(list);
    }

    public void align(List<IPos> row) {
        doAlign(fListener, fList, row, true);
    }

    public IPos getPeek() {
        return !fList.isEmpty() ? fList.get(fList.size() - 1) : null;
    }

    public void trim(IPos pos) {
        List<IPos> list = new ArrayList<IPos>();
        if (pos != null)
            list.add(pos);
        trim(list);
    }

    public void trim(List<IPos> row) {
        doAlign(fListener, fList, row, false);
    }

}
