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
 * @author MikhailKotelnikov
 */
public class TexListener implements IWemListener
{

    boolean tableHead = false;

    private StringBuffer fBuffer;

    public TexListener()
    {
    }

    /**
     * @param buf
     */
    public TexListener(StringBuffer buf)
    {
        fBuffer = buf;
    }

    public void beginDefinitionDescription()
    {
        print("  <dd>");
    }

    public void beginDefinitionList(WikiParameters parameters)
    {
        println("<dl>");
    }

    public void beginDefinitionTerm()
    {
        print("  <dt>");
    }

    public void beginDocument()
    {
        // println("<div class='doc'>");
    }

    public void beginHeader(int level, WikiParameters params)
    {
        // print("<h" + level + params + ">");
        print("\\");
        for (int i = 0; i < level - 1; i++)
            print("sub");
        print("section{");
        // println("\\label{section:"+params+"}");

    }

    public void beginInfoBlock(char infoType, WikiParameters params)
    {
        print("<table" + params + "><tr><th>" + infoType + "</th><td>");
    }

    public void beginList(WikiParameters parameters, boolean ordered)
    {
        if (ordered)
            println("<ol" + parameters + ">");
        else
            println("\\begin{itemize}");
    }

    public void beginListItem()
    {
        print(" \\item ");
    }

    public void beginParagraph(WikiParameters params)
    {
        println("");
    }

    public void beginPropertyBlock(String propertyUri, boolean doc)
    {
        print("<div class='property' url='" + WikiPageUtil.escapeXmlAttribute(propertyUri) + "'>");
        if (doc)
            println("");
    }

    public void beginQuotation(WikiParameters params)
    {
        print("<quot" + params + ">");
    }

    public void beginTable(WikiParameters params)
    {
        // println("<table" + params + ">");
        println("\\begin{center}");
        println("\\begin{footnotesize}");
        println("\\begin{tabular}{|p{6cm}|p{5cm}|}\\hline");
        tableHead = true;
    }

    boolean isFirstRowCell = false;
    
    public void beginTableCell(boolean tableHead, WikiParameters params)
    {
        String str = tableHead ? "\\textcolor{white}" : "";
        print(str + params);
        if (tableHead)
            tableHeadCell = true;
        if (!isFirstRowCell)
            print("&");
        isFirstRowCell = false;
    }

    public void beginTableRow(WikiParameters params)
    {
        if (tableHead)
            print("\\rowcolor{style@lightblue}");
        else
            print("");
        isFirstRowCell = true;
    }

    public void endDefinitionDescription()
    {
        println("</dd>");
    }

    public void endDefinitionList(WikiParameters parameters)
    {
        println("</dl>");
    }

    public void endDefinitionTerm()
    {
        println("</dt>");
    }

    public void endDocument()
    {

    }

    public void endHeader(int level, WikiParameters params)
    {
        println("}");
    }

    public void endInfoBlock(char infoType, WikiParameters params)
    {
        println("</td></tr></table>");
    }

    public void endList(WikiParameters parameters, boolean ordered)
    {
        if (ordered)
            println("</ol>");
        else
            println("\\end{itemize}");
    }

    public void endListItem()
    {
        println("");
    }

    public void endParagraph(WikiParameters params)
    {
        println("");
    }

    public void endPropertyBlock(String propertyUri, boolean doc)
    {
        println("</div>");
    }

    public void endQuotation(WikiParameters params)
    {
        println("</quot>");
    }

    public void endQuotationLine()
    {
        println("");
    }

    public void endTable(WikiParameters params)
    {
        println("\\end{tabular}");
        println("\\end{footnotesize}");
        println("\\end{center}");

    }

    public void endTableCell(boolean tableHead, WikiParameters params)
    {
        // String str = tableHead ? "</th>" : "</td>";
        if (tableHead)
            print("}");
        
    }

    public void endTableRow(WikiParameters params)
    {
        println("\\\\\\hline");
        tableHead = false;
    }

    /**
     * @see org.wikimodel.wem.PrintInlineListener#onExtensionBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionBlock(String extensionName, WikiParameters params)
    {
        println("<div class='extension' extension='" + extensionName + "' " + params + " />");
    }

    public void onHorizontalLine()
    {
        println("<hr />");
    }

    public void onTableCaption(String str)
    {
    }

    public void onVerbatimBlock(String str)
    {
        println("<pre>" + WikiPageUtil.escapeXmlString(str) + "</pre>");
    }

    protected void print(String str)
    {
        if (fBuffer != null) {
            fBuffer.append(str);
        } else {
            System.out.print(str);
        }
    }

    protected void println(String str)
    {
        if (fBuffer != null) {
            fBuffer.append(str);
            fBuffer.append("\n");
        } else {
            System.out.println(str);
        }
    }

    public void beginFormat(WikiFormat format)
    {
        // TODO Auto-generated method stub

    }

    public void beginPropertyInline(String str)
    {
        // TODO Auto-generated method stub

    }

    public void beginQuotationLine()
    {
        // TODO Auto-generated method stub

    }

    public void endFormat(WikiFormat format)
    {
        // TODO Auto-generated method stub

    }

    public void endPropertyInline(String inlineProperty)
    {
        // TODO Auto-generated method stub

    }

    public void onEscape(String str)
    {
        // TODO Auto-generated method stub

    }

    public void onExtensionInline(String extensionName, WikiParameters params)
    {
        // TODO Auto-generated method stub

    }

    public void onLineBreak()
    {
        // TODO Auto-generated method stub

    }

    public void onNewLine()
    {
        // TODO Auto-generated method stub

    }

    public void onReference(String ref)
    {
        if (ref.indexOf("image:") == 0) {
            print("\\begin{figure}[htpb]\n\\centering\n\\vspace{0cm}\n");
            if (ref.indexOf("image:") == 0)
                ref = ref.substring("image:".length());
            println("\\includegraphics{figures/" + WikiPageUtil.escapeXmlString(ref) + "}");
            ref = ref.replaceAll("_","-");
            println("\\caption{" + ref + "}");
            println("\\label{fig:" + ref + "}");
            print("\\end{figure}");
        } else {
            print("<a href='" + WikiPageUtil.escapeXmlAttribute(ref) + "'>"
                + WikiPageUtil.escapeXmlString(ref) + "</a>");
        }

    }

    public void onSpace(String str)
    {
        print(str);

    }

    public void onSpecialSymbol(String str)
    {
        if (str.equals("}") || str.equals("{"))
            return;
        else
            print(str);

    }

    public void onVerbatimInline(String str)
    {
        print("<code>" + WikiPageUtil.escapeXmlString(str) + "</code>");

    }

    boolean tableHeadCell = false;

    public void onWord(String str)
    {
        //str = str.replaceAll("é", "\\\\'e");
        if (tableHeadCell) {
            print("{\\bf ");
            tableHeadCell = false;
        }
        print(str);

    }

	public void onMacro(String macroName, WikiParameters params, String content) {
		// TODO Auto-generated method stub
		
	}

	public void onEmptyLines(int count) {
		// TODO Auto-generated method stub
		
	}

	public void onMacroBlock(String macroName, WikiParameters params,
			String content) {
		// TODO Auto-generated method stub
		
	}

	public void onMacroInline(String macroName, WikiParameters params,
			String content) {
		// TODO Auto-generated method stub
		
	}
}
