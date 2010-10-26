/**
 * 
 */
package org.wikimodel.wem.util.tmp;

import java.util.Stack;

public class TreeBuilder1<T> {

    public interface ITreeBuilderListener<T> {

        void beginItem(int depth, T data);

        void beginLevel(int depth, T prev);

        void endItem(int depth, T data);

        void endLevel(int i, T prev);

    }

    private static class Slot<T> {

        private final T data;

        private final int val;

        public Slot(int pos, T data) {
            this.val = pos;
            this.data = data;
        }
    }

    private static final int MIN = Integer.MIN_VALUE;

    private boolean fIn;

    private boolean fInitialized;

    private ITreeBuilderListener<T> fListener;

    private T fPrev;

    private Stack<Slot<T>> fStack = new Stack<Slot<T>>();

    public TreeBuilder1(ITreeBuilderListener<T> listener) {
        fListener = listener;
    }

    public void align(int val, T data) {
        trim(val, true);
        if (!fInitialized) {
            fInitialized = true;
            fIn = true;
            val = MIN;
        }
        Slot<T> slot = new Slot<T>(val, data);
        if (fIn) {
            fListener.beginLevel(fStack.size(), data);
        }
        fStack.push(slot);
        fListener.beginItem(fStack.size(), data);
        fPrev = data;
        fIn = true;
    }

    public void finish() {
        trim(MIN, true);
    }

    /**
     * @param val
     */
    public void trim(int val) {
        trim(val, true);
    }

    /**
     * @param val
     */
    public void trim(int val, boolean includeValue) {
        while (!fStack.isEmpty()) {
            Slot<T> peek = fStack.peek();
            if (peek.val < val || peek.val == val && !includeValue)
                break;
            if (!fIn) {
                fListener.endLevel(fStack.size(), fPrev);
            }
            fStack.pop();
            fListener.endItem(fStack.size(), peek.data);
            fIn = false;
            fPrev = peek.data;

            if (peek.val == val)
                break;
        }
        if (fInitialized && fStack.isEmpty()) {
            fListener.endLevel(fStack.size(), fPrev);
        }
    }
}