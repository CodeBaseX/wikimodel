package org.wikimodel.wem.confluence;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.impl.InternalWikiScannerContext;
import org.wikimodel.wem.util.SectionBuilder;

public class ConfluenceInternalWikiScannerContext extends
        InternalWikiScannerContext {

    public ConfluenceInternalWikiScannerContext(
            SectionBuilder<WikiParameters> sectionBuilder, IWemListener listener) {
        super(sectionBuilder, listener);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Modified to make sure last empty cell of a row is not taken into account.
     * 
     * @see org.wikimodel.wem.impl.InternalWikiScannerContext#onTableCell(boolean,
     *      org.wikimodel.wem.WikiParameters)
     */
    @Override
    public void onTableCell(boolean head, WikiParameters params) {
        checkStyleOpened();
        endTableCell();
        fTableHead = head;
        fTableCellParams = params != null ? params : WikiParameters.EMPTY;

        // Commented because we don't want last empty cell of a row to be taken
        // into account
        // beginTableCell(head, params);
    }
}
