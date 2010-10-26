package org.wikimodel.wem.confluence;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.impl.InternalWikiScannerContext;
import org.wikimodel.wem.impl.WikiScannerContext;

public class ConfluenceWikiScannerContext extends WikiScannerContext {

    public ConfluenceWikiScannerContext(IWemListener listener) {
        super(listener);
    }

    protected InternalWikiScannerContext newInternalContext() {
        InternalWikiScannerContext context = new ConfluenceInternalWikiScannerContext(
            fSectionBuilder,
            fListener);
        return context;
    }
}
