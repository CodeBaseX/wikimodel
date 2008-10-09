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
package org.wikimodel.wem.xhtml;

import java.util.Map;
import java.util.Stack;

import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

public interface XhtmlEscapeHandler
{
    void initialize(Map<String, Object> context);
    XhtmlCharacter handleCharacter(XhtmlCharacter current, Stack<XhtmlCharacter> characters, TagContext tagContent, Map<String, Object> context);
}
