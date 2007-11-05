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

import java.util.Iterator;

/**
 * Shift iterator returns objects until the returned objects are not
 * <code>null</code>.
 * 
 * @author kotelnikov
 */
public abstract class ShiftIterator implements Iterator {

    private boolean fDone;

    protected Object fObject;

    /**
     * Constructor for ShiftIterator.
     */
    public ShiftIterator() {
        this(null);
    }

    /**
     * Constructor for ShiftIterator.
     * 
     * @param firstObject the first object returned by this iterator item source
     */
    public ShiftIterator(Object firstObject) {
        reset(firstObject);
    }

    /**
     * @return the current object of this iterator
     */
    public Object getObject() {
        return fObject;
    }

    /**
     * @return <code>true</code> if there is at least one object to return.
     */
    public boolean hasNext() {
        return step(true);
    }

    /**
     * Returns the next object.
     * 
     * @return the next object.
     */
    public Object next() {
        return step(false) ? getObject() : null;
    }

    /**
     * @throws UnsupportedOperationException - this is an unallowed operation.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Method reset.
     */
    public void reset() {
        reset(null);
    }

    /**
     * Method reset.
     * 
     * @param object
     */
    public void reset(Object object) {
        fObject = object;
        fDone = (fObject != null);
    }

    protected abstract Object shiftItem();

    /**
     * Go to the next node.
     * 
     * @param result
     * @return true if a bew object was successfully loaded
     */
    private boolean step(boolean result) {
        if (!fDone) {
            fObject = shiftItem();
        }
        fDone = result;
        return (fObject != null);
    }

}
