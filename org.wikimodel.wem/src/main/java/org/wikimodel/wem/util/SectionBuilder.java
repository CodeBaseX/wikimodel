/**
 * 
 */
package org.wikimodel.wem.util;

import java.util.ArrayList;
import java.util.List;

import org.wikimodel.wem.util.AbstractListBuilder.IPos;

/**
 * @author kotelnikov
 */
public class SectionBuilder<T> {

    protected static class TocEntry<T> implements AbstractListBuilder.IPos {

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

    AbstractListBuilder fBuilder = new AbstractListBuilder() {

        @Override
        protected void onBeginRow(IPos n) {
            TocEntry<T> entry = (TocEntry<T>) n;
            fListener.beginSection(entry.fLevel, entry.fData);
        }

        @Override
        protected void onBeginTree(IPos n) {
            TocEntry<T> entry = (TocEntry<T>) n;
            fListener.beginLevel(entry.fLevel, entry.fData);
        }

        @Override
        protected void onEndRow(IPos n) {
            TocEntry<T> entry = (TocEntry<T>) n;
            fListener.endSectionContent(entry.fLevel, entry.fData);
            fListener.endSection(entry.fLevel, entry.fData);
        }

        @Override
        protected void onEndTree(IPos n) {
            TocEntry<T> entry = (TocEntry<T>) n;
            fListener.endLevel(entry.fLevel, entry.fData);
        }
    };

    ISectionListener<T> fListener;

    public SectionBuilder(ISectionListener<T> listener) {
        fListener = listener;
    }

    private void align(int level, T data) {
        List<IPos> row = new ArrayList<IPos>();
        if (level > 0) {
            row.add(new TocEntry<T>(fDocLevel, level, data));
        }
        fBuilder.doAlign(row);
    }

    public void beginDocument() {
        fDocLevel++;
    }

    public void beginHeader(int level, T data) {
        align(level, data);
        TocEntry<T> entry = (TocEntry<T>) fBuilder.getPeek();
        fListener.beginSectionHeader(entry.fLevel, entry.fData);
    }

    public void close() {
        fDocLevel = 0;
        align(0, null);
    }

    public void endDocument() {
        fDocLevel--;
    }

    public void endHeader() {
        TocEntry<T> entry = (TocEntry<T>) fBuilder.getPeek();
        fListener.endSectionHeader(entry.fLevel, entry.fData);
        fListener.beginSectionContent(entry.fLevel, entry.fData);
    }
}