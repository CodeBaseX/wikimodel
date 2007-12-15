package org.wikimodel.app.formatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.PrintListener;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.common.CommonWikiParser;

/**
 * @author kotelnikov
 */
public class DocFormatter {

    private final static Logger log = Logger.getLogger(DocFormatter.class
        .getName());

    public static void main(String[] args)
        throws IOException,
        WikiParserException {
        final File source = new File("./doc-txt");
        File dest = new File("./doc-html");
        DocFormatter formatter = new DocFormatter("txt", "html");
        // DocFormatter formatter = new DocFormatter("txt", "tex") {
        // @Override
        // protected IWemListener newSerializer(IWikiPrinter wikiPrinter) {
        // return new TexSerializer(wikiPrinter) {
        // @Override
        // protected InputStream getImageInput(String ref) throws IOException {
        // File file = new File(source, ref);
        // FileInputStream input = new FileInputStream(file);
        // return input;
        // }
        // };
        // }
        //
        // };
        formatter.formatDir(source, dest);
    }

    private String fDestExt;

    private String fSourceExt;

    public DocFormatter(String sourceExt, String destExt) {
        fSourceExt = "." + sourceExt;
        fDestExt = "." + destExt;
    }

    private void copyFile(File from, File to) throws IOException {
        if (log.isLoggable(Level.INFO)) {
            log.info("Copy '" + from + "' => '" + to + "'");
        }
        FileInputStream in = new FileInputStream(from);
        try {
            FileOutputStream out = new FileOutputStream(to);
            try {
                byte[] buf = new byte[1024 * 10];
                int len = 0;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    protected String filterName(String name) {
        if (name.endsWith(fSourceExt))
            return name.substring(0, name.length() - fSourceExt.length())
                + fDestExt;
        return null;
    }

    private void formatDir(File source, File dest)
        throws IOException,
        WikiParserException {
        File[] list = source.listFiles();
        if (list == null)
            return;
        dest.mkdirs();
        for (File from : list) {
            String name = from.getName();
            if (from.isDirectory()) {
                File to = new File(dest, name);
                formatDir(from, to);
            } else {
                String newName = filterName(name);
                if (newName != null) {
                    File to = new File(dest, newName);
                    formatFile(from, to);
                } else {
                    File to = new File(dest, name);
                    copyFile(from, to);
                }
            }
        }
    }

    private void formatFile(File from, File to)
        throws IOException,
        WikiParserException {
        if (log.isLoggable(Level.INFO)) {
            log.info("Transform '" + from + "' => '" + to + "'");
        }
        FileReader reader = new FileReader(from);
        try {
            FileWriter writer = new FileWriter(to);
            try {
                final PrintWriter printer = new PrintWriter(writer);
                IWikiPrinter wikiPrinter = new IWikiPrinter() {

                    public void print(String str) {
                        printer.print(str);
                    }

                    public void println(String str) {
                        printer.println(str);
                    }

                };
                IWemListener listener = newSerializer(wikiPrinter);
                CommonWikiParser parser = new CommonWikiParser();
                parser.parse(reader, listener);
                printer.flush();
            } finally {
                writer.close();
            }
        } finally {
            reader.close();
        }
    }

    protected IWemListener newSerializer(IWikiPrinter wikiPrinter) {
        return new PrintListener(wikiPrinter);
    }

}
