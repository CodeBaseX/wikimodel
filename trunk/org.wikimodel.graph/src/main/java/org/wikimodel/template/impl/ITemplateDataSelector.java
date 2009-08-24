/**
 * 
 */
package org.wikimodel.template.impl;

/**
 * @author kotelnikov
 */
public interface ITemplateDataSelector<N> {

    /**
     * @param node the template node corresponding to the data in the given data
     *        array
     * @param data the parent data array
     * @param index the current position in the parent data array
     * @return an array of data objects associated with the specified node
     */
    Object[] selectData(N node, Object[] data, int index);

}
