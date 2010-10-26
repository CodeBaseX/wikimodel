/**
 * 
 */
package org.wikimodel.wem.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * @author kotelnikov
 * @author thomas.mortagne
 */
public class SectionBuilder<T> {

    protected class TocEntry implements TreeBuilder.IPos<TocEntry>,
            ISectionListener.IPos<T> {

        T fData;

        protected boolean fDoc;
        protected boolean fHeader;

        int fDocLevel;

        int fLevel;

        public TocEntry(int docLevel, int level, T data, boolean doc,
                boolean header) {
            fDocLevel = docLevel;
            fLevel = level;
            fData = data;
            fDoc = doc;
            fHeader = header;
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

    Stack<TreeBuilder<TocEntry>> fBuilder = new Stack<TreeBuilder<TocEntry>>();

    private Stack<TocEntry> fDocEntries = new Stack<TocEntry>();

    ISectionListener<T> fListener;

    public SectionBuilder(ISectionListener<T> listener) {
        fListener = listener;
    }

    private void pushBuilder() {
        fBuilder.push(new TreeBuilder<TocEntry>(
            new TreeBuilder.ITreeListener<TocEntry>() {

                public void onBeginRow(TocEntry n) {
                    if (!n.fDoc) {
                        fListener.beginSection(n);
                        if (n.fHeader) {
                            fListener.beginSectionHeader(n);
                        } else {
                            fListener.beginSectionContent(n);
                        }
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
            }));
    }

    private TreeBuilder<TocEntry> popBuilder() {
        return fBuilder.pop();
    }

    /**
     * @param docLevel
     * @param level
     * @param data
     * @param b
     * @return
     */
    private TocEntry align(int docLevel, int level, T data, boolean doc) {
        TocEntry entry = null;
        List<TocEntry> entries = new ArrayList<TocEntry>();
        for (int i = 0; i <= level; ++i) {
            entry = new TocEntry(docLevel, i, data, doc, i == level);
            entries.add(entry);
        }
        fBuilder.peek().align(entries);

        return entry;
    }

    public void beginDocument(T data) {
        pushBuilder();

        TocEntry entry = align(getDocLevel() + 1, 0, data, true);

        fDocEntries.push(entry);
    }

    public void beginHeader(int level, T data) {
        int docLevel = getDocLevel();
        align(docLevel, level, data, false);
    }

    public void endDocument() {
        fDocEntries.pop();

        popBuilder().align(Collections.<TocEntry> emptyList());
    }

    public void endHeader() {
        TocEntry entry = fBuilder.peek().getPeek();
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