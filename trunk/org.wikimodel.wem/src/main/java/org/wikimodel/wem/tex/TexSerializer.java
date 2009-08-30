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
package org.wikimodel.wem.tex;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.PrintTextListener;
import org.wikimodel.wem.ReferenceHandler;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.images.ImageUtil;

/**
 * @author MikhailKotelnikov
 */
public class TexSerializer extends PrintTextListener {

    private static class DocumentContext {

        boolean fFirstRowCell = false;

        boolean fTableHead = false;

        boolean fTableHeadCell = false;
    }

    class WGet {

        final String commandName = WGet.class.getName();

        int count;

        final PrintStream out = System.out;

        boolean output = true;

        boolean verb;

        // https://my.mandriva.com/rest/export/club.php?auth=a68184fa4d3acec7ac63542a70fa09d8-fee6f44d0dced0229543d9201b074377

        public final void fetchURL(String url, OutputStream output)
            throws IOException {
            URL url1 = new URL(url);
            URLConnection urlConnection = url1.openConnection();
            BufferedOutputStream bos = new BufferedOutputStream(output);
            if (urlConnection instanceof HttpURLConnection) {
                readHttpURL((HttpURLConnection) urlConnection, bos);
            } else {
                readURL(urlConnection);
            }
        }

        public final void printHeader(URLConnection url) {
            verbose(WGet.class.getName()
                + ": Content-Length   : "
                + url.getContentLength());
            verbose(WGet.class.getName()
                + ": Content-Type     : "
                + url.getContentType());
            if (url.getContentEncoding() != null)
                verbose(WGet.class.getName()
                    + ": Content-Encoding : "
                    + url.getContentEncoding());

        }

        public final void readHttpURL(HttpURLConnection url, OutputStream output)
            throws IOException {

            long before, after;

            url.setAllowUserInteraction(true);
            url.connect();
            before = System.currentTimeMillis();
            DataInputStream in = new DataInputStream(url.getInputStream());
            after = System.currentTimeMillis();

            before = System.currentTimeMillis();

            try {
                if (url.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    out.println(commandName + ": " + url.getResponseMessage());
                } else {
                    printHeader(url);
                    while (true) {
                        // writeChar((char) in.readUnsignedByte());
                        output.write(in.readByte());
                    }
                }
            } catch (EOFException e) {
                after = System.currentTimeMillis();
                int milliSeconds = (int) (after - before);
                verbose(commandName
                    + ": Read "
                    + count
                    + " bytes from "
                    + url.getURL());
                verbose(commandName
                    + ": HTTP/1.0 "
                    + url.getResponseCode()
                    + " "
                    + url.getResponseMessage());
                url.disconnect();

                if (url.usingProxy()) {
                    verbose(commandName + ": This URL uses a proxy");
                }
            } catch (IOException e) {
                out.println(e + ": " + e.getMessage());
                verbose(commandName
                    + ": I/O Error : Read "
                    + count
                    + " bytes from "
                    + url.getURL());
                out.println(commandName
                    + ": I/O Error "
                    + url.getResponseMessage());
            }
            output.flush();
            output.close();

        }

