package org.wikimodel.wem.util;

/**
 * @author kotelnikov
 * @param <T> the type of data managed by this listener
 */
public interface ISectionListener<T> {

    public static interface IPos<T> {

        T getData();

        int getDocumentLevel();

        int getHeaderLevel();
    }

    void beginDocument(IPos<T> pos);

    void beginSection(IPos<T> pos);

    void beginSectionContent(IPos<T> pos);

    void beginSectionHeader(IPos<T> pos);

    void endDocument(IPos<T> pos);

    void endSection(IPos<T> pos);

    void endSectionContent(IPos<T> pos);

    void endSectionHeader(IPos<T> pos);

}