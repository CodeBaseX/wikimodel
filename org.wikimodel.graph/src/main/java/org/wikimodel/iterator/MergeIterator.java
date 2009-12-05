/*******************************************************************************
 * Copyright (c) 2005,2007 Cognium Systems SA and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Apache Public License v2.0 which accompanies this distribution,
 * and is available at http://opensource.org/licenses/apache2.0.php
 * <br />
 * Contributors:
 * Cognium Systems SA - initial API and implementation
 ******************************************************************************/
package org.wikimodel.iterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used to merge iteration results and returns them in the order.
 * By default this class considers that all elements returned by iterators
 * implement the {@link Comparable} interface. To change this behavior users can
 * overload the method {@link #compareEntries(Object, Object)}.
 * <p>
 * This class can be very useful when it is required to merge iteration results
 * of many iterators over ordered sets and return them as a single ordered
 * iterator.
 * </p>
 * 
 * <pre>
 * // Example of usage:
 * Iterator it1 = ...; // the first ordered iterator
 * Iterator it2 = ...; // the second ordered iterator
 * MergeIterator merge = new MergeIterator(new Iterator[]{it1, it2});
 * while (merge.hasNext()) {
 *     System.out.println(merge.next());
 * }
 * </pre>
 * 
 * @author kotelnikov
 */
public class MergeIterator<T> extends ShiftIterator<T> {

    private class Slot extends ShiftIterator<T> implements Comparable<Slot> {

        private Iterator<T> fIterator;

        /**
         * @param arrayElement
         */
        public Slot(Iterator<T> arrayElement) {
            fIterator = arrayElement;
        }

        /**
         * @see java.lang.Comparable#compareTo(Object)
         */
        public int compareTo(Slot s) {
            if (s == this)
                return 0;
            if (fObject == s.fObject)
                return 0;
            return compareEntries(fObject, s.fObject);
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            Slot s = (Slot) obj;
            return compareTo(s) == 0;
        }

        /**
         * @see org.semanticdesktop.common.iterator.ShiftIterator#shiftItem()
         */
        @Override
        protected T shiftItem() {
            return fIterator.hasNext() ? fIterator.next() : null;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "" + fObject;
        }

    }

    private boolean fFilterRepetedItems;

    private List<Slot> fList = new ArrayList<Slot>();

    /**
     * @param filterRepetedItems
     * @param array
     */
    public MergeIterator(boolean filterRepetedItems, Iterator<T>... array) {
        setArray(array);
        fFilterRepetedItems = filterRepetedItems;
    }

    /**
     * @param array
     */
    public MergeIterator(Iterator<T>... array) {
        this(false, array);
    }

    /**
     * Adds a new slot to the internal list
     * 
     * @param slot the slot to add
     */
    private void addSlot(Slot slot) {
        if (slot.hasNext()) {
            int pos = Collections.binarySearch(fList, slot);
            if (pos < 0)
                pos = -(pos + 1);
            fList.add(pos, slot);
        }
    }

    /**
     * Compares the iterated entries between them and returns the result of the
     * comparison. If the result is less then zero then the first item is less
     * then the second one. If the result is more then zero then the first item
     * is greater then the second. If both elements are equal then this method
     * returns 0.
     * 
     * @param first the first element to compare
     * @param second the second element to compare
     * @return comparison result of the given entries
     */
    @SuppressWarnings("unchecked")
    protected int compareEntries(T first, T second) {
        if (first == null || second == null) {
            // Puts all null elements at the end
            return first != null ? -1 : 1;
        }
        return ((Comparable<T>) first).compareTo(second);
    }

    /**
     * Sets the array of objects used as sources of iterated entries.
     * 
     * @param array
     */
    public void setArray(Iterator<T>[] array) {
        fList.clear();
        int len = array != null ? array.length : 0;
        for (int i = 0; i < len; i++) {
            Slot slot = new Slot(array[i]);
            addSlot(slot);
        }
    }

    /**
     * @see org.semanticdesktop.common.iterator.ShiftIterator#shiftItem()
     */
    @Override
    protected T shiftItem() {
        Object prev = fObject;
        while (true) {
            if (fList.isEmpty())
                return null;
            Slot s = fList.remove(0);
            T result = s.next();
            addSlot(s);
            if (!fFilterRepetedItems || result == null || !result.equals(prev))
                return result;
        }
    }
}
