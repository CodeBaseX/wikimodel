/*******************************************************************************
 * Copyright (c) 2005,2007 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem;

/**
 * This interface is a marker for classes implementing all WEM listeners. In the
 * future it should be removed and replaced by an Object Adapter providing
 * individual listeners. Such a provider should be used something like that:
 * 
 * <pre>
 * IWemListenerProvider provider = new MyWemListenerProvider();
 * provider.registerListener(
 *      IWemListenerDocument.class, 
 *      new MyDocumentListener());
 * ...
 * IWemListenerDocument docListener = 
 *      provider.getListener(IWemListenerProvider.class);
 * if (docListener != null) {
 *      docListener.beginDocument();
 * }
 * </pre>
 * 
 * Adapter-based approach is much more flexible and it can be used to
 * transparently extend parsers to handle new types of structural elements.
 * 
 * @author MikhailKotelnikov
 */
public interface IWemListener
    extends
    IWemListenerDocument,
    IWemListenerSimpleBlocks,
    IWemListenerInline,
    IWemListenerTable,
    IWemListenerList,
    IWemListenerSemantic,
    IWemListenerProgramming {
    //
}
