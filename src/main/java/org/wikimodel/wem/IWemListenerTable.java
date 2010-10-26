package org.wikimodel.wem;

/**
 * This interface re-groups all methods used to notify about tables and their
 * structural elements.
 * 
 * @author kotelnikov
 */
public interface IWemListenerTable {

    /**
     * This method notifies about the beginning of a new table in the document.
     * 
     * @param params table parameters
     * @see #endTable(WikiParameters)
     */
    void beginTable(WikiParameters params);

    /**
     * This method is used to notify about the beginning of a new table cell.
     * 
     * @param tableHead if this flag is <code>true</code> then the reported cell
     *        corresponds to the table head ("th" element); otherwise it should
     *        be considered as a normal table cell ("td" element).
     * @param params parameters of this cell
     * @see #endTableCell(boolean, WikiParameters)
     */
    void beginTableCell(boolean tableHead, WikiParameters params);

    /**
     * This method is used to notify about the beginning of a new table row.
     * 
     * @param params parameters of the row.
     * @see #endTableRow(WikiParameters)
     */
    void beginTableRow(WikiParameters params);

    /**
     * This method notifies about the end of a table in the document.
     * 
     * @param params table parameters
     * @see #beginTable(WikiParameters)
     */
    void endTable(WikiParameters params);

    /**
     * This method is used to notify about the end of a table cell.
     * 
     * @param tableHead if this flag is <code>true</code> then the reported cell
     *        corresponds to the table head ("th" element); otherwise it should
     *        be considered as a normal table cell ("td" element).
     * @param params parameters of this cell
     * @see #beginTableCell(boolean, WikiParameters)
     */
    void endTableCell(boolean tableHead, WikiParameters params);

    /**
     * This method is used to notify about the end of a table row.
     * 
     * @param params parameters of the row.
     * @see #beginTableRow(WikiParameters)
     */
    void endTableRow(WikiParameters params);

    /**
     * Notifies the table caption.
     * 
     * @param str the content of the table caption
     */
    void onTableCaption(String str);

}
