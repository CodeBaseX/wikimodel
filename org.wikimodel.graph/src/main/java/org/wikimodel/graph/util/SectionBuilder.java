/**
 * 
 */
package org.wikimodel.graph.util;

import java.util.ArrayList;
import java.util.List;

import org.wikimodel.graph.INodeWalkerListener;
import org.wikimodel.graph.NodeWalkerListener1;
import org.wikimodel.graph.TreeBuilder;

public class SectionBuilder {

    private TreeBuilder<Integer, RuntimeException> fBuilder = new TreeBuilder<Integer, RuntimeException>() {
        @Override
        protected int getLength(List<Integer> list, int pos) {
            return list.get(pos);
        }
    };

    private int fDepth;

    private ArrayList<Integer> fHeaders = new ArrayList<Integer>();

    private int fLevel;

    private INodeWalkerListener<Integer, RuntimeException> fListener = new NodeWalkerListener1<Integer, RuntimeException>(
        fBuilder) {

        @Override
        protected void onBeginNode(Integer parent, Integer node) {
            fDepth++;
            fLevel = node;
            fSectionListener.beginSection(fDepth, fLevel);
            fSectionListener.beginSectionHeader(fDepth, fLevel);
        }

        @Override
        protected void onBeginSubnodes(Integer parent) {
            fSectionListener.beginLevel(fDepth);
        }

        @Override
        protected void onEndNode(Integer parent, Integer node) {
            fDepth--;
            fLevel = node;
            fSectionListener.endSectionContent(fDepth, fLevel);
            fSectionListener.endSection(fDepth, fLevel);

        }

        @Override
        protected void onEndSubnodes(Integer parent) {
            fSectionListener.endLevel(fDepth);
        }
    };

    ISectionListener fSectionListener;

    public SectionBuilder(ISectionListener listener) {
        fSectionListener = listener;
    }

    public void beginDocument() {
        if (fHeaders.isEmpty()) {
            fSectionListener.beginLevel(0);
        }
        fHeaders.add(0);
    }

    public void beginHeader(int i) {
        fHeaders.set(fHeaders.size() - 1, i);
        fBuilder.align(fHeaders, fListener);
    }

    public void endDocument() {
        fHeaders.remove(fHeaders.size() - 1);
        fBuilder.align(fHeaders, fListener);
        if (fHeaders.isEmpty()) {
            fSectionListener.endLevel(0);
        }
    }

    public void endHeader() {
        fSectionListener.endSectionHeader(fDepth, fLevel);
        fSectionListener.beginSectionContent(fDepth, fLevel);
    }

}