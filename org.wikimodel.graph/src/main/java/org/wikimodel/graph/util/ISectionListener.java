/**
 * 
 */
package org.wikimodel.graph.util;

public interface ISectionListener {

    /**
     * This method is called to notify about a block of sections with the same
     * depth.
     * 
     * @param depth
     */
    void beginLevel(int depth);

    void beginSection(int depth, int level);

    void beginSectionContent(int depth, int level);

    void beginSectionHeader(int depth, int level);

    void endLevel(int depth);

    void endSection(int depth, int level);

    void endSectionContent(int depth, int level);

    void endSectionHeader(int depth, int level);

}