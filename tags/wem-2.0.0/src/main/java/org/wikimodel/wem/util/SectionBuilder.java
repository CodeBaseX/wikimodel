/**
 * 
 */
package org.wikimodel.wem.util;

import org.wikimodel.wem.util.TreeBuilder.IPos;

/**
 * @author kotelnikov
 */
public class SectionBuilder<T> {

    protected static class TocEntry<T> implements TreeBuilder.IPos {

        T fData;

        int fDocLevel;

        int fLevel;

        public TocEntry(int docLevel, int level, T data) {
            fDocLevel = docLevel;
            fLevel = level;
            fData = data;
        }

        public boolean equalsData(IPos pos) {
            return true;
        }

        public int getPos() {
            return fDocLevel * 10 + fLevel;
        }

    }

    static int fDocLevel;

    TreeBuilder fBuilder = new TreeBuilder(new TreeBuilder.ITreeListener() {

        public void onBeginRow(IPos n) {
            TocEntry<T> entry = (TocEntry<T>) n;
            fListener.beginSection(entry.fLevel, entry.fData);
        }

        public void onBeginTree(IPos n) {
            TocEntry<T> entry = (TocEntry<T>) n;
            fListener.beginLevel(entry.fLevel, entry.fData);
        }

        public void onEndRow(IPos n) {
            TocEntry<T> entry = (TocEntry<T>) n;
            fListener.endSectionContent(entry.fLevel, entry.fData);
            fListener.endSection(entry.fLevel, entry.fData);
        }

        public void onEndTree(IPos n) {
            TocEntry<T> entry = (TocEntry<T>) n;
            fListener.endLevel(entry.fLevel, entry.fData);
        }
    });

    ISectionListener<T> fListener;

    public SectionBuilder(ISectionListener<T> listener) {
        fListener = listener;
    }

    public void beginDocument() {
        fDocLevel++;
    }

    public void beginHeader(int level, T data) {
        TocEntry<T> entry = new TocEntry<T>(fDocLevel, level, data);
        fBuilder.align(entry);

        entry = (TocEntry<T>) fBuilder.getPeek();
        fListener.beginSectionHeader(entry.fLevel, entry.fData);
    }

    public void endDocument() {
        TocEntry<T> entry = new TocEntry<T>(fDocLevel, 1, null);
        fBuilder.trim(entry);
        fDocLevel--;
    }

    public void endHeader() {
        TocEntry<T> entry = (TocEntry<T>) fBuilder.getPeek();
        fListener.endSectionHeader(entry.fLevel, entry.fData);
        fListener.beginSectionContent(entry.fLevel, entry.fData);
    }

}