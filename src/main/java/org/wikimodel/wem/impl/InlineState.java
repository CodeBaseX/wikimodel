/**
 * 
 */
package org.wikimodel.wem.impl;

/**
 * @author kotelnikov
 */
public class InlineState {

    public final static int BEGIN = inc();

    public final static int BEGIN_FORMAT = inc();

    public final static int ESCAPE = inc();

    public final static int EXTENSION = inc();

    private static int fCounter;

    public final static int IMAGE = inc();

    public final static int LINE_BREAK = inc();

    public final static int MACRO = inc();

    public final static int NEW_LINE = inc();

    public final static int REFERENCE = inc();

    public final static int SPACE = inc();

    public final static int SPECIAL_SYMBOL = inc();

    public final static int VERBATIM = inc();

    public final static int WORD = inc();

    private static int inc() {
        fCounter++;
        return 1 << fCounter;
    }

    private int fState = BEGIN_FORMAT;

    public boolean check(int mask) {
        return (fState & mask) == mask;
    }

    public int get() {
        return fState;
    }

    public void set(int state) {
        fState = state;
    }

}
