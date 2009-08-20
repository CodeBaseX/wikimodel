/**
 * 
 */
package org.wikimodel.template.impl;

public interface ITemplateNodeManager<N> {

    N getFirstChild(N node);

    N getNextSibling(N node);

}