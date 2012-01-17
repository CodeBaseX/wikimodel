package org.wikimodel.wem.confluence;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.impl.WikiScannerContext;

public class ConfluenceWikiScannerContext extends WikiScannerContext {

    public ConfluenceWikiScannerContext(IWemListener listener) {
        super(listener);
    }
    
    @Override
    protected ConfluenceInternalWikiScannerContext newInternalContext() {
      ConfluenceInternalWikiScannerContext context = new ConfluenceInternalWikiScannerContext(
            fSectionBuilder,
            fListener);
        return context;
    }
    
    @Override
    public ConfluenceInternalWikiScannerContext getContext() {
      if (!fStack.isEmpty())
        return (ConfluenceInternalWikiScannerContext)fStack.peek();
      ConfluenceInternalWikiScannerContext context = newInternalContext();
      fStack.push(context);
      return context;
    }
    
    public boolean isExplicitInTable() {
      return getContext().isExplicitInTable();
    }
    
}
