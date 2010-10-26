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

public class XhtmlCharacter {
    private char fCharacter;

    private XhtmlCharacterType fType;

    public XhtmlCharacter(char character, XhtmlCharacterType type) {
        fCharacter = character;
        fType = type;
    }

    public char getCharacter() {
        return fCharacter;
    }

    public XhtmlCharacterType getType() {
        return fType;
    }

    public void setType(XhtmlCharacterType type) {
        fType = type;
    }
}
