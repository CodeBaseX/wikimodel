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
 * implement {@link Comparable} iterface. To change this behaviour users can
 * overload the method {@link #compareEntries(Object, Object)}.
 * <p>
 * This class can be very useful when it is required to merge iteration results
 * of many iterators over ordered sets and returns them as a single ordered
 * iterator.
 * </p>
 * 
 * <pre>
 * // Example of usage:
 * Iterator it1 = ...; // the first ordered iterator
 * Iterator it2 = ...; // the second ordered iterator
 * OrderedIterator merge = new OrderedIterator(new Iterator[]{it1, it2});
 * while (merge.hasNext()) {
 *     System.out.println(merge.next());
 * }
 * </pre>
 * 
 * @author kotelnikov
 */
public class OrderedIterator extends ShiftIterator {

    private class Slot extends ShiftIterator implements Comparable {

        private Iterator fIterator;

        /**
         * @param arrayElement
         */
        public Slot(Iterator arrayElement) {
            fIterator = arrayElement;
        }

        /**
         * @see java.lang.Comparable#compareTo(Object)
         */
        public int compareTo(Object o) {
            if (o == this)
                return 0;
            Slot s = (Slot) o;
            if (fObject == s.fObject)
                return 0;
            return compareEntries(fObject, s.fObject);
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj) {
            if (!(obj instanceof Slot))
                return false;
            return compareTo(obj) == 0;
        }

        /**
         * @see org.semanticdesktop.common.iterator.ShiftIterator#shiftItem()
         */
        protected Object shiftItem() {
            return fIterator.hasNext() ? fIterator.next() : null;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return "" + fObject;
        }

    }

    private boolean fFilterRepetedItems;

    private List fList = new ArrayList();

    /**
     * 
     *
     */
    public OrderedIterator() {
        this(null, false);
    }

    /**
     * @param array
     */
    public OrderedIterator(Iterator[] array) {
        this(array, false);
    }

    /**
     * @param array
     * @param filterRepetedItems
     */
    public OrderedIterator(Iterator[] array, boolean filterRepetedItems) {
        setArray(array);
        fFilterRepetedItems = filterRepetedItems;
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
     * comparision. If the result is less then zero then the first item is less
     * then the second one. If the result is more then zero then the first item
     * is greater then the second. If both elements are equal then this method
     * returns 0.
     * 
     * @param first the first element to compare
     * @param second the second element to compare
     * @return comparision result of the given entries
     */
    protected int compareEntries(Object first, Object second) {
        if (first == null || second == null) {
            // Puts all null elements at the end
            return first != null ? -1 : 1;
        }
        return ((Comparable) first).compareTo(second);
    }

    /**
     * Sets the array of objects used as sources of iterated entries.
     * 
     * @param array
     */
    public void setArray(Iterator[] array) {
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
    protected Object shiftItem() {
        Object prev = fObject;
        while (true) {
            if (fList.isEmpty())
                return null;
            Slot s = (Slot) fList.remove(0);
            Object result = s.next();
            addSlot(s);
            if (!fFilterRepetedItems || result == null || !result.equals(prev))
                return result;
        }
    }
}
