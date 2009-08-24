/**
 * 
 */
package org.wikimodel.template.impl;

public interface ITemplateListener<N> {

    boolean beginNode(N node, Object[] data, int index);

    void endNode(N node, Object[] data, int index);

}