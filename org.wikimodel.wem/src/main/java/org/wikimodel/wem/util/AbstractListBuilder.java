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
public abstract class AbstractListBuilder {

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

    /**
     *
     */
    public List<IPos> fList = new ArrayList<IPos>();

    /**
     * 
     */
    public AbstractListBuilder() {
        super();
    }

    private void addTail(List<IPos> list, int pos, boolean openTree) {
        IPos n = getNode(list, pos);
        if (n == null)
            return;
        if (openTree)
            onBeginTree(n);
        onBeginRow(n);
        fList.add(n);
        addTail(list, pos + 1, true);
    }

    public void doAlign(List<IPos> row) {
        boolean newTree = true;
        int f;
        int s;
        int firstLen = fList.size();
        int secondLen = row.size();
        for (f = 0, s = 0; f < firstLen && s < secondLen; f++) {
            IPos first = fList.get(f);
            IPos second = row.get(s);
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
        removeTail(Math.max(0, f), newTree);
        addTail(row, s, newTree);
    }

    private IPos getNode(List<IPos> list, int pos) {
        return pos < 0 || pos >= list.size() ? null : list.get(pos);
    }

    protected abstract void onBeginRow(IPos n);

    protected abstract void onBeginTree(IPos n);

    protected abstract void onEndRow(IPos n);

    protected abstract void onEndTree(IPos n);

    private void removeTail(int pos, boolean closeTree) {
        IPos node = getNode(fList, pos);
        if (node == null)
            return;
        removeTail(pos + 1, true);
        onEndRow(node);
        if (closeTree)
            onEndTree(node);
        fList.remove(pos);
    }

}
