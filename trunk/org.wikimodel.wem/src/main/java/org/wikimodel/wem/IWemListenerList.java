package org.wikimodel.wem;

/**
 * This listener re-groups all hierarchical listener elements like lists
 * (ordered and unordered), definition lists and quotations. This interface
 * contains methods used to notify begin/end of each list as well as begin/end
 * of each list item.
 * 
 * @author kotelnikov
 */
public interface IWemListenerList {

    /**
     * This method is used to notify about a definition description. All
     * definition descriptions are contained in definition lists and can contain
     * text, embedded lists or an embedded document.
     * 
     * @see #endDefinitionDescription()
     */
    void beginDefinitionDescription();

    /**
     * This method is used to notify about a new definition list.
     * 
     * @param params list of parameters for the list
     * @see #endDefinitionList(WikiParameters)
     */
    void beginDefinitionList(WikiParameters params);

    /**
     * A definition term. Definition terms can contain only formatted texts.
     * Note that it is impossible to have an embedded list or a document inside
     * of definition terms.
     * 
     * @see #endDefinitionTerm()
     */
    void beginDefinitionTerm();

    /**
     * This method is used to notify about a new list. Each list contain at
     * least one list item. All list items are notified using the
     * {@link #beginListItem()}/{@link #endListItem()} method pair. Items of
     * lists of this type can contain the following sequence of elements:
     * <ol>
     * <li><em>formatted text</em> - the text can be empty; in this case it is
     * not notified</li>
     * <li>Just after the text the list item can have:
     * <ul>
     * <li><em>an embedded list</em> - in this way it is possible to build
     * hierarchical structures</li>
     * <li><em>an embedded document</em> - it should be defined explicitly and
     * it can have all valid elements of the top-level document</li>
     * </ul>
     * </li>
     * </ol>
     * 
     * @param params parameters of the list
     * @param ordered if this flag is <code>true</code> then this method
     *        corresponds to a new ordered list ("ol"); otherwise this method
     *        notifies a beginning of an unordered list ("ul")
     * @see #endList(WikiParameters, boolean)
     */
    void beginList(WikiParameters params, boolean ordered);

    /**
     * This method is used to notify about the beginning of a new item of a
     * simple list (see {@link #beginList(WikiParameters, boolean)}/
     * {@link #endList(WikiParameters, boolean)} methods).
     * 
     * @see #endListItem()
     */
    void beginListItem();

    /**
     * Notifies about the beginning of a quotation elements.
     * 
     * @param params parameters of the list
     * @see #endQuotation(WikiParameters)
     */
    void beginQuotation(WikiParameters params);

    /**
     * A list containing quotation. This is a part of quotation lists. Each
     * quotation item can have other quotation lists.
     * 
     * @see #endQuotationLine()
     */
    void beginQuotationLine();

    /**
     * This method is used to notify about the end of a definition description.
     * 
     * @see #beginDefinitionDescription()
     */
    void endDefinitionDescription();

    /**
     * This method is used to notify about the end of a definition list.
     * 
     * @param params list of parameters for the list
     * @see #beginDefinitionList(WikiParameters)
     */
    void endDefinitionList(WikiParameters params);

    /**
     * The end of a definition term.
     * 
     * @see #beginDefinitionTerm()
     */
    void endDefinitionTerm();

    /**
     * This method is used to notify about the end of a list.
     * 
     * @param params parameters of the list
     * @param ordered if this flag is <code>true</code> then this method
     *        corresponds to a new ordered list ("ol"); otherwise this method
     *        notifies a beginning of an unordered list ("ul")
     * @see #beginList(WikiParameters, boolean)
     */
    void endList(WikiParameters params, boolean ordered);

    /**
     * This method is used to notify about the end of an item of a simple list
     * (see {@link #beginList(WikiParameters, boolean)}/
     * {@link #endList(WikiParameters, boolean)} methods).
     * 
     * @see #beginListItem()
     */
    void endListItem();

    /**
     * Notifies about the end of a quotation element sequence.
     * 
     * @param params parameters of the list
     * @see #endQuotation(WikiParameters)
     */
    void endQuotation(WikiParameters params);

    /**
     * This method is used to notify about the end of a quotation line.
     * 
     * @see #endQuotationLine()
     */
    void endQuotationLine();

}
