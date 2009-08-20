/**
 * 
 */
package org.wikimodel.template.impl;


/**
 * @author kotelnikov
 */
public interface ITemplateDataSelector<N> {

    Object[] selectData(N node, Object data);

}