        public final void readURL(URLConnection url) throws IOException {
            DataInputStream in = new DataInputStream(url.getInputStream());
            printHeader(url);

            try {
                while (true) {
                    System.out.println((char) in.readUnsignedByte());
                }
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public final void verbose(String s) {
            if (verb)
                out.println(s);
        }

    }

    private static int tableCount = 0;

    public static String processString(String g, boolean flag) {
        String[] array1 = new String[] { "\\s", "é", "è", "ê", "à", "ô", "ù" };
        String[] array2 = new String[] {
            "_",
            "eacute;",
            "eagrave;",
            "ecicr;",
            "aacute;",
            "ocirc;",
            "ugrave;" };

        if (flag) {
            for (int i = 0; i < array1.length; i++)
                g = g.replaceAll(array1[i], array2[i]);
            return g;
        } else {
            for (int i = 1; i < array1.length; i++)
                g = g.replaceAll(array2[i], array1[i]);
            return g;
        }

    }

    int[] cols = new int[] {
        3,
        4,
        4,
        4,
        3,
        3,
        3,
        5,
        5,
        3,
        5,
        3,
        4,
        3,
        3,
        3,
        0,
        0,
        0,
        0,
        0 };

    private String documentName;

    private DocumentContext fContext;

    private Stack<DocumentContext> fContextStack = new Stack<DocumentContext>();

    private String imageTargetFolder;

    private String wikiFileDownloadBaseUrl;

    /**
     * @param printer
     */
    public TexSerializer(
        IWikiPrinter printer,
        String documentName,
        String wikiFileDownloadBaseUrl,
        String imageTargetFolderPath) {
        super(printer);
        this.documentName = documentName;
        this.imageTargetFolder = imageTargetFolderPath;
        this.wikiFileDownloadBaseUrl = wikiFileDownloadBaseUrl;

    }

    @Override
    public void beginDocument(WikiParameters params) {
        // if (fContext == null) {
        // println("\\documentclass{article}");
        // println("\\usepackage{graphics} % for pdf, bitmapped graphics files");
        // println("\\usepackage{graphicx} % for pdf, bitmapped graphics files");
        // println("\\usepackage{epsfig} % for postscript graphics files");
        // println("\\begin{document}");
        // println();
        // }
        fContext = new DocumentContext();
        fContextStack.push(fContext);
    }

    @Override
    public void beginHeader(int headerLevel, WikiParameters params) {
        println();
        print("\\");
        if (headerLevel == 1) {
            print("chapter{");
        } else if (headerLevel < 4) {
            for (int i = 1; i < headerLevel - 1; i++)
                print("sub");
            print("section{");
        } else {
            print("paragraph{");
        }
    }

    @Override
    public void beginList(WikiParameters parameters, boolean ordered) {
        println("\\begin{itemize}");
    }

    @Override
    public void beginListItem() {
        print(" \\item ");
    }

    @Override
    public void beginParagraph(WikiParameters params) {
        println("");
    }

    @Override
    public void beginTable(WikiParameters params) {
        println("\\begin{center}");
        println("\\begin{footnotesize}");
        // System.out.println("Table count: "+tableCount);
        String colwidth = "3";
        if (cols[tableCount] > 3)
            colwidth = "2";
        print("\\begin{tabular}{");
        for (int i = 0; i < cols[tableCount] + 1; i++) {
            print("|p{" + colwidth + "cm}");
        }
        println("|}\\hline");
        fContext.fTableHead = true;
        tableCount++;
    }

    @Override
    public void beginTableCell(boolean tableHead, WikiParameters params) {
        // String str = tableHead ? "\\textcolor{white}" : "";
        String str = tableHead ? "" : "";

        print(str + params);
        if (tableHead)
            fContext.fTableHeadCell = true;
        if (!fContext.fFirstRowCell)
            print("&");
        fContext.fFirstRowCell = false;
    }

    @Override
    public void beginTableRow(WikiParameters params) {
        if (fContext.fTableHead)
            // print("\\rowcolor{style@lightblue}");
            print("");
        else
            print("");
        fContext.fFirstRowCell = true;
    }

    @Override
    public void endDocument(WikiParameters params) {
        fContextStack.pop();
        fContext = !fContextStack.empty() ? fContextStack.peek() : null;
        // if (fContext == null) {
        // println("\\end{document}");
        // }
    }

    @Override
    public void endHeader(int headerLevel, WikiParameters params) {
        println("}");

    }

    @Override
    public void endList(WikiParameters parameters, boolean ordered) {
        println("\\end{itemize}");
    }

    @Override
    public void endListItem() {
        println("");
    }

    @Override
    public void endParagraph(WikiParameters params) {
        println("");
    }

    @Override
    public void endQuotationLine() {
        println("");
    }

    @Override
    public void endTable(WikiParameters params) {
        println("\\end{tabular}");
        println("\\end{footnotesize}");
        println("\\end{center}");

    }

    @Override
    public void endTableCell(boolean tableHead, WikiParameters params) {
        if (tableHead)
            print("}");

    }

    @Override
    public void endTableRow(WikiParameters params) {
        println("\\\\\\hline");
        fContext.fTableHead = false;
    }

    /**
     * Returns an input stream with an image corresponding to the specified
     * reference. If there is no image was found then this method should return
     * null. This method is used to define dimensions of images used in the
     * output. Sould be overloaded in sublcasses.
     * 
     * @param ref the image reference
     * @return the input stream with an image
     * @throws IOException
     */
    protected InputStream getImageInput(String ref) throws IOException {
        File f = new File(imageTargetFolder + ref);
        if (!f.exists()) {

            FileOutputStream fos = new FileOutputStream(f);
            String url = wikiFileDownloadBaseUrl + documentName + "/" + ref;
            System.out.println("Downloading image "
                + url
                + " to "
                + f.getAbsolutePath()
                + "...");
            try {
                WGet wget = new WGet();
                wget.fetchURL(url, fos);

            } catch (Exception e) {
                e.printStackTrace();
            }
            fos.flush();
            fos.close();

        }
        FileInputStream fis = new FileInputStream(f);
        return fis;
    }

    /**
     * Returns a two-value array with the size of the image defined by the given
     * url
     * 
     * @param ref the reference to the image
     * @return a size of an image with the specified url;
     */
    protected int[] getImageSize(String ref) {
        int[] result = null;
        try {
            InputStream input = getImageInput(ref);
            if (input != null) {
                int maxWidth = getMaxImageWidth();
                int maxHeight = getMaxImageHeight();
                return ImageUtil.getImageSize(input, maxWidth, maxHeight);
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * Returns maximal possible image height. This method can be overloaded in
     * subclasses.
     * 
     * @return the maximal possible image height
     */
    protected int getMaxImageHeight() {
        return 2000;
    }

    /**
     * Returns maximal possible image width. This method can be overloaded in
     * subclasses.
     * 
     * @return the maximal possible image width
     */
    protected int getMaxImageWidth() {
        return 2000;
    }

    @Override
    protected ReferenceHandler newReferenceHandler() {
        return new ReferenceHandler() {

            Map<String, int[]> fImageSizes = new HashMap<String, int[]>();

            @Override
            protected void handleImage(
                String ref,
                String label,
                WikiParameters params) {

                String caption = "";
                int idx = ref.indexOf("===caption===");
                if (idx > 0) {
                    caption = ref.substring(idx + 13, ref.length() - 13);
                    caption = caption.replaceAll("_", " ");
                    caption = processString(caption, false);
                    ref = ref.substring(0, idx);
                    // removing extension
                    label = ref.substring(0, ref.length() - 4);
                }

                int[] size = getImageSize(ref);

                println();
                println("\\begin{figure}");
                println("\\centering");

                if (size[0] > 400)
                    println("\\includegraphics[width=0.95\\textwidth]{images/"
                        + ref
                        + "}");
                else
                    println("\\includegraphics{images/" + ref + "}");
                println("\\caption{" + caption + "}");
                println("\\label{" + label + "}");
                println("\\end{figure}");
                println();

                if (fImageSizes.containsKey(ref))
                    size = fImageSizes.get(ref);
                else {
                    size = getImageSize(ref);
                    fImageSizes.put(ref, size);
                }

                // if (size != null) {
                // // print("\\begin{figure}[htpb]\n");
                // String dim = "[bb=0 0 " + size[0] + " " + size[1] + "]";
                // println("\\includegraphics" + dim + "{"
                // + WikiPageUtil.escapeXmlString(ref) + "}");
                // ref = ref.replaceAll("_", "-");
                // // if (!"".equals(label)) {
                // // println("\\caption{" + label + "}");
                // // println("\\label{fig:" + label + "}");
                // // }
                // // print("\\end{figure}");
                // }
            }

            @Override
            protected void handleReference(
                String ref,
                String label,
                WikiParameters params) {
                // String s = ref + " " + label;
                int idx1 = ref.indexOf('>');

                if (idx1 > 0) {
                    label = ref.substring(0, idx1);
                    ref = ref.substring(idx1 + 1);
                }

                ref = ref.substring(ref.indexOf('.') + 1);
                print(texClean(label) + "~(\\ref{" + ref + "})");
                // print(texClean(ref)+ texClean(label));
                // print(" (");
                // print(texClean(ref));
                // print(")");
                // print("~\ref{"+ref+"}");
            }

        };
    }

    @Override
    public void onEscape(String str) {
        print(str);
    }

    @Override
    public void onLineBreak() {
        println("\\newline");
    }

    @Override
    public void onNewLine() {
        println("");
    }

    @Override
    public void onSpace(String str) {
        print(str);

    }

    @Override
    public void onSpecialSymbol(String str) {

        if (str.equals("#")) {
            print("\\_\\_");
            return;
        }

        if (str.equals("&")) {
            print("\\&");
            return;
        }

        if (str.equals("$")) {
            print("\\$");
            return;
        }

        if (str.equals("%")) {
            print("\\%");
            return;
        }

        if (!str.equals("}") && !str.equals("{")) {
            print(str);
        }

    }

    @Override
    public void onWord(String str) {
        str = texClean(str);
        if (fContext.fTableHeadCell) {
            print("{\\bf ");
            fContext.fTableHeadCell = false;
        }
        print(str);
    }

    public String texClean(String str) {

        str = str.replace("_", "\\_");
        str = str.replace("#", "\\#");
        str = str.replace("$", "\\$");

        return str;
    }

}
