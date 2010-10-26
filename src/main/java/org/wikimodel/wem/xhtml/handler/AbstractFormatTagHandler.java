/*******************************************************************************
 * Copyright (c) 2005,2006 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.xhtml.handler;

import java.io.StringReader;

import org.w3c.css.sac.InputSource;
import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiStyle;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.parser.CSSOMParser;

/**
 * @author vmassol
 * @author thomass.mortagne
 */
public abstract class AbstractFormatTagHandler extends TagHandler {
    public static final String FORMATWIKISTYLE = "formatWikiStyle";

    public static final String FORMATPARAMETERS = "formatParameters";

    public static final String FORMATSTYLEPARAMETER = "formatStyleParameter";

    private final WikiStyle style;

    /**
     * The object used to parse the style attribute.
     */
    private final CSSOMParser cssParser = new CSSOMParser();

    public AbstractFormatTagHandler() {
        this(null);
    }

    protected AbstractFormatTagHandler(WikiStyle style) {
        super(false, false, true);

        this.style = style;
    }

    @Override
    protected void begin(TagContext context) {
        // parameters
        WikiParameters currentParameters = (WikiParameters) context
                .getTagStack().getStackParameter(FORMATPARAMETERS);
        CSSStyleDeclarationImpl currentStyle = (CSSStyleDeclarationImpl) context
                .getTagStack().getStackParameter(FORMATSTYLEPARAMETER);

        if (currentParameters != null) {
            for (WikiParameter parameter : context.getParams()) {
                WikiParameter currentParameter = currentParameters
                        .getParameter(parameter.getKey());

                String value = parameter.getValue();

                if (currentParameter != null) {
                    if (parameter.getKey().equals("style")) {
                        CSSStyleDeclarationImpl mergedStyle = mergeStyle(
                            currentStyle, currentParameter.getValue(),
                            parameter.getValue());

                        if (mergedStyle != currentStyle) {
                            value = mergedStyle.getCssText();
                            currentStyle = mergedStyle;
                        }
                    } else if (parameter.getKey().equals("class")) {
                        value = mergeClass(currentParameter.getValue(),
                            parameter.getValue());
                    }
                }

                currentParameters = currentParameters.setParameter(
                    parameter.getKey(), value);
            }
        } else {
            currentParameters = new WikiParameters(context.getParams());
        }
        context.getTagStack().pushStackParameter(FORMATPARAMETERS,
            currentParameters);
        context.getTagStack().pushStackParameter(FORMATSTYLEPARAMETER,
            currentStyle);

        if (currentParameters.getSize() > 0) {
            context.getScannerContext().beginFormat(currentParameters);
        }

        // style
        if (this.style != null) {
            context.getScannerContext().beginFormat(this.style);
            context.getTagStack().pushStackParameter(FORMATWIKISTYLE,
                this.style);
        }
    }

    private CSSStyleDeclarationImpl mergeStyle(
            CSSStyleDeclarationImpl parentStyle, String parentStyleValue,
            String styleValue) {
        CSSStyleDeclarationImpl currentStyle = new CSSStyleDeclarationImpl();

        if (parentStyle == null) {
            try {
                this.cssParser.parseStyleDeclaration(currentStyle,
                    new InputSource(new StringReader(parentStyleValue)));
            } catch (Exception e) {
                return parentStyle;
            }
        } else {
            currentStyle.setProperties(parentStyle.getProperties());
        }

        try {
            this.cssParser.parseStyleDeclaration(currentStyle, new InputSource(
                new StringReader(styleValue)));
        } catch (Exception e) {
            return parentStyle;
        }

        return currentStyle;
    }

    private String mergeClass(String value1, String value2) {
        return value1 + " " + value2;
    }

    @Override
    protected void end(TagContext context) {
        // style
        if (this.style != null) {
            context.getScannerContext().endFormat(this.style);
            context.getTagStack().popStackParameter(FORMATWIKISTYLE);
        }

        // parameters
        if (context.getParams().getSize() > 0) {
            context.getScannerContext().endFormat(WikiParameters.EMPTY);
        }

        context.getTagStack().popStackParameter(FORMATPARAMETERS);
        context.getTagStack().popStackParameter(FORMATSTYLEPARAMETER);

        // reopen

        WikiStyle currentWikiStyle = (WikiStyle) context.getTagStack()
                .getStackParameter(FORMATWIKISTYLE);
        WikiParameters currentParameters = (WikiParameters) context
                .getTagStack().getStackParameter(FORMATPARAMETERS);

        if (currentParameters != null && currentParameters.getSize() > 0) {
            context.getScannerContext().beginFormat(currentParameters);
            if (currentWikiStyle != null) {
                context.getScannerContext().beginFormat(currentWikiStyle);
            }
        }
    }
}
