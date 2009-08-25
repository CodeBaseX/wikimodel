/**
 * 
 */
package org.wikimodel.wem.util;

import java.util.Stack;

/**
 * @author kotelnikov
 */
public class SectionBuilder<T> {

    protected class TocEntry
        implements
        TreeBuilder.IPos<TocEntry>,
        ISectionListener.IPos<T> {

        T fData;

        protected boolean fDoc;

        int fDocLevel;

        int fLevel;

        public TocEntry(int docLevel, int level, T data, boolean doc) {
            fDocLevel = docLevel;
            fLevel = level;
            fData = data;
            fDoc = doc;
        }

        public boolean equalsData(TocEntry pos) {
            return true;
        }

        public T getData() {
            return fData;
        }

        public int getDocumentLevel() {
            return fDocLevel;
        }

        public int getHeaderLevel() {
            return fLevel;
        }

        public int getPos() {
            return fDocLevel * 10 + fLevel + 1;
        }

    }

    TreeBuilder<TocEntry> fBuilder = new TreeBuilder<TocEntry>(
        new TreeBuilder.ITreeListener<TocEntry>() {

            public void onBeginRow(TocEntry n) {
                if (!n.fDoc) {
                    fListener.beginSection(n);
                    fListener.beginSectionHeader(n);
                }
            }

            public void onBeginTree(TocEntry n) {
                if (n.fDoc) {
                    fListener.beginDocument(n);
                }
            }

            public void onEndRow(TocEntry n) {
                if (!n.fDoc) {
                    fListener.endSectionContent(n);
                    fListener.endSection(n);
                }
            }

            public void onEndTree(TocEntry n) {
                if (n.fDoc) {
                    fListener.endDocument(n);
                }
            }

        });

    private Stack<TocEntry> fDocEntries = new Stack<TocEntry>();

    ISectionListener<T> fListener;

    public SectionBuilder(ISectionListener<T> listener) {
        fListener = listener;
    }

    /**
     * @param docLevel
     * @param level
     * @param data
     * @param b
     * @return
     */
    private TocEntry align(int docLevel, int level, T data, boolean doc) {
        TocEntry entry = new TocEntry(docLevel, level, data, doc);
        fBuilder.align(entry);
        return entry;
    }

    public void beginDocument(T data) {
        TocEntry entry = align(getDocLevel() + 1, 0, data, true);
        fDocEntries.push(entry);
    }

    public void beginHeader(int level, T data) {
        int docLevel = getDocLevel();
        align(docLevel, level, data, false);
    }

    public void endDocument() {
        fDocEntries.pop();
        fBuilder.trim(fDocEntries);
    }

    public void endHeader() {
        TocEntry entry = fBuilder.getPeek();
        fListener.endSectionHeader(entry);
        fListener.beginSectionContent(entry);
    }

    /**
     * @return
     */
    private int getDocLevel() {
        return fDocEntries.size();
    }

}